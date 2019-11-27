package mobi.cangol.mobile.base

import android.os.Bundle
import android.view.View
import mobi.cangol.mobile.actionbar.*
import mobi.cangol.mobile.logging.Log


abstract class BaseContentFragment : BaseFragment() {
    private var title: CharSequence? = null

    /**
     * 获取ActionBarActivity,由于原getActivity为final，故增加此方法
     *
     * @return
     */
    fun getActionBarActivity(): ActionBarActivity {
        return activity as ActionBarActivity
    }

    /**
     * 获取自定义actionbar
     *
     * @return
     */
    fun getCustomActionBar(): ActionBar {
        val abActivity = this.activity as ActionBarActivity?
        return if (abActivity == null) {
            throw IllegalStateException(GET_ACTIVITY_IS_NULL)
        } else {
            abActivity.getCustomActionBar()
        }
    }

    /**
     * 获取标题
     *
     * @return
     */
    fun getTitle(): CharSequence? {
        return title
    }


    /**
     * 设置标题
     *
     * @param title
     */
    fun setTitle(title: String) {
        if (this.parentFragment != null) return
        this.title = title
        getCustomActionBar().setTitle(title)
    }

    /**
     * 设置标题
     *
     * @param title
     */
    fun setTitle(title: Int) {
        if (this.parentFragment != null) return
        this.title = getString(title)
        getCustomActionBar().setTitle(title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.setTitle("")
    }


    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    fun setStatusBarColor(color: Int) {
        checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
        (this.activity as ActionBarActivity).setStatusBarTintColor(color)
    }

    /**
     * 设置导航栏颜色
     *
     * @param color
     */
    fun setNavigationBarTintColor(color: Int) {
        checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
        (this.activity as ActionBarActivity).setNavigationBarTintColor(color)
    }


    /**
     * 开始progress模式
     */
    fun enableRefresh(enable: Boolean) {
        checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
        (this.activity as ActionBarActivity).getCustomActionBar().enableRefresh(enable)
    }

    /**
     * 停止progress模式
     */
    fun refreshing(refreshing: Boolean) {
        checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
        (this.activity as ActionBarActivity).getCustomActionBar().refreshing(refreshing)
    }


    /**
     * 开启自定义actionbar模式
     *
     * @param callback
     * @return
     */
    fun startCustomActionMode(callback: ActionMode.Callback): ActionMode {
        val abActivity = this.activity as ActionBarActivity?
        return if (abActivity == null) {
            throw IllegalStateException(GET_ACTIVITY_IS_NULL)
        } else {
            abActivity.startCustomActionMode(callback)
        }
    }

    /**
     * 设置menu的显示（在super.onViewCreated后调用才有效）
     *
     * @param enable
     */
    protected fun setMenuEnable(enable: Boolean) {
        val parent = this.parentFragment as BaseContentFragment?
        if (parent == null) {
            checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
            if (this.parentFragment == null) {
                if (this.activity is BaseNavigationFragmentActivity) {
                    (this.activity as BaseNavigationFragmentActivity).setMenuEnable(enable)
                } else {
                    Log.e("getActivity is not BaseNavigationFragmentActivity ")
                }
            }
        }
    }

    private fun notifyMenuChange(moduleId: Int) {
        checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
        if (this.activity is BaseNavigationFragmentActivity) {
            (this.activity as BaseNavigationFragmentActivity).setCurrentModuleId(moduleId)
        }
    }

    private fun setActionBarUpIndicator() {
        val parent = this.parentFragment as BaseContentFragment?
        if (parent == null) {
            checkNotNull(activity) { GET_ACTIVITY_IS_NULL }
            if (isCleanStack()) {
                (this.activity as ActionBarActivity).getCustomActionBar().displayHomeIndicator()
            } else {
                (this.activity as ActionBarActivity).getCustomActionBar().displayUpIndicator()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setMenuVisibility(true)
        if (savedInstanceState != null) {
            title = savedInstanceState.getCharSequence("title")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("title", title)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this.parentFragment == null) {
            getCustomActionBar().clearActionMenus()
        }
        setActionBarUpIndicator()
        if (this.activity is BaseNavigationFragmentActivity) {
            setMenuEnable(isCleanStack())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkNotNull(this.activity) { GET_ACTIVITY_IS_NULL }
        this.onMenuActionCreated((this.activity as ActionBarActivity).getCustomActionBar().getActionMenu())
    }

    /**
     * actionbar menu创建方法
     *
     * @param actionMenu
     * @return
     */
    open fun onMenuActionCreated(actionMenu: ActionMenu): Boolean {

        return false
    }

    /**
     * actionbar menu选择时间相应方法
     *
     * @param action
     * @return
     */
    open fun onMenuActionSelected(action: ActionMenuItem): Boolean {
        if (this.childFragmentManager.fragments.isNotEmpty()) {
            val size = childFragmentManager.fragments.size
            for (i in size - 1 downTo 0) {
                val fragment = childFragmentManager.fragments[i]
                if (fragment is BaseContentFragment) {
                    if (fragment.isEnable() && fragment.isVisible()) {
                        return fragment.onMenuActionSelected(action)
                    }
                }
            }
        }
        return false
    }

    /**
     * 设置顶级content fragment
     *
     * @param fragmentClass
     * @param args
     */
    fun setContentFragment(fragmentClass: Class<out BaseContentFragment>, args: Bundle?) {
        this.setContentFragment(fragmentClass, fragmentClass.name, args)
    }

    /**
     * 设置顶级content fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    fun setContentFragment(fragmentClass: Class<out BaseContentFragment>, tag: String, args: Bundle?) {
        if (this.activity is CustomFragmentActivityDelegate) {
            (this.activity as CustomFragmentActivityDelegate).replaceFragment(fragmentClass, tag, args)
        } else {
            replaceFragment(fragmentClass, tag, args)
        }
    }

    /**
     * 设置顶级content fragment,并更通知menuFragment更新变更了模块
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param moduleId
     */
    fun setContentFragment(fragmentClass: Class<out BaseContentFragment>, tag: String, args: Bundle?, moduleId: Int) {
        this.setContentFragment(fragmentClass, tag, args)
        notifyMenuChange(moduleId)
    }

    /**
     * 获取父类 BaseContentFragment
     * @return
     */
    fun getParentContentFragment(): BaseContentFragment? {
        return parentFragment as BaseContentFragment
    }

    companion object {
        const val GET_ACTIVITY_IS_NULL = "getActivity is null"
    }
}
