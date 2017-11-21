/*
 * Copyright (c) 2013 Cangol
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.actionbar.ActionBarActivity;
import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.session.SessionService;

public abstract class BaseActionBarActivity extends ActionBarActivity implements BaseActivityDelegate, CustomFragmentActivityDelegate {
    protected final String TAG = Log.makeLogTag(this.getClass());
    private static final boolean LIFECYCLE = Log.getLevel() >= android.util.Log.VERBOSE;
    protected CoreApplication app;
    protected CustomFragmentManager stack;
    private long startTime;
    private HandlerThread handlerThread;
    private Handler handler;
    public float getIdleTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (LIFECYCLE) Log.v(TAG, "onCreate");
        startTime = System.currentTimeMillis();
        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        handler = new InternalHandler(this,handlerThread.getLooper());
        app = (CoreApplication) this.getApplication();
        app.addActivityToManager(this);
        getCustomActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showToast(int resId, int duration) {
        Toast.makeText(this, resId, duration).show();
    }
    @Override
    public void showToast(String str, int duration) {
        Toast.makeText(this, str, duration).show();
    }

    /**
     * 初始化Custom Fragment管理栈
     *
     * @param containerId
     */
    public void initFragmentStack(int containerId) {
        if (containerId <= 0) {
            throw new IllegalStateException("getContainerId must return a valid  containerId");
        }
        if (null == stack)
            stack = CustomFragmentManager.forContainer(this, containerId, this.getSupportFragmentManager());
    }

    /**
     * 替换fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    public void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        if (null == stack) {
            throw new IllegalStateException("stack is null");
        }else if(!stack.isStateSaved()){
            stack.replace(fragmentClass, tag, args);
            stack.commit();
        }else{
            Log.e(TAG,"Can not perform this action after onSaveInstanceState");
        }
    }

    @Override
    public CustomFragmentManager getCustomFragmentManager() {
        return stack;
    }

    @Override
    public AppService getAppService(String name) {
        return app.getAppService(name);
    }

    @Override
    public SessionService getSession() {
        return app.getSession();
    }

    @Override
    public void setFullScreen(boolean fullscreen) {
        if (fullscreen) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void showSoftInput(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
        editText.setText(null);
    }
    @Override
    public void hideSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void onMenuActionCreated(ActionMenu actionMenu) {
        if (stack != null && stack.size() > 0) {
            ((BaseContentFragment) stack.peek()).onMenuActionCreated(actionMenu);
        }
    }

    @Override
    public boolean onMenuActionSelected(ActionMenuItem action) {
        if (null != stack&& stack.peek().isEnable()&& stack.peek().isVisible()) {
            return ((BaseContentFragment) stack.peek()).onMenuActionSelected(action);
        }
        return false;
    }

    @Override
    final public void onBackPressed() {
        if (LIFECYCLE) Log.v(TAG, "onBackPressed ");
        if (null == stack||stack.size()==0) {
            onBack();
            return;
        }else {
            if (stack.peek().onBackPressed()) {
                return;
            } else {
                if (stack.size() == 1)  {
                    onBack();
                    return;
                }else{
                    stack.popBackStack();
                    return;
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (LIFECYCLE)Log.v(TAG, "onSupportNavigateUp ");
        if (stack == null||stack.size()==0) {
            return super.onSupportNavigateUp();
        }else{
            if (stack.peek().onSupportNavigateUp()) {
                return true;
            } else {
                if (stack.size() == 1) {
                    return super.onSupportNavigateUp();
                } else{
                    FragmentInfo upFragment = stack.peek().getNavigtionUpToFragment();
                    if (upFragment != null) {
                        stack.popBackStack();
                        replaceFragment(upFragment.clss, upFragment.tag, upFragment.args);
                    } else {
                        stack.popBackStack();
                    }
                    return true;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (LIFECYCLE) Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LIFECYCLE) Log.v(TAG, "onResume " + getIdleTime() + "s");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (LIFECYCLE) Log.v(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (LIFECYCLE) Log.v(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (LIFECYCLE) Log.v(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (LIFECYCLE) Log.v(TAG, "onDestroy" );
        if (null != stack)stack.destroy();
        app.delActivityFromManager(this);
        handlerThread.quit();
    }

    public void onBack() {
        if (LIFECYCLE) Log.v(TAG, "onBack");
        super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null != stack) stack.restoreState(savedInstanceState);
        if (LIFECYCLE) Log.v(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != stack) stack.saveState(outState);
        if (LIFECYCLE) Log.v(TAG, "onSaveInstanceState");
    }

    @Override
    public Object getLastCustomNonConfigurationInstance() {
        if (LIFECYCLE) Log.v(TAG, "getLastCustomNonConfigurationInstance");
        return super.getLastCustomNonConfigurationInstance();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if (LIFECYCLE) Log.v(TAG, "onRetainCustomNonConfigurationInstance");
        return super.onRetainCustomNonConfigurationInstance();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (LIFECYCLE) Log.v(TAG, "onConfigurationChanged");
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    protected void postRunnable(StaticInnerRunnable runnable) {
        if (handler!= null && runnable != null)
            handler.post(runnable);
    }
    protected void handleMessage(Message msg) {

    }
    protected  static class StaticInnerRunnable implements Runnable{
        @Override
        public void run() {
        }
    }
    final static class InternalHandler extends Handler {
        private final WeakReference<Context> mContext;

        public InternalHandler(Context context,Looper looper) {
            super(looper);
            mContext = new WeakReference<Context>(context);
        }

        public void handleMessage(Message msg) {
            Context context = mContext.get();
            if (context != null) {
                ((BaseActionBarActivity)context).handleMessage(msg);
            }
        }
    }
}
