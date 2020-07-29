package mobi.cangol.mobile.base;

import android.os.Bundle;

public interface CustomFragmentActivityDelegate {

    /**
     * 初始化自定义栈管理器
     *
     * @param containerId
     */
    void initFragmentStack(int containerId);

    /**
     * fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args);
    /**
     * fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param moduleId
     */
    void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args,int moduleId);
    /**
     * 获取自定栈管理器
     *
     * @return
     */
    CustomFragmentManager getCustomFragmentManager();


    void showToast(int resId);

    void showToast(String str);

    void showToast(int resId, int duration);

    void showToast(String str, int duration);

}
