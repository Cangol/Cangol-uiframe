package mobi.cangol.mobile.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.*
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import mobi.cangol.mobile.CoreApplication
import mobi.cangol.mobile.logging.Log
import mobi.cangol.mobile.service.AppService
import mobi.cangol.mobile.service.session.SessionService
import java.lang.ref.WeakReference

/**
 * @author Cangol
 */
abstract class BaseActivity:Activity(), BaseActivityDelegate {
    var TAG:String = Log.makeLogTag(this.javaClass)
    private var app: CoreApplication? = null
    private var startTime: Long = 0
    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null

    private fun getIdleTime(): Float {
        return (System.currentTimeMillis() - startTime) / 1000.0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        Log.setLogTag(this)
        Log.v(TAG, "onCreate")
        startTime = System.currentTimeMillis()
        handlerThread = HandlerThread(TAG)
        handlerThread!!.start()
        handler = InternalHandler(this, handlerThread!!.looper)
        app = this.application as CoreApplication
        app!!.addActivityToManager(this)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.v(TAG, "onNewIntent")
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
        Log.v(TAG, "onPause")
        super.onPause()
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "onRestart")
    }

    override fun onStop() {
        Log.v(TAG, "onStop")
        super.onStop()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.v(TAG, "onRestoreInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.v(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun getSession(): SessionService {
        return app!!.session
    }

    override fun getAppService(name: String): AppService {
        return app!!.getAppService(name)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.v(TAG, "onConfigurationChanged")
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        app!!.delActivityFromManager(this)
        handlerThread!!.quit()
        super.onDestroy()
    }

    override fun onBackPressed() {
        Log.v(TAG, "onBackPressed")
        super.onBackPressed()
    }

    override fun setFullScreen(fullscreen: Boolean) {
        if (fullscreen) {
            this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    ,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
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

    /**
     * 处理back事件
     */
    override fun onBack() {
        Log.v(TAG, "onBack")
        super.onBackPressed()
    }

    override fun getHandler(): Handler {
        return handler!!
    }

    protected fun postRunnable(runnable: StaticInnerRunnable?) {
        if (runnable != null)
            handler?.post(runnable)
    }

    protected fun handleMessage(msg: Message) {
        // do somethings
    }

    protected class StaticInnerRunnable : Runnable {
        override fun run() {
            // do somethings
        }
    }

    internal class InternalHandler(context: Context, looper: Looper) : Handler(looper) {
        private val mContext: WeakReference<Context> = WeakReference(context)

        override fun handleMessage(msg: Message) {
            val context = mContext.get()
            if (context != null) {
                (context as BaseActivity).handleMessage(msg)
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