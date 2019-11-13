package mobi.cangol.mobile.base

import android.os.Bundle

abstract class BaseMenuFragment : BaseFragment() {
    private var currentModuleId: Int = 0
    /**
     * 获取当前模块Id
     *
     * @return
     */
    fun getCurrentModuleId():Int{
        return currentModuleId
    }

    /**
     * 设置当前模块
     *
     * @param currentModuleId
     */
    fun setCurrentModuleId(currentModuleId: Int) {
        this.currentModuleId = currentModuleId
        if (this.isEnable())
            onContentChange(currentModuleId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取模块id
        if (savedInstanceState != null)
            currentModuleId = savedInstanceState.getInt("currentModuleId")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //存储模块id
        outState.putInt("currentModuleId", this.currentModuleId)
    }

    /**
     * 设置内容区域fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    fun setContentFragment(fragmentClass: Class<out BaseContentFragment>, tag: String, args: Bundle?) {
        checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
        (this.activity as BaseNavigationFragmentActivity).setContentFragment(fragmentClass, tag, args)
    }

    /**
     * 设置内容区域fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param moduleId      模块ID
     */
    fun setContentFragment(fragmentClass: Class<out BaseContentFragment>, tag: String, args: Bundle?, moduleId: Int) {
        checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
        (this.activity as BaseNavigationFragmentActivity).setContentFragment(fragmentClass, tag, args, moduleId)
    }

    /**
     * 显示或关闭 菜单
     *
     * @param show
     */
    fun showMenu(show: Boolean) {
        checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
        (this.activity as BaseNavigationFragmentActivity).showMenu(show)
    }

    protected abstract fun onContentChange(moduleId: Int)

    /**
     * 菜单打开时调用
     */
    abstract fun onOpen()

    /**
     * 菜单关闭时调用
     */
    abstract fun onClosed()

    companion object {
        const val GET_ACTIVITY_IS_NULL = "getActivity is null"
    }

}