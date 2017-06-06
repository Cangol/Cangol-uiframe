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
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.session.SessionService;

/**
 * @author Cangol
 */
public abstract class BaseActivity extends Activity implements BaseActivityDelegate {
    protected final String TAG = Log.makeLogTag(this.getClass());
    private static final boolean LIFECYCLE = Log.getLevel() >= android.util.Log.VERBOSE;
    public CoreApplication app;
    private long startTime;
    private Handler handler;
    public float getIdletime() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.setLogTag(this);
        if (LIFECYCLE) Log.v(TAG, "onCreate");
        startTime = System.currentTimeMillis();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        handler = new InternalHandler(this,handlerThread.getLooper());
        app = (CoreApplication) this.getApplication();
        app.addActivityToManager(this);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (LIFECYCLE) Log.v(TAG, "onNewIntent");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (LIFECYCLE) Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LIFECYCLE) Log.v(TAG, "onResume " + getIdletime() + "s");
    }

    @Override
    protected void onPause() {
        if (LIFECYCLE) Log.v(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (LIFECYCLE) Log.v(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        if (LIFECYCLE) Log.v(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (LIFECYCLE) Log.v(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (LIFECYCLE) Log.v(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    public SessionService getSession() {
        return app.getSession();
    }

    public AppService getAppService(String name) {
        return app.getAppService(name);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (LIFECYCLE) Log.v(TAG, "onConfigurationChanged");
    }

    @Override
    protected void onDestroy() {
        if (LIFECYCLE) Log.v(TAG, "onDestroy");
        app.delActivityFromManager(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (LIFECYCLE) Log.v(TAG, "onBackPressed");
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
    public void hideSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    /**
     * 处理back事件
     */
    @Override
    public void onBack() {
        if (LIFECYCLE) Log.v(TAG, "onBack");
        super.onBackPressed();
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void postRunnable(Runnable runnable) {
        if (handler!= null && runnable != null)
            handler.post(runnable);
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
                handleMessage(msg);
            }
        }
    }
}
