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
import androidx.fragment.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.session.Session;
import mobi.cangol.mobile.service.session.SessionService;

public abstract class BaseFragmentActivity extends FragmentActivity implements BaseActivityDelegate, CustomFragmentActivityDelegate {
    protected final String TAG = Log.makeLogTag(this.getClass());
    protected CoreApplication app;
    private CustomFragmentManager stack;
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
        Log.setLogTag(this);
        startTime = System.currentTimeMillis();
        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        threadHandler = new BaseActionBarActivity.InternalHandler(this,handlerThread.getLooper());
        uiHandler= new BaseActionBarActivity.InternalHandler(this,Looper.getMainLooper());
        app = (CoreApplication) this.getApplication();
        app.addActivityToManager(this);
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
     * 初始自定义栈管理器
     *
     * @param containerId
     */
    @Override
    public void initFragmentStack(int containerId) {
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

    /**
     * 获取AppService
     *
     * @param name
     * @return
     */
    public AppService getAppService(String name) {
        return app.getAppService(name);
    }

    /**
     * 获取Session
     *
     * @return
     */
    public Session getSession() {
        return app.getSession();
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
        Log.v(TAG, "onDestroy");
        if (null != stack)stack.destroy();
        app.delActivityFromManager(this);
        handlerThread.quit();
        super.onDestroy();
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

    /**
     * 设置全屏
     *
     * @param fullscreen
     */
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
    public boolean isFullScreen() {
        int flag = this.getWindow().getAttributes().flags;
         return (flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }
    @Override
    public final  void onBackPressed() {
        Log.v(TAG, "onBackPressed ");
        if (null == stack||stack.size()==0||stack.peek()==null) {
            onBack();
        }else {
            if (!stack.peek().onBackPressed()) {
                if (stack.size() == 1)  {
                    onBack();
                }else{
                    stack.popBackStack();
                }
            }
        }
    }

    /**
     * 处理back事件
     */
    @Override
    public void onBack() {
        Log.v(TAG, "onBack");
        super.onBackPressed();
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
                ((BaseFragmentActivity)context).handleMessage(msg);
            }
        }
    }

    @ColorInt
    public  int getThemeAttrColor(@AttrRes int colorAttr) {
        TypedArray array = this.obtainStyledAttributes(null, new int[]{colorAttr});
        try {
            return array.getColor(0, 0);
        } finally {
            array.recycle();
        }
    }
}
