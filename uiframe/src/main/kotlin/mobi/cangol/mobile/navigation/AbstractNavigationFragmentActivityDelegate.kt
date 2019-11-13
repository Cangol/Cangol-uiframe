package mobi.cangol.mobile.navigation

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity


abstract class AbstractNavigationFragmentActivityDelegate {

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    abstract fun onCreate(savedInstanceState: Bundle?)
    /**
     * 返回自定义的根布局
     *
     * @return 根布局
     */
    abstract fun getRootView(): ViewGroup

    /**
     * 返回menu布局
     *
     * @return menu布局
     */
    abstract fun getMenuView(): ViewGroup

    /**
     * 返回内容去布局
     *
     * @return content布局
     */
    abstract fun getContentView(): ViewGroup
    /**
     * 获取menu布局的id
     *
     * @return menuFrame的id
     */
    abstract fun getMenuFrameId(): Int
    /**
     * @param v
     */
    abstract fun setContentView(v: View)

    /**
     * 显示menu
     *
     * @param show
     */
    abstract fun showMenu(show: Boolean)
    /**
     * 返回menu是否显示
     *
     * @return 是否显示
     */
    abstract fun isShowMenu(): Boolean
    /**
     * 设置menu有效
     *
     * @param enable
     */
    abstract fun setMenuEnable(enable: Boolean)

    /**
     * 处理onKeyUp事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    abstract fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean
    /**
     * 返回一个BaseNavigationFragmentActivity
     *
     * @return
     */
    abstract fun getActivity(): BaseNavigationFragmentActivity

    /**
     * attachToActivity
     *
     * @param activity
     */
    abstract fun attachToActivity(activity: Activity)

    /**
     * 设置背景颜色
     *
     * @param color
     */
    abstract fun setBackgroundColor(color: Int)

    /**
     * 设置背景
     *
     * @param resId
     */
    abstract fun setBackgroundResource(resId: Int)

    /**
     * 显示或隐藏蒙板
     * @param show
     */
    abstract fun displayMaskView(show: Boolean)

    /**
     * 获取蒙版view
     * @return
     */
    abstract fun getMaskView(): FrameLayout

}