package mobi.cangol.mobile.base

import android.content.Intent
import android.content.res.Configuration
import android.os.*
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.EditText
import mobi.cangol.mobile.CoreApplication
import mobi.cangol.mobile.logging.Log
import mobi.cangol.mobile.service.AppService
import mobi.cangol.mobile.service.session.SessionService
import java.lang.ref.WeakReference


abstract class BaseFragment : Fragment() {
    companion object {
        const val TAG ="BaseFragment"
        const val RESULT_CANCELED = 0
        const val RESULT_OK = -1
        const val REQUEST_CODE_1 = "requestCode!=-1"
        const val ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE = "IllegalStateException  Fragment isEnable=false"
        const val GET_ACTIVITY_IS_NULL = "getActivity is null"
    }

    protected var app: CoreApplication? = null
    private var startTime: Long = 0
    private var resultCode = RESULT_CANCELED
    private var resultData: Bundle? = null
    private var stack: CustomFragmentManager? = null
    private var handler: InternalHandler? = null
    private var handlerThread: HandlerThread? = null

    /**
     * 查找view
     *
     * @param view
     */
    protected abstract fun findViews(view: View)

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    protected abstract fun initViews(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 返回上级导航fragment
     *
     * @return
     */
    open fun getNavigtionUpToFragment(): FragmentInfo? {
        return null
    }

    /**
     * 初始化子fragment管理栈
     *
     * @param containerId
     */
    protected fun initFragmentStack(containerId: Int) {
        if (null == stack)
            stack = CustomFragmentManager.forContainer(this.activity!!, containerId, this.childFragmentManager)
    }

    /**
     * 获取子fragment管理栈
     *
     * @return
     */
    fun getCustomFragmentManager(): CustomFragmentManager? {
        return stack
    }

    /**
     * 返回当前fragment是否是单例的，如果是在当前的自定栈里，他只能存在一个
     *
     * @return
     */
    open fun isSingleton(): Boolean {
        return false
    }

    /**
     * 返回是否清除栈,一级fragment建议设置为true,二三级fragment建议设置为false
     *
     * @return
     */
    open fun isCleanStack(): Boolean {
        return false
    }

    /**
     * 获取AppService
     *
     * @param name
     * @return
     */
    fun getAppService(name: String): AppService {
        return app!!.getAppService(name)
    }

    /**
     * 获取Session
     *
     * @return
     */
    fun getSession(): SessionService {
        return app!!.session
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        Log.v(TAG, "onAttachFragment")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        Log.v(TAG, "onCreateAnimation")
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        super.onMultiWindowModeChanged(isInMultiWindowMode)
        Log.v(TAG, "onMultiWindowModeChanged")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        handlerThread = HandlerThread(TAG)
        handlerThread?.start()
        handler = handlerThread?.looper?.let { InternalHandler(this, it) }
        app = this.activity!!.application as CoreApplication
        if (savedInstanceState != null && null != stack) {
            stack!!.restoreState(savedInstanceState)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.v(TAG, "onCreateView")
        startTime = System.currentTimeMillis()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume " + getIdleTime() + "s")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop")
    }

    override fun onDetach() {
        super.onDetach()
        Log.v(TAG, "onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(TAG, "onDestroyView " + getIdleTime() + "s")
    }

    override fun onDestroy() {
        getHandler().looper.quit()
        handlerThread?.quit()
        stack?.destroy()
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }

    fun onDrawerSlide(slideOffset: Float) {
        Log.v(TAG, "onDrawerSlide $slideOffset")
    }

    fun onDrawerOpened() {
        Log.v(TAG, "onDrawerOpened")
    }

    fun onDrawerClosed() {
        Log.v(TAG, "onDrawerClosed")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stack?.saveState(outState)
        Log.v(TAG, "onSaveInstanceState")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.v(TAG, "onConfigurationChanged")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.v(TAG, "onActivityResult")

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.v(TAG, "onHiddenChanged $hidden")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.v(TAG, "onLowMemory")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.v(TAG, "onViewStateRestored")
    }

    /**
     * fragment之间的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    open fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        Log.v(TAG, "onFragmentResult requestCode=$requestCode,resultCode=$resultCode,data=$data")
    }

    /**
     * 设置回调的状态码
     *
     * @param resultCode
     */
    fun setResult(resultCode: Int) {
        this.setResult(resultCode, null)
    }

    /**
     * 设置回调的状态码和参数
     *
     * @param resultCode
     * @param resultData
     */
    fun setResult(resultCode: Int, resultData: Bundle?) {
        this.resultCode = resultCode
        this.resultData = resultData
    }

    /**
     * 通知返回回调
     */
    fun notifyResult() {
        val target = targetFragment as BaseFragment?
        if (target != null) {
            target.onFragmentResult(targetRequestCode, resultCode, resultData)
        } else {
            throw IllegalStateException("Target Fragment is null")
        }
    }

    /**
     * 返回左上角导航图标的事件处理结果
     *
     * @return
     */
    open fun onSupportNavigateUp(): Boolean {
        hideSoftInput()
        return false
    }

    /**
     * back键相应
     *
     * @return
     */

    open fun onBackPressed(): Boolean {
        if (null == stack) return false
        return if (stack!!.size() <= 1 || stack!!.peek() == null) {
            false
        } else {
            if (stack!!.peek()!!.onBackPressed()) {
                true
            } else {
                stack!!.popBackStack()
                true
            }
        }
    }

    /**
     * 返回view
     *
     * @param id
     * @return
     */
    fun <T : View> findViewById(@IdRes id: Int): T? {
        return if (view == null)
            null
        else
            this.view!!.findViewById(id)
    }

    /**
     * 显示toast
     *
     * @param resId
     */
    fun showToast(resId: Int) {
        if (activity != null) {
            (this.activity as CustomFragmentActivityDelegate).showToast(resId)
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE)
        }
    }

    /**
     * 显示toast
     *
     * @param resId
     * @param duration
     */
    fun showToast(resId: Int, duration: Int) {
        if (activity != null) {
            (this.activity as CustomFragmentActivityDelegate).showToast(resId, duration)
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE)
        }
    }

