package mobi.cangol.mobile.base

import android.content.Context
import android.content.res.Configuration
import android.os.*
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.v4.app.FragmentActivity
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import mobi.cangol.mobile.CoreApplication
import mobi.cangol.mobile.logging.Log
import mobi.cangol.mobile.service.AppService
import mobi.cangol.mobile.service.session.SessionService
import java.lang.ref.WeakReference

abstract class BaseFragmentActivity : FragmentActivity(), BaseActivityDelegate, CustomFragmentActivityDelegate {
    companion object {
        const val TAG = "BaseFragmentActivity"
    }
    protected lateinit var app: CoreApplication
    private var stack: CustomFragmentManager? = null
    private var startTime: Long = 0
    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null

    private fun getIdleTime(): Float {
        return (System.currentTimeMillis() - startTime) / 1000.0f
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        startTime = System.currentTimeMillis()
        handlerThread = HandlerThread(TAG)
        handlerThread!!.start()
        handler = InternalHandler(this, handlerThread!!.looper)
        app = this.application as CoreApplication
        app.addActivityToManager(this)
    }

    override fun showToast(resId: Int) {
        if (!isFinishing) Toast.makeText(this.applicationContext, resId, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(str: String) {
        if (!isFinishing) Toast.makeText(this.applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(resId: Int, duration: Int) {
        if (!isFinishing) Toast.makeText(this.applicationContext, resId, duration).show()
    }

    override fun showToast(str: String, duration: Int) {
        if (!isFinishing) Toast.makeText(this.applicationContext, str, duration).show()
    }

    /**
     * 初始自定义栈管理器
     *
     * @param containerId
     */
    override fun initFragmentStack(containerId: Int) {
        if (null == stack)
            stack = CustomFragmentManager.forContainer(this, containerId, this.supportFragmentManager)
    }

    /**
     * 替换fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    override fun replaceFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?) {
        checkNotNull(stack) { "stack is null" }
        if (!stack!!.isStateSaved()) {
            stack!!.replace(fragmentClass, tag, args)
            stack!!.commit()
        } else {
            Log.e(TAG, "Can not perform this action after onSaveInstanceState")
        }
    }

    override fun getCustomFragmentManager(): CustomFragmentManager? {
        return stack
    }

    /**
     * 获取AppService
     *
     * @param name
     * @return
     */
    override fun getAppService(name: String): AppService {
        return app.getAppService(name)
    }

    /**
     * 获取Session
     *
     * @return
     */
    override fun getSession(): SessionService {
        return app.session
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume " + getIdleTime() + "s")
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "onRestart")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop")
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        if (null != stack) stack!!.destroy()
        app.delActivityFromManager(this)
        handlerThread!!.quit()
        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (null != stack) stack!!.restoreState(savedInstanceState)
        Log.v(TAG, "onRestoreInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (null != stack) stack!!.saveState(outState)
        Log.v(TAG, "onSaveInstanceState")
    }

    override fun getLastCustomNonConfigurationInstance(): Any {
        Log.v(TAG, "getLastCustomNonConfigurationInstance")
        return super.getLastCustomNonConfigurationInstance()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        Log.v(TAG, "onRetainCustomNonConfigurationInstance")
        return super.onRetainCustomNonConfigurationInstance()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.v(TAG, "onConfigurationChanged")
    }

    /**
     * 设置全屏
     *
     * @param fullscreen
     */
    override fun setFullScreen(fullscreen: Boolean) {
        if (fullscreen) {
            this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

    }

    override fun isFullScreen(): Boolean {
        val flag = this.window.attributes.flags
        return flag and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
    }

    override fun onBackPressed() {
        Log.v(TAG, "onBackPressed ")
        if (null == stack || stack!!.size() == 0 || stack!!.peek() == null) {
            onBack()
        } else {
            if (!stack!!.peek()!!.onBackPressed()) {
                if (stack!!.size() == 1) {
                    onBack()
                } else {
                    stack!!.popBackStack()
                }
            }
        }
    }

    /**
     * 处理back事件
     */
    override fun onBack() {
        Log.v(TAG, "onBack")
        super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return if (null == stack || stack!!.size() == 0 || stack!!.peek() == null) {
            super.onKeyUp(keyCode, event)
        } else {
            if (stack!!.peek()!!.onKeyUp(keyCode, event)) {
                true
            } else {
                super.onKeyUp(keyCode, event)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (null == stack || stack!!.size() == 0 || stack!!.peek() == null) {
            super.onKeyDown(keyCode, event)
        } else {
            if (stack!!.peek()!!.onKeyDown(keyCode, event)) {
                true
            } else {
                super.onKeyDown(keyCode, event)
            }
        }
    }

    override fun showSoftInput(editText: EditText) {
        editText.requestFocus()
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
        editText.text = null
    }

    override fun hideSoftInput() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (this.currentFocus != null) {
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun hideSoftInput(editText: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun getHandler(): Handler {
        return handler!!
    }

    protected fun postRunnable(runnable: StaticInnerRunnable?) {
        if (handler != null && runnable != null)
            handler!!.post(runnable)
    }

    protected fun handleMessage(msg: Message) {
        //do somethings
    }

    protected class StaticInnerRunnable : Runnable {
        override fun run() {
            //do somethings
        }
    }

    internal class InternalHandler(context: Context, looper: Looper) : Handler(looper) {
        private val mContext: WeakReference<Context> = WeakReference(context)

        override fun handleMessage(msg: Message) {
            val context = mContext.get()
            if (context != null) {
                (context as BaseFragmentActivity).handleMessage(msg)
            }
        }
    }

    @ColorInt
    override fun getThemeAttrColor(@AttrRes colorAttr: Int): Int {
        val array = this.obtainStyledAttributes(null, intArrayOf(colorAttr))
        try {
            return array.getColor(0, 0)
        } finally {
            array.recycle()
        }
    }
}