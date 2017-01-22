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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.Toast;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.session.SessionService;

public abstract class BaseFragmentActivity extends FragmentActivity implements BaseActivityDelegate, CustomFragmentActivityDelegate {
    protected final String TAG = Log.makeLogTag(this.getClass());
    private static final boolean LIFECYCLE = Log.getLevel() >= android.util.Log.VERBOSE;
    protected CoreApplication app;
    private CustomFragmentManager stack;
    private long startTime;

    public float getIdletime() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.setLogTag(this);
        app = (CoreApplication) this.getApplication();
        app.addActivityToManager(this);
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId, int duration) {
        Toast.makeText(this, resId, duration).show();
    }

    public void showToast(String str, int duration) {
        Toast.makeText(this, str, duration).show();
    }

    /**
     * 初始自定义栈管理器
     *
     * @param containerId
     */
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
    public void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        if (null == stack) {
            throw new IllegalStateException("stack is null");
        }
        stack.replace(fragmentClass, tag, args);
        stack.commit();
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
    public SessionService getSession() {
        return app.getSession();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (LIFECYCLE) Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LIFECYCLE) Log.v(TAG, "onResume");
        if (LIFECYCLE) Log.v(TAG, "onResume " + getIdletime() + "s");
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
        if (LIFECYCLE) Log.v(TAG, "onDestroy " + getIdletime() + "s");

        app.delActivityFromManager(this);
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
    final public void onBackPressed() {
        if (LIFECYCLE) Log.v("onBackPressed");
        if (null == stack) {
            onBack();
            return;
        }
        if (stack.size() <= 1) {
            onBack();
        } else {
            if (stack.peek().onBackPressed()) {
                return;
            } else {
                stack.pop();
                return;
            }
        }
    }

    /**
     * 处理back事件
     */
    public void onBack() {
        if (LIFECYCLE) Log.v(TAG, "onBack");
        super.onBackPressed();
    }
}
