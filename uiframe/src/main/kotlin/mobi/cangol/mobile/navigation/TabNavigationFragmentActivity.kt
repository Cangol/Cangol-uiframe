package mobi.cangol.mobile.navigation

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import mobi.cangol.mobile.base.BaseNavigationFragmentActivity
import mobi.cangol.mobile.uiframe.R

abstract class TabNavigationFragmentActivity : BaseNavigationFragmentActivity() {

    /**
     * 此方法无效 固定返回false
     * @return
     */
    override fun isFloatActionBarEnabled(): Boolean {
        return false
    }

    /**
     * 此方法无效
     * @param floatActionBarEnabled
     */
    override fun setFloatActionBarEnabled(floatActionBarEnabled: Boolean) {
        //
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.setNavigationFragmentActivityDelegate(TabNavigationFragmentActivityDelegate(
                this))
        super.onCreate(savedInstanceState)
        this.getCustomActionBar().setTitleGravity(Gravity.CENTER)
        this.getCustomActionBar().setDisplayShowHomeEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (stack == null || stack!!.size() == 0 || stack!!.peek() == null) {
            true
        } else if (stack!!.size() <= 1) {
            stack!!.peek()!!.onSupportNavigateUp()
            true
        } else {
            super.onSupportNavigateUp()
        }
    }
}

internal class TabNavigationFragmentActivityDelegate(
        private val mActivity: BaseNavigationFragmentActivity) : AbstractNavigationFragmentActivityDelegate() {
    private var mRootView: ViewGroup? = null
    private var mMenuView: FrameLayout? = null
    private var mContentView: FrameLayout? = null
    private var mMaskView: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mRootView = LayoutInflater.from(mActivity).inflate(
                R.layout.navigation_tab_main, null) as ViewGroup
        mContentView = mRootView?.findViewById(R.id.content_view)
        mMenuView = mRootView?.findViewById(R.id.menu_view)
    }

    override fun getMenuFrameId(): Int {
        return mMenuView!!.id
    }

    override fun getRootView(): ViewGroup {
        return mRootView!!
    }

    override fun getMenuView(): ViewGroup {
        return mMenuView!!
    }

    override fun getContentView(): ViewGroup {
        return mContentView!!
    }

    override fun setContentView(v: View) {
        mContentView!!.addView(v)
    }

    override fun showMenu(show: Boolean) {
        if (show) {
            mMenuView?.visibility = View.VISIBLE
        } else {
            mMenuView?.visibility = View.GONE
        }
    }

    override fun isShowMenu(): Boolean {
        return mMenuView?.visibility == View.VISIBLE
    }

    override fun setMenuEnable(enable: Boolean) {
        if (enable) {
            mMenuView?.visibility = View.VISIBLE
        } else {
            mMenuView?.visibility = View.GONE
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    override fun getActivity(): BaseNavigationFragmentActivity {
        return mActivity
    }

    override fun attachToActivity(activity: Activity) {
        val contentParent = getActivity().findViewById<ViewGroup>(android.R.id.content)
        val content = contentParent?.getChildAt(0) as ViewGroup
        contentParent.removeView(content)
        contentParent.addView(mRootView, 0)
        getContentView().addView(content)

    }

    override fun setBackgroundColor(color: Int) {
        mRootView?.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resId: Int) {
        mRootView?.setBackgroundResource(resId)
    }

    override fun getMaskView(): FrameLayout {
        return mMaskView!!
    }

    override fun displayMaskView(show: Boolean) {
        //do nothings
    }

}