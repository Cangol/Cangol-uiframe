package mobi.cangol.mobile.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import mobi.cangol.mobile.navigation.AbstractNavigationFragmentActivityDelegate
import java.lang.ref.WeakReference


abstract class BaseNavigationFragmentActivity : BaseActionBarActivity() {
    private var menuFragmentReference: WeakReference<BaseMenuFragment>? = null
    private var mHelper: AbstractNavigationFragmentActivityDelegate? = null
    private var mFloatActionBarEnabled: Boolean = false

    open fun isFloatActionBarEnabled(): Boolean {
        return mFloatActionBarEnabled
    }

    open fun setFloatActionBarEnabled(floatActionBarEnabled: Boolean) {
        mFloatActionBarEnabled = floatActionBarEnabled
    }

    /**
     * 返回content布局的id
     *
     * @return
     */
    abstract fun getContentFrameId(): Int

    fun setNavigationFragmentActivityDelegate(mHelper: AbstractNavigationFragmentActivityDelegate) {
        this.mHelper = mHelper
    }

    fun getNavigationFragmentActivityDelegate(): AbstractNavigationFragmentActivityDelegate {
        return mHelper!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mHelper?.onCreate(savedInstanceState)
        this.initFragmentStack(getContentFrameId())
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper?.attachToActivity(this)
        if (savedInstanceState != null) {
            val show = savedInstanceState.getBoolean(MENU_SHOW)
            mHelper?.showMenu(show)
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(MENU_SHOW, isShowMenu())
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        mHelper?.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resId: Int) {
        super.setBackgroundResource(resId)
        mHelper?.setBackgroundResource(resId)
    }

    override fun <T : View> findViewById(id: Int): T? {
        val v = super.findViewById<T>(id)
        return v ?: mHelper!!.getRootView().findViewById(id)
    }

    override fun setContentView(id: Int) {
        setContentView(layoutInflater.inflate(id, null))
    }

    override fun setContentView(v: View) {
        setContentView(v, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    override fun getMaskView(): FrameLayout {
        return if (isFloatActionBarEnabled()) {
            mHelper!!.getMaskView()
        } else {
            super.getMaskView()
        }
    }

    override fun displayMaskView(show: Boolean) {
        if (isFloatActionBarEnabled()) {
            mHelper!!.displayMaskView(show)
        } else {
            super.displayMaskView(show)
        }
    }

    fun showMenu(show: Boolean) {
        mHelper!!.showMenu(show)
    }

    fun isShowMenu(): Boolean {
        return mHelper!!.isShowMenu()
    }

    fun setMenuEnable(enable: Boolean) {
        mHelper!!.setMenuEnable(enable)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val b = mHelper!!.onKeyUp(keyCode, event)
        return if (b) b else super.onKeyUp(keyCode, event)
    }

    fun getCurrentMoudleId(): Int {
        return if (menuFragmentReference == null || menuFragmentReference!!.get() == null) {
            throw IllegalStateException("menuFragment is null")
        } else {
            menuFragmentReference!!.get()!!.getCurrentModuleId()
        }
    }

    fun setCurrentModuleId(moduleId: Int) {
        check(!(menuFragmentReference == null || menuFragmentReference!!.get() == null)) { "menuFragment is null" }
        menuFragmentReference!!.get()?.setCurrentModuleId(moduleId)
    }

    fun getMenuFragment(): BaseMenuFragment? {
        return menuFragmentReference!!.get()
    }

    fun setMenuFragment(fragmentClass: Class<out BaseMenuFragment>, args: Bundle?) {
        val menuFragment = Fragment.instantiate(this, fragmentClass.name, args) as BaseMenuFragment
        menuFragmentReference = WeakReference(menuFragment)
        val t = this.supportFragmentManager
                .beginTransaction()
        t.replace(mHelper!!.getMenuFrameId(), menuFragment, MENU_TAG)
        t.commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }

    fun setContentFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?, moduleId: Int) {
        replaceFragment(fragmentClass, tag, args)
        setCurrentModuleId(moduleId)
    }

    fun setContentFragment(fragmentClass: Class<out BaseFragment>, tag: String, args: Bundle?) {
        replaceFragment(fragmentClass, tag, args)
    }

    fun setContentFragment(fragmentClass: Class<out BaseFragment>, args: Bundle?) {
        replaceFragment(fragmentClass, fragmentClass.name, args)
    }

    fun notifyMenuOnClose() {
            menuFragmentReference?.get()?.onClosed()
    }

    fun notifyMenuOnOpen() {
        if (menuFragmentReference != null && menuFragmentReference!!.get() != null)
            menuFragmentReference?.get()?.onOpen()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val menuFragment = supportFragmentManager.findFragmentByTag(MENU_TAG) as BaseMenuFragment?
        if (menuFragment != null) {
            menuFragmentReference = WeakReference(menuFragment)
        }
    }

    companion object {
        private const  val MENU_SHOW = "MENU_SHOW"
        private const val MENU_TAG = "MenuFragment"
    }
}