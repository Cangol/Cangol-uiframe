package mobi.cangol.mobile.navigation

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.view.*
import android.widget.FrameLayout
import mobi.cangol.mobile.base.BaseFragment
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity

import mobi.cangol.mobile.uiframe.R

/**
 * Created by xuewu.wei on 2017/9/19.
 */

abstract class TabDrawerNavigationFragmentActivity : BaseNavigationFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        this.setNavigationFragmentActivityDelegate(TabDrawerNavigationFragmentActivityDelegate(
                this))
        super.onCreate(savedInstanceState)
        this.customActionBar.titleGravity = Gravity.CENTER
        this.customActionBar.setDisplayShowHomeEnabled(false)
        //默认是不能滑动的
        this.setDrawerEnable(Gravity.LEFT, false)
        this.setDrawerEnable(Gravity.RIGHT, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (stack!!.size() <= 1) {
            stack!!.peek()!!.onSupportNavigateUp()
            true
        } else {
            super.onSupportNavigateUp()
        }
    }

    fun setDrawerEnable(gravity: Int, enable: Boolean) {
        if (getNavigationFragmentActivityDelegate() is TabDrawerNavigationFragmentActivityDelegate) {
            (getNavigationFragmentActivityDelegate() as TabDrawerNavigationFragmentActivityDelegate).setDrawerEnable(gravity, enable)
        } else {
            throw IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE)
        }
    }

    fun showDrawer(gravity: Int, show: Boolean) {
        if (getNavigationFragmentActivityDelegate() is TabDrawerNavigationFragmentActivityDelegate) {
            (getNavigationFragmentActivityDelegate() as TabDrawerNavigationFragmentActivityDelegate).showDrawer(gravity, show)
        } else {
            throw IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE)
        }
    }

    fun isShowDrawer(gravity: Int): Boolean {
        return if (getNavigationFragmentActivityDelegate() is TabDrawerNavigationFragmentActivityDelegate) {
            (getNavigationFragmentActivityDelegate() as TabDrawerNavigationFragmentActivityDelegate).isShowDrawer(gravity)
        } else {
            throw IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE)
        }
    }

    fun setDrawer(gravity: Int, fragmentClass: Class<out BaseFragment>, args: Bundle?) {
        val drawerFragment = Fragment.instantiate(this, fragmentClass.name, args) as BaseFragment
        if (getNavigationFragmentActivityDelegate() is TabDrawerNavigationFragmentActivityDelegate) {
            (getNavigationFragmentActivityDelegate() as TabDrawerNavigationFragmentActivityDelegate).setDrawer(gravity, drawerFragment)
        } else {
            throw IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE)
        }
    }

    fun getDrawer(gravity: Int): BaseFragment? {
        return if (getNavigationFragmentActivityDelegate() is TabDrawerNavigationFragmentActivityDelegate) {
            (getNavigationFragmentActivityDelegate() as TabDrawerNavigationFragmentActivityDelegate).getDrawer(gravity)
        } else {
            throw IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE)
        }
    }

    fun removeDrawer(gravity: Int) {
        if (getNavigationFragmentActivityDelegate() is TabDrawerNavigationFragmentActivityDelegate) {
            (getNavigationFragmentActivityDelegate() as TabDrawerNavigationFragmentActivityDelegate).removeDrawer(gravity)
        } else {
            throw IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE)
        }
    }

    companion object {
        const val GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE = "getActivity is TabDrawerNavigationFragmentActivityDelegate"
    }
}

