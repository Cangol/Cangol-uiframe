package mobi.cangol.mobile.navigation

import android.app.Activity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.DrawerLayout.DrawerListener
import android.view.*
import android.widget.FrameLayout
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity
import mobi.cangol.mobile.logging.Log
import mobi.cangol.mobile.uiframe.R

abstract class DrawerNavigationFragmentActivity : BaseNavigationFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        this.setNavigationFragmentActivityDelegate(DrawerMenuNavigationFragmentActivityDelegate(this))
        super.onCreate(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (stack!!.size() <= 1) {
            showMenu(!isShowMenu())
            true
        } else {
            super.onSupportNavigateUp()
        }
    }

}

internal class DrawerMenuNavigationFragmentActivityDelegate(
        private val mActivity: BaseNavigationFragmentActivity) : AbstractNavigationFragmentActivityDelegate() {
    private var mDrawerMenuLayout: DrawerMenuLayout? = null

    override fun getActivity(): BaseNavigationFragmentActivity {
        return mActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mDrawerMenuLayout = LayoutInflater.from(mActivity).inflate(R.layout.navigation_drawer_main, null) as DrawerMenuLayout
        mDrawerMenuLayout!!.setDrawerListener(object : DrawerListener {

            override fun onDrawerClosed(view: View) {
                Log.d("onDrawerClosed")
                if (mActivity.getCustomFragmentManager()!!.size() <= 1) {
                    mDrawerMenuLayout!!.setDrawerLockMode(Gravity.LEFT, DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    mActivity.getCustomActionBar().displayHomeIndicator()
                } else {
                    mDrawerMenuLayout!!.setDrawerLockMode(Gravity.LEFT, DrawerLayout.LOCK_MODE_UNLOCKED)
                    mActivity.getCustomActionBar().displayUpIndicator()
                }
                // 通知menu onClose
                mActivity.notifyMenuOnClose()
            }

            override fun onDrawerOpened(view: View) {
                Log.d("onDrawerOpened")
                // 通知menu onOpen
                mActivity.notifyMenuOnOpen()
                mActivity.getCustomActionBar().displayUpIndicator()
            }

            override fun onDrawerSlide(view: View, slideOffset: Float) {
                mActivity.getCustomActionBar().displayIndicator(slideOffset)
            }

            override fun onDrawerStateChanged(arg0: Int) {
                //do nothings
            }

        })
    }

    override fun getRootView(): ViewGroup {
        return mDrawerMenuLayout!!
    }

    override fun getMenuView(): ViewGroup {
        return mDrawerMenuLayout!!.getMenuView()
    }

    override fun getContentView(): ViewGroup {
        return mDrawerMenuLayout!!.getContentView()
    }

    override fun setContentView(v: View) {
        mDrawerMenuLayout!!.setContentView(v)
    }

    override fun getMenuFrameId(): Int {
        return mDrawerMenuLayout!!.getMenuFrameId()
    }

    override fun showMenu(show: Boolean) {
        mDrawerMenuLayout!!.showMenu(show)
    }

    override fun isShowMenu(): Boolean {
        return mDrawerMenuLayout!!.isShowMenu()
    }

    override fun setMenuEnable(enable: Boolean) {
        mDrawerMenuLayout!!.setMenuEnable(enable)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDrawerMenuLayout!!.isShowMenu()) {
            mDrawerMenuLayout!!.showMenu(false)
            return true
        }
        return false
    }

    override fun attachToActivity(activity: Activity) {
        mDrawerMenuLayout!!.attachToActivity(activity, (activity as DrawerNavigationFragmentActivity).isFloatActionBarEnabled())
    }

    override fun setBackgroundColor(color: Int) {
        mDrawerMenuLayout!!.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resId: Int) {
        mDrawerMenuLayout!!.setBackgroundResource(resId)
    }

    override fun getMaskView(): FrameLayout {
        return mDrawerMenuLayout!!.getMaskView()
    }

    override fun displayMaskView(show: Boolean) {
        mDrawerMenuLayout!!.displayMaskView(show)
    }
}