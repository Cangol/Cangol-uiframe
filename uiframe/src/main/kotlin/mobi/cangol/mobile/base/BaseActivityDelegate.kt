package mobi.cangol.mobile.base

import android.os.Bundle
import android.os.Handler
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.widget.EditText
import mobi.cangol.mobile.service.AppService
import mobi.cangol.mobile.service.session.SessionService

interface BaseActivityDelegate {

    /**
     * 获取Session
     *
     * @return
     */
    fun getSession(): SessionService

    /**
     * 设置全屏
     *
     * @param fullscreen
     */
    fun setFullScreen(fullscreen: Boolean)

    /**
     * 是否全屏
     *
     */
    fun isFullScreen(): Boolean

    /**
     * 获取一个主线程的Handler
     */
    fun getHandler(): Handler

    /**
     * 查找view
     */
    fun findViews()

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */

    fun initViews(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 显示toast
     *
     * @param resId
     */
    fun showToast(resId: Int)

    /**
     * 显示toast
     *
     * @param str
     */
    fun showToast(str: String)

    /**
     * 显示toast
     *
     * @param resId
     * @param duration
     */
    fun showToast(resId: Int, duration: Int)

    /**
     * 显示toast
     *
     * @param str
     * @param duration
     */
    fun showToast(str: String, duration: Int)

    /**
     * 获取AppService
     *
     * @param name
     * @return
     */
    fun getAppService(name: String): AppService

    /**
     * back按钮回调
     */
    fun onBack()


    /**
     * 显示软键盘
     *
     * @param editText
     */
    fun showSoftInput(editText: EditText)

    /**
     * 隐藏软键盘
     *
     */
    fun hideSoftInput()

    /**
     * 隐藏软键盘
     *
     */
    fun hideSoftInput(editText: EditText)


    @ColorInt
    fun getThemeAttrColor(@AttrRes colorAttr: Int): Int

}