internal class TabDrawerNavigationFragmentActivityDelegate(
        private val mActivity: BaseNavigationFragmentActivity) : AbstractNavigationFragmentActivityDelegate() {
    private var mDrawerLayout: TabMenuDrawerLayout? = null
    private var mMenuView: FrameLayout? = null
    private var mContentView: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mDrawerLayout = LayoutInflater.from(mActivity).inflate(
                R.layout.navigation_tab_drawer_main, null) as TabMenuDrawerLayout

        mContentView = mDrawerLayout!!.findViewById(R.id.content_view)
        mMenuView = mDrawerLayout!!.findViewById(R.id.menu_view)
        mDrawerLayout?.setDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val fragment = mActivity.supportFragmentManager.findFragmentById(drawerView.id)
                if (fragment is BaseFragment) {
                    fragment.onDrawerSlide(slideOffset)
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                val fragment = mActivity.supportFragmentManager.findFragmentById(drawerView.id)
                if (fragment is BaseFragment) {
                    fragment.userVisibleHint = true
                    fragment.onDrawerOpened()
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                val fragment = mActivity.supportFragmentManager.findFragmentById(drawerView.id)
                if (fragment is BaseFragment) {
                    fragment.userVisibleHint = false
                    fragment.onDrawerClosed()
                }
            }

            override fun onDrawerStateChanged(newState: Int) {
                //do nothings
            }
        })
    }

    fun setDrawerEnable(gravity: Int, enable: Boolean) {
        mDrawerLayout!!.setDrawerEnable(gravity, enable)
    }

    override fun getMaskView(): FrameLayout {
        return mDrawerLayout!!.getMaskView()
    }

    override fun displayMaskView(show: Boolean) {
        mDrawerLayout!!.displayMaskView(show)
    }

    fun showDrawer(gravity: Int, show: Boolean) {
        mDrawerLayout!!.showDrawer(gravity, show)
    }

    fun isShowDrawer(gravity: Int): Boolean {
        return mDrawerLayout!!.isShowDrawer(gravity)
    }

    override fun getMenuFrameId(): Int {
        return mMenuView!!.id
    }

    override fun getRootView(): ViewGroup {
        return mDrawerLayout!!
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
            mMenuView!!.visibility = View.VISIBLE
        } else {
            mMenuView!!.visibility = View.GONE
        }
    }

    override fun isShowMenu(): Boolean {
        return mMenuView!!.visibility == View.VISIBLE
    }

    override fun setMenuEnable(enable: Boolean) {
        if (enable) {
            mMenuView!!.visibility = View.VISIBLE
        } else {
            mMenuView!!.visibility = View.GONE
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout!!.isShowDrawer(Gravity.LEFT)) {
                mDrawerLayout!!.showDrawer(Gravity.LEFT, false)
                return true
            } else if (mDrawerLayout!!.isShowDrawer(Gravity.RIGHT)) {
                mDrawerLayout!!.showDrawer(Gravity.RIGHT, false)
                return true
            }
        }
        return false
    }

    override fun attachToActivity(activity: Activity) {
        mDrawerLayout!!.attachToActivity(activity, (activity as TabDrawerNavigationFragmentActivity).isFloatActionBarEnabled())
    }

    override fun getActivity(): BaseNavigationFragmentActivity {
        return mActivity
    }

    override fun setBackgroundColor(color: Int) {
        mDrawerLayout!!.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resId: Int) {
        mDrawerLayout!!.setBackgroundResource(resId)
    }

    fun setDrawer(gravity: Int, fragment: BaseFragment) {
        fragment.userVisibleHint = false
        val t = mActivity.supportFragmentManager
                .beginTransaction()
        t.replace(if (gravity == Gravity.LEFT) R.id.left_view else R.id.right_view, fragment, "DrawerFragment_$gravity")
        t.commitAllowingStateLoss()
        mActivity.supportFragmentManager.executePendingTransactions()
    }

    fun getDrawer(gravity: Int): BaseFragment? {
        val fragment = mActivity.supportFragmentManager.findFragmentById(if (gravity == Gravity.LEFT) R.id.left_view else R.id.right_view)
        return if (fragment != null) {
            fragment as BaseFragment?
        } else {
            null
        }
    }

    fun removeDrawer(gravity: Int) {
        val fragment = mActivity.supportFragmentManager.findFragmentById(if (gravity == Gravity.LEFT) R.id.left_view else R.id.right_view)
        if (fragment != null) {
            val t = mActivity.supportFragmentManager
                    .beginTransaction()
            t.remove(fragment)
            t.commitAllowingStateLoss()
            mActivity.supportFragmentManager.executePendingTransactions()
        }
    }

}
