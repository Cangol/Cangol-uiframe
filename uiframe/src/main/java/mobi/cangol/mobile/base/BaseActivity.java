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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.session.Session;

/**
 * @author Cangol
 */
public abstract class BaseActivity extends Activity implements BaseActivityDelegate {
    protected final String TAG = Log.makeLogTag(this.getClass());
    private CoreApplication app;
    private long startTime;
    private HandlerThread handlerThread;
    private Handler threadHandler;
    private Handler uiHandler;
    public float getIdleTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.setLogTag(this);
        Log.v(TAG, "onCreate");
        startTime = System.currentTimeMillis();
        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        threadHandler = new InternalHandler(this,handlerThread.getLooper());
        uiHandler= new InternalHandler(this,Looper.getMainLooper());
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(TAG, "onNewIntent");
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
        Log.v(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    public Session getSession() {
        return app.getSession();
    }

    public AppService getAppService(String name) {
        return app.getAppService(name);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v(TAG, "onConfigurationChanged");
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        app.delActivityFromManager(this);
        handlerThread.quit();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");
        super.onBackPressed();
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
    /**
     * 处理back事件
     */
    @Override
    public void onBack() {
        Log.v(TAG, "onBack");
        super.onBackPressed();
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
        // do somethings
    }
    protected  static class StaticInnerRunnable implements Runnable{
        @Override
        public void run() {
            // do somethings
        }
    }
    static final class InternalHandler extends Handler {
        private final WeakReference<Context> mContext;

        public InternalHandler(Context context,Looper looper) {
            super(looper);
            mContext = new WeakReference<>(context);
        }

        public void handleMessage(Message msg) {
            Context context = mContext.get();
            if (context != null) {
               ((BaseActivity)context).handleMessage(msg);
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
