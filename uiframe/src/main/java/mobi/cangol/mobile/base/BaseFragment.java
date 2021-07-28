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
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.session.Session;
import mobi.cangol.mobile.service.session.SessionService;

public abstract class BaseFragment extends Fragment {
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    public static final String REQUEST_CODE_1 = "requestCode!=-1";
    public static final String ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE = "IllegalStateException  Fragment isEnable=false";
    protected final String TAG = Log.makeLogTag(this.getClass());
    protected CoreApplication app;
    private long startTime;
    private int resultCode = RESULT_CANCELED;
    private Bundle resultData;
    private CustomFragmentManager stack;
    protected HandlerThread handlerThread;
    private Handler threadHandler;
    private Handler uiHandler;
    /**
     * 查找view
     *
     * @param view
     */
    protected abstract  void findViews(View view);

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    protected abstract  void initViews(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract  void initData(Bundle savedInstanceState);


    public void onNewBundle(Bundle bundle) {
        Log.v(TAG, "onNewBundle");
    }

    /**
     * 返回上级导航fragment
     *
     * @return
     */
    protected FragmentInfo getNavigtionUpToFragment(){
        return null;
    }

    /**
     * 初始化子fragment管理栈
     *
     * @param containerId
     */
    protected void initFragmentStack(int containerId) {
        if (null == stack)
            stack = CustomFragmentManager.forContainer(this.getActivity(), containerId, this.getChildFragmentManager());
    }

    /**
     * 获取子fragment管理栈
     *
     * @return
     */
    public final  CustomFragmentManager getCustomFragmentManager() {
        return stack;
    }

    /**
     * 返回当前fragment是否是单例的，如果是在当前的自定栈里，他只能存在一个
     *
     * @return
     */
    public boolean isSingleton() {
        return false;
    }

    /**
     * 返回是否清除栈,一级fragment建议设置为true,二三级fragment建议设置为false
     *
     * @return
     */
    public boolean isCleanStack() {
        return false;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(TAG, "onAttach");
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.v(TAG, "onAttachFragment");
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Log.v(TAG, "onCreateAnimation");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        Log.v(TAG, "onMultiWindowModeChanged");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        threadHandler = new InternalHandler(this,handlerThread.getLooper());
        uiHandler= new InternalHandler(this,Looper.getMainLooper());
        app = (CoreApplication) this.getActivity().getApplication();
        if (savedInstanceState != null&&null != stack) {
           stack.restoreState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        startTime = System.currentTimeMillis();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume " + getIdleTime() + "s");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "onDestroyView " + getIdleTime() + "s");
    }

    @Override
    public void onDestroy() {
        handlerThread.quit();
        if (null != stack)stack.destroy();
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    public void onDrawerSlide(float slideOffset) {
        Log.v(TAG, "onDrawerSlide "+slideOffset);
    }

    public void onDrawerOpened() {
        Log.v(TAG, "onDrawerOpened");
    }

    public void onDrawerClosed() {
        Log.v(TAG, "onDrawerClosed");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != stack) stack.saveState(outState);
        Log.v(TAG, "onSaveInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v(TAG, "onConfigurationChanged");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "onActivityResult");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.v(TAG, "onHiddenChanged " + hidden);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.v(TAG, "onLowMemory");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(TAG, "onViewStateRestored");
    }

    /**
     * fragment之间的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        Log.v(TAG, "onFragmentResult requestCode="+requestCode+",resultCode="+resultCode+",data="+data);
    }

    /**
     * 设置回调的状态码
     *
     * @param resultCode
     */
    public final  void setResult(int resultCode) {
        this.setResult(resultCode, null);
    }

    /**
     * 设置回调的状态码和参数
     *
     * @param resultCode
     * @param resultData
     */
    public final  void setResult(int resultCode, Bundle resultData) {
        this.resultCode = resultCode;
        this.resultData = resultData;
    }

    /**
     * 通知返回回调
     */
    public final  void notifyResult() {
        BaseFragment target = (BaseFragment) getTargetFragment();
        if (target != null) {
            target.onFragmentResult(getTargetRequestCode(), resultCode, resultData);
        } else {
            throw new IllegalStateException("Target Fragment is null");
        }
    }

    /**
     * 返回左上角导航图标的事件处理结果
     *
     * @return
     */
    public boolean onSupportNavigateUp() {
        hideSoftInput();
        return false;
    }

    /**
     * back键相应
     *
     * @return
     */

    public boolean onBackPressed() {

        if (null == stack) return false;
        if (stack.size() <= 1||stack.peek()==null) {
            return false;
        } else {
            if (stack.peek().onBackPressed()) {
                return true;
            } else {
                stack.popBackStack();
                return true;
            }
        }
    }

    /**
     * 返回view
     *
     * @param id
     * @return
     */
    public final  <T extends View> T findViewById(@IdRes int id) {
        if(getView()==null)
            return null;
        else
            return this.getView().findViewById(id);
    }

    /**
     * 显示toast
     *
     * @param resId
     */
    public void showToast(int resId) {
        if (getActivity()!=null) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.showToast(resId);
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE);
        }
    }

    /**
     * 显示toast
     *
     * @param resId
     * @param duration
     */
    public void showToast(int resId, int duration) {
        if (getActivity()!=null) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.showToast(resId, duration);
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE);
        }
    }

    /**
     * 显示toast
     *
     * @param str
     */
    public void showToast(String str) {
        if (getActivity()!=null) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.showToast(str);
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE);
        }
    }

    /**
     * 显示toast
     *
     * @param str
     * @param duration
     */
    public void showToast(String str, int duration) {
        if (getActivity()!=null) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.showToast(str,duration);
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE);
        }
    }

    /**
     * 返回当前fragment是否有效
     *
     * @return
     */
    public boolean isEnable() {
        return  !(null == getActivity() || !isAdded() || isRemoving() || isDetached());
    }

    public void showSoftInput(EditText editText) {
        if (isEnable()) {
            BaseActivityDelegate bfActivity = (BaseActivityDelegate) this.getActivity();
            bfActivity.showSoftInput(editText);
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE);
        }
    }

    public void hideSoftInput() {
        if (isEnable()) {
            BaseActivityDelegate bfActivity = (BaseActivityDelegate) this.getActivity();
            bfActivity.hideSoftInput();
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE);
        }
    }

    public void hideSoftInput(EditText editText) {
        if (isEnable()) {
            BaseActivityDelegate bfActivity = (BaseActivityDelegate) this.getActivity();
            bfActivity.hideSoftInput(editText);
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE);
        }
    }
    /**
     * 获取相应时间
     *
     * @return
     */

    public float getIdleTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    /**
     * 获取回调码
     *
     * @return
     */
    protected int getResultCode() {
        return resultCode;
    }

    /**
     * 替换同级fragment
     *
     * @param fragmentClass
     */
    public final  void replaceFragment(Class<? extends BaseFragment> fragmentClass) {
        replaceFragment(fragmentClass, fragmentClass.getName(), null);
    }

    /**
     * 替换同级fragment
     *
     * @param fragmentClass
     * @param args
     */
    public final  void replaceFragment(Class<? extends BaseFragment> fragmentClass, Bundle args) {
        replaceFragment(fragmentClass, fragmentClass.getName(), args);
    }

    /**
     * 替换同级fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    public final  void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        this.replaceFragment(fragmentClass, tag, args, null);
    }

    /**
     * 替换同级fragment,并要求请求回调
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param args
     */
    public final  void replaceFragmentForResult(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, int requestCode) {
        if (requestCode != -1) {
            this.replaceFragment(fragmentClass, tag, args, new CustomFragmentTransaction().setTargetFragment(this, requestCode));
        } else {
            throw new IllegalStateException(REQUEST_CODE_1);
        }
    }
    public final  void replaceFragmentForResult(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, int requestCode,CustomFragmentTransaction customFragmentTransaction) {
        if (requestCode != -1) {
            if(customFragmentTransaction!=null){
                this.replaceFragment(fragmentClass, tag, args, customFragmentTransaction.setTargetFragment(this, requestCode));
            }else{
                this.replaceFragment(fragmentClass, tag, args, new CustomFragmentTransaction().setTargetFragment(this, requestCode));
            }
        } else {
            throw new IllegalStateException(REQUEST_CODE_1);
        }
    }
    /**
     * 替换同级fragment 带自定义动画
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param customFragmentTransaction
     */
    public final  void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, CustomFragmentTransaction customFragmentTransaction) {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        CustomFragmentManager fragmentManager;
        if (parent != null) {
            fragmentManager = parent.getCustomFragmentManager();
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                fragmentManager = bfActivity.getCustomFragmentManager();
            }
        }
        if (null != fragmentManager && !fragmentManager.isStateSaved()) {
            fragmentManager.replace(fragmentClass, tag, args, customFragmentTransaction);
            fragmentManager.commit();
        } else {
            Log.e(TAG, "Can not perform this action after onSaveInstanceState");
        }
    }

    /**
     * 判断是否执行了onSaveInstanceState
     * Support 26.0.0-alpha1 之后才有isStateSaved方法
     * 这里用反射直接读取mStateSaved字段，兼容旧的版本
     * 也为了与之后的版本兼容修改了方法名
     *
     * @return
     */
    public boolean isSavedState() {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        CustomFragmentManager fragmentManager;
        if (parent != null) {
            fragmentManager = parent.getCustomFragmentManager();
        } else {
            if (getActivity() == null) {
                return false;
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                fragmentManager = bfActivity.getCustomFragmentManager();
            }
        }
        if (fragmentManager != null) {
            return fragmentManager.isStateSaved();
        } else {
            return false;
        }
    }

    /**
     * 替换父级fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    public final  void replaceParentFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        replaceParentFragment(fragmentClass, tag, args, null);
    }

    /**
     * 替换父类级fragment,并要求请求回调
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param requestCode
     */
    public final  void replaceParentFragmentForResult(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, int requestCode) {
        if (requestCode != -1) {
            this.replaceParentFragment(fragmentClass, tag, args, new CustomFragmentTransaction().setTargetFragment(this, requestCode));
        } else {
            throw new IllegalStateException(REQUEST_CODE_1);
        }
    }

    /**
     * 替换父级fragment 带自定义动画
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param customFragmentTransaction
     */
    public final  void replaceParentFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, CustomFragmentTransaction customFragmentTransaction) {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.replaceFragment(fragmentClass, tag, args, customFragmentTransaction);
        } else {
            throw new IllegalStateException("ParentFragment is null");
        }
    }

    /**
     * 替换子级fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    public final  void replaceChildFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        replaceChildFragment(fragmentClass, tag, args, null);
    }

    /**
     * 替换子级fragment 带自定义动画
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param customFragmentTransaction
     */
    public final  void replaceChildFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, CustomFragmentTransaction customFragmentTransaction) {
        if (stack != null) {
            if (!stack.isStateSaved()) {
                stack.replace(fragmentClass, tag, args, customFragmentTransaction);
                stack.commit();
            } else {
                Log.e(TAG, "Can not perform this action after onSaveInstanceState");
            }
        } else {
            throw new IllegalStateException("fragment'CustomFragmentManager is null, Please initFragmentStack");
        }
    }

    public final  void popBackStack(String tag,int flag) {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.getCustomFragmentManager().popBackStack(tag,flag);
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                bfActivity.getCustomFragmentManager().popBackStack(tag,flag);
            }
        }
    }
    public final  void popBackStackImmediate(String tag,int flag) {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.getCustomFragmentManager().popBackStackImmediate(tag,flag);
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                bfActivity.getCustomFragmentManager().popBackStackImmediate(tag,flag);
            }
        }
    }
    /**
     * 立即将tag的fragment弹出栈
     */
    public final  void popBackStack() {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.getCustomFragmentManager().popBackStack();
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                bfActivity.getCustomFragmentManager().popBackStack();
            }
        }
    }
    /**
     * 立即将当前fragment弹出栈
     */
    public final  void popBackStackImmediate() {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.getCustomFragmentManager().popBackStackImmediate();
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                bfActivity.getCustomFragmentManager().popBackStackImmediate();
            }
        }
    }
    /**
     * 将所有fragment弹出栈
     */
    public final  void popBackStackAll() {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.getCustomFragmentManager().popBackStackAll();
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                bfActivity.getCustomFragmentManager().popBackStackAll();
            }
        }
    }

    protected Handler getUiHandler() {
        return uiHandler;
    }
    protected Handler getThreadHandler() {
        return threadHandler;
    }

    protected void handleMessage(Message msg) {
        //do somethings
    }

    protected void postRunnable(StaticInnerRunnable runnable) {
        if (threadHandler != null && runnable != null)
            threadHandler.post(runnable);
    }

    protected void postRunnable(Runnable runnable) {
        if (threadHandler != null && runnable != null)
            threadHandler.post(runnable);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (null == stack||stack.size()==0||stack.peek()==null) {
            return false;
        }else {
            return stack.peek().onKeyUp(keyCode, event);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null == stack||stack.size()==0||stack.peek()==null) {
            return false;
        }else {
            return stack.peek().onKeyDown(keyCode, event);
        }
    }

    protected static class StaticInnerRunnable implements Runnable {
        @Override
        public void run() {
            //do somethings
        }
    }

    protected static final  class InternalHandler extends Handler {
        private final WeakReference<BaseFragment> mFragmentRef;

        public InternalHandler(BaseFragment fragment, Looper looper) {
            super(looper);
            mFragmentRef = new WeakReference<>(fragment);
        }

        public void handleMessage(Message msg) {
            BaseFragment fragment = mFragmentRef.get();
            if (fragment != null && fragment.isEnable()) {
                fragment.handleMessage(msg);
            }
        }
    }

    @ColorInt
    public  int getThemeAttrColor(@AttrRes int colorAttr) {
        if(getActivity()==null){
            throw new IllegalStateException("getActivity is null");
        }else{
            TypedArray array = getActivity().obtainStyledAttributes(null, new int[]{colorAttr});
            try {
                return array.getColor(0, 0);
            } finally {
                array.recycle();
            }
        }
    }

    public TypedValue getAttrTypedValue(@AttrRes int attr){
        if(getActivity()==null){
            throw new IllegalStateException("getActivity is null");
        }else{
            TypedValue typedValue = new TypedValue();
            getActivity().getTheme().resolveAttribute(attr, typedValue, true);
            return typedValue;
        }
    }
}