    /**
     * 显示toast
     *
     * @param str
     */
    fun showToast(str: String) {
        if (activity != null) {
            (this.activity as CustomFragmentActivityDelegate).showToast(str)
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE)
        }
    }

    /**
     * 显示toast
     *
     * @param str
     * @param duration
     */
    fun showToast(str: String, duration: Int) {
        if (activity != null) {
            (this.activity as CustomFragmentActivityDelegate).showToast(str, duration)
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE)
        }
    }

    /**
     * 返回当前fragment是否有效
     *
     * @return
     */
    fun isEnable(): Boolean {
        return !(null == activity || !isAdded || isRemoving || isDetached)
    }

    fun showSoftInput(editText: EditText) {
        if (isEnable()) {
            (this.activity as BaseActivityDelegate).showSoftInput(editText)
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE)
        }
    }

    fun hideSoftInput() {
        if (isEnable()) {
            (this.activity as BaseActivityDelegate).hideSoftInput()
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE)
        }
    }

    fun hideSoftInput(editText: EditText) {
        if (isEnable()) {
            (this.activity as BaseActivityDelegate).hideSoftInput(editText)
        } else {
            Log.e(ILLEGAL_STATE_EXCEPTION_FRAGMENT_IS_ENABLE_FALSE)
        }
    }

    /**
     * 获取相应时间
     *
     * @return
     */

    private fun getIdleTime(): Float {
        return (System.currentTimeMillis() - startTime) / 1000.0f
    }


    /**
     * 获取回调码
     *
     * @return
     */
    protected fun getResultCode(): Int {
        return resultCode
    }

    /**
     * 替换同级fragment
     *
     * @param fragmentClass
     */
    fun replaceFragment(fragmentClass: Class<out BaseFragment>) {
        replaceFragment(fragmentClass, fragmentClass.name, null)
    }

    /**
     * 替换同级fragment
     *
     * @param fragmentClass
     * @param args
     */
    fun replaceFragment(fragmentClass: Class<out BaseFragment>, args: Bundle) {
        replaceFragment(fragmentClass, fragmentClass.name, args)
    }

    /**
     * 替换同级fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    fun replaceFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?) {
        this.replaceFragment(fragmentClass, tag, args, null)
    }

    /**
     * 替换同级fragment,并要求请求回调
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param args
     */
    fun replaceFragmentForResult(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?, requestCode: Int) {
        if (requestCode != -1) {
            this.replaceFragment(fragmentClass, tag, args, CustomFragmentTransaction().setTargetFragment(this, requestCode))
        } else {
            throw IllegalStateException(REQUEST_CODE_1)
        }
    }

    fun replaceFragmentForResult(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?, requestCode: Int, customFragmentTransaction: CustomFragmentTransaction?) {
        if (requestCode != -1) {
            if (customFragmentTransaction != null) {
                this.replaceFragment(fragmentClass, tag, args, customFragmentTransaction.setTargetFragment(this, requestCode))
            } else {
                this.replaceFragment(fragmentClass, tag, args, CustomFragmentTransaction().setTargetFragment(this, requestCode))
            }
        } else {
            throw IllegalStateException(REQUEST_CODE_1)
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
    fun replaceFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?, customFragmentTransaction: CustomFragmentTransaction?) {
        val parent = this.parentFragment as BaseFragment?
        val fragmentManager: CustomFragmentManager?
        if (parent != null) {
            fragmentManager = parent.getCustomFragmentManager()
        } else {
            checkNotNull(activity) { "getActivity is null" }
            fragmentManager = (this.activity as CustomFragmentActivityDelegate).getCustomFragmentManager()
        }
        if (null != fragmentManager && !fragmentManager.isStateSaved()) {
            fragmentManager.replace(fragmentClass, tag, args, customFragmentTransaction)
            fragmentManager.commit()
        } else {
            Log.e(TAG, "Can not perform this action after onSaveInstanceState")
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
    fun isSavedState(): Boolean {
        val parent = this.parentFragment as BaseFragment?
        val fragmentManager: CustomFragmentManager?
        if (parent != null) {
            fragmentManager = parent.getCustomFragmentManager()
        } else {
            if (activity == null) {
                return false
            } else {
                fragmentManager = (this.activity as CustomFragmentActivityDelegate).getCustomFragmentManager()
            }
        }
        return fragmentManager?.isStateSaved() ?: false
    }

    /**
     * 替换父级fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    fun replaceParentFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?) {
        replaceParentFragment(fragmentClass, tag, args, null)
    }

    /**
     * 替换父类级fragment,并要求请求回调
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param requestCode
     */
    fun replaceParentFragmentForResult(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?, requestCode: Int) {
        if (requestCode != -1) {
            this.replaceParentFragment(fragmentClass, tag, args, CustomFragmentTransaction().setTargetFragment(this, requestCode))
        } else {
            throw IllegalStateException(REQUEST_CODE_1)
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
    fun replaceParentFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?, customFragmentTransaction: CustomFragmentTransaction?) {
        val parent = this.parentFragment as BaseFragment?
        if (parent != null) {
            parent.replaceFragment(fragmentClass, tag, args, customFragmentTransaction)
        } else {
            throw IllegalStateException("ParentFragment is null")
        }
    }

    /**
     * 替换子级fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    fun replaceChildFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?) {
        replaceChildFragment(fragmentClass, tag, args, null)
    }

    /**
     * 替换子级fragment 带自定义动画
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param customFragmentTransaction
     */
    fun replaceChildFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?, customFragmentTransaction: CustomFragmentTransaction?) {
        if (stack != null) {
            if (!stack!!.isStateSaved()) {
                stack?.replace(fragmentClass, tag, args, customFragmentTransaction)
                stack?.commit()
            } else {
                Log.e(TAG, "Can not perform this action after onSaveInstanceState")
            }
        } else {
            throw IllegalStateException("fragment'CustomFragmentManager is null, Please initFragmentStack")
        }
    }

    fun popBackStack(tag: String, flag: Int) {
        val parent = this.parentFragment as BaseFragment?
        if (parent != null) {
            parent.getCustomFragmentManager()?.popBackStack(tag, flag)
        } else {
            checkNotNull(activity) { "getActivity is null" }
            (this.activity as CustomFragmentActivityDelegate).getCustomFragmentManager()?.popBackStack(tag, flag)
        }
    }

    fun popBackStackImmediate(tag: String, flag: Int) {
        val parent = this.parentFragment as BaseFragment?
        if (parent != null) {
            parent.getCustomFragmentManager()?.popBackStackImmediate(tag, flag)
        } else {
            checkNotNull(activity) { "getActivity is null" }
            (this.activity as CustomFragmentActivityDelegate).getCustomFragmentManager()?.popBackStackImmediate(tag, flag)
        }
    }

    /**
     * 立即将tag的fragment弹出栈
     */
    fun popBackStack() {
        val parent = this.parentFragment as BaseFragment?
        if (parent != null) {
            parent.getCustomFragmentManager()?.popBackStack()
        } else {
            checkNotNull(activity) { "getActivity is null" }
            (this.activity as CustomFragmentActivityDelegate).getCustomFragmentManager()?.popBackStack()
        }
    }

    /**
     * 立即将当前fragment弹出栈
     */
    fun popBackStackImmediate() {
        val parent = this.parentFragment as BaseFragment?
        if (parent != null) {
            parent.getCustomFragmentManager()?.popBackStackImmediate()
        } else {
            checkNotNull(activity) { "getActivity is null" }
            (this.activity as CustomFragmentActivityDelegate).getCustomFragmentManager()?.popBackStackImmediate()
        }
    }

    /**
     * 将所有fragment弹出栈
     */
    fun popBackStackAll() {
        val parent = this.parentFragment as BaseFragment?
        if (parent != null) {
            parent.getCustomFragmentManager()?.popBackStackAll()
        } else {
            checkNotNull(activity) { "getActivity is null" }
            (this.activity as CustomFragmentActivityDelegate).getCustomFragmentManager()?.popBackStackAll()
        }
    }

    protected fun getHandler(): Handler {
        return handler!!
    }

    protected fun handleMessage(msg: Message) {
        //do somethings
    }

    protected fun postRunnable(runnable: StaticInnerRunnable?) {
        if (runnable != null)
            handler?.post(runnable)
    }

    protected fun postRunnable(runnable: Runnable?) {
        if (runnable != null)
            handler?.post(runnable)
    }

    open fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return if (null == stack || stack?.size() == 0 || stack?.peek() == null) {
            false
        } else {
            stack?.peek()!!.onKeyUp(keyCode, event)
        }
    }

    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (null == stack || stack?.size() == 0 || stack?.peek() == null) {
            false
        } else {
            stack?.peek()!!.onKeyDown(keyCode, event)
        }
    }

    protected class StaticInnerRunnable : Runnable {
        override fun run() {
            //do somethings
        }
    }

    protected class InternalHandler(fragment: BaseFragment, looper: Looper) : Handler(looper) {
        private val mFragmentRef: WeakReference<BaseFragment> = WeakReference(fragment)

        override fun handleMessage(msg: Message) {
            val fragment = mFragmentRef.get()
            if (fragment != null && fragment.isEnable()) {
                fragment.handleMessage(msg)
            }
        }
    }

    @ColorInt
    fun getThemeAttrColor(@AttrRes colorAttr: Int): Int {
        checkNotNull(activity) { "getActivity is null" }
        val array = activity!!.obtainStyledAttributes(null, intArrayOf(colorAttr))
        try {
            return array.getColor(0, 0)
        } finally {
            array.recycle()
        }
    }

    fun getAttrTypedValue(@AttrRes attr: Int): TypedValue {
        checkNotNull(activity) { "getActivity is null" }
        val typedValue = TypedValue()
        activity!!.theme.resolveAttribute(attr, typedValue, true)
        return typedValue
    }
}
