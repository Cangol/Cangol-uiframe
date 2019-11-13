package mobi.cangol.mobile.base

import android.os.Bundle


interface CustomFragmentActivityDelegate {

    /**
     * 获取自定栈管理器
     *
     * @return
     */
     fun getCustomFragmentManager(): CustomFragmentManager?
    /**
     * 初始化自定义栈管理器
     *
     * @param containerId
     */
    fun initFragmentStack(containerId: Int)

    /**
     * fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    fun replaceFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?)


    fun showToast(resId: Int)

    fun showToast(str: String)

    fun showToast(resId: Int, duration: Int)

    fun showToast(str: String, duration: Int)

}