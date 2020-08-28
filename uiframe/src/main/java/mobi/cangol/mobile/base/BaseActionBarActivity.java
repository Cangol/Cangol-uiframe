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
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import android.view.KeyEvent;
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
    protected CoreApplication app;
    protected CustomFragmentManager stack;
    private long startTime;
    private HandlerThread handlerThread;
    private Handler threadHandler;
    private Handler uiHandler;
    public float getIdleTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.v(TAG, "onCreate");
        startTime = System.currentTimeMillis();
        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        threadHandler = new InternalHandler(this,handlerThread.getLooper());
        uiHandler= new InternalHandler(this,Looper.getMainLooper());
        app = (CoreApplication) this.getApplication();
        app.addActivityToManager(this);
        getCustomActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void showToast(int resId) {
        if(!isFinishing())Toast.makeText(this.getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showToast(String str) {
        if(!isFinishing())Toast.makeText(this.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showToast(int resId, int duration) {
        if(!isFinishing())Toast.makeText(this.getApplicationContext(), resId, duration).show();
    }
    @Override
    public void showToast(String str, int duration) {
        if(!isFinishing())Toast.makeText(this.getApplicationContext(), str, duration).show();
    }

    /**
     * 初始化Custom Fragment管理栈
     *
     * @param containerId
     */
    @Override
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
    @Override
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
    public void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args,int moduleId) {
        this.replaceFragment(fragmentClass,tag,args);
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (null == stack||stack.size()==0||stack.peek()==null) {
            return super.onKeyUp(keyCode, event);
        }else {
            if(stack.peek().onKeyUp(keyCode, event)){
                return true;
            }else {
                return super.onKeyUp(keyCode, event);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null == stack||stack.size()==0||stack.peek()==null) {
            return super.onKeyDown(keyCode, event);
        }else {
            if(stack.peek().onKeyDown(keyCode, event)){
                return true;
            }else {
                return super.onKeyDown(keyCode, event);
            }
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
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void hideSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    public void onMenuActionCreated(ActionMenu actionMenu) {
        if (stack != null && stack.size() > 0&&null!=stack.peek()&&stack.peek().isEnable()) {
            ((BaseContentFragment) stack.peek()).onMenuActionCreated(actionMenu);
        }
    }

    @Override
    public boolean onMenuActionSelected(ActionMenuItem action) {
        if (null != stack&& null!=stack.peek()&&stack.peek().isEnable()&& stack.peek().isVisible()) {
            return ((BaseContentFragment) stack.peek()).onMenuActionSelected(action);
        }
        return false;
    }

    @Override
    public final void onBackPressed() {
        Log.v(TAG, "onBackPressed ");
        if (null == stack||stack.size()==0||stack.peek()==null) {
            onBack();
        }else {
            if (!stack.peek().onBackPressed()){
                if (stack.size() == 1)  {
                    onBack();
                }else{
                    stack.popBackStack();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.v(TAG, "onSupportNavigateUp ");
        if (stack == null||stack.size()==0||stack.peek()==null) {
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
                        replaceFragment(upFragment.clazz, upFragment.tag, upFragment.args);
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
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume " + getIdleTime() + "s");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy" );
        if (null != stack)stack.destroy();
        app.delActivityFromManager(this);
        handlerThread.quit();
        super.onDestroy();
    }

    public void onBack() {
        Log.v(TAG, "onBack");
        super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null != stack) stack.restoreState(savedInstanceState);
        Log.v(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != stack) stack.saveState(outState);
        Log.v(TAG, "onSaveInstanceState");
    }

    @Override
    public Object getLastCustomNonConfigurationInstance() {
        Log.v(TAG, "getLastCustomNonConfigurationInstance");
        return super.getLastCustomNonConfigurationInstance();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        Log.v(TAG, "onRetainCustomNonConfigurationInstance");
        return super.onRetainCustomNonConfigurationInstance();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v(TAG, "onConfigurationChanged");
    }

    @Override
    public Handler getUiHandler() {
        return uiHandler;
    }

    @Override
    public Handler getThreadHandler() {
        return threadHandler;
    }

    protected void postRunnable(StaticInnerRunnable runnable) {
        if (threadHandler!= null && runnable != null)
            threadHandler.post(runnable);
    }
    protected void handleMessage(Message msg) {
        //do somethings
    }
    protected  static class StaticInnerRunnable implements Runnable{
        @Override
        public void run() {
            //do somethings
        }
    }
    static final  class InternalHandler extends Handler {
        private final WeakReference<Context> mContext;

        public InternalHandler(Context context,Looper looper) {
            super(looper);
            mContext = new WeakReference<>(context);
        }

        public void handleMessage(Message msg) {
            Context context = mContext.get();
            if (context != null) {
                ((BaseActionBarActivity)context).handleMessage(msg);
            }
        }
    }

    @ColorInt
    @Override
    public  int getThemeAttrColor(@AttrRes int colorAttr) {
        TypedArray array = this.obtainStyledAttributes(null, new int[]{colorAttr});
        try {
            return array.getColor(0, 0);
        } finally {
            array.recycle();
        }
    }
}
