package mobi.cangol.mobile.navigation

import android.app.Activity
import android.os.Bundle
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import mobi.cangol.mobile.base.BaseNavigationFragmentActivity
import mobi.cangol.mobile.uiframe.R

abstract class SlidingNavigationFragmentActivity : BaseNavigationFragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.setNavigationFragmentActivityDelegate(SlidingMenuNavigationFragmentActivityDelegate(this))
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

internal class SlidingMenuNavigationFragmentActivityDelegate(
        private val mActivity: BaseNavigationFragmentActivity) : AbstractNavigationFragmentActivityDelegate() {
    private var mRootView: ViewGroup? = null
    private var mSlidingMenuLayout: SlidingMenuLayout? = null
    private var mMaskView: FrameLayout? = null

    override fun getActivity(): BaseNavigationFragmentActivity {
        return mActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.navigation_sliding_main, null) as ViewGroup
        mMaskView = mRootView!!.findViewById(R.id.mask_view)
        mSlidingMenuLayout = mRootView!!.findViewById(R.id.sidingMenuLayout)
        mSlidingMenuLayout!!.setPanelSlideListener(object : PanelSlideListener {
            override fun onPanelClosed(view: View) {
                // 通知menu onClose
                mActivity.notifyMenuOnClose()
                if (mActivity.getCustomFragmentManager()!!.size() <= 1) {
                    mActivity.getCustomActionBar().displayHomeIndicator()
                } else {
                    mActivity.getCustomActionBar().displayUpIndicator()
                }

            }

            override fun onPanelOpened(view: View) {
                // 通知menu onClose
                mActivity.notifyMenuOnClose()
                mActivity.getCustomActionBar().displayUpIndicator()
            }

            override fun onPanelSlide(view: View, slideOffset: Float) {
                mActivity.getCustomActionBar().displayIndicator(slideOffset)
            }

        })
    }

    override fun getRootView(): ViewGroup {
        return mRootView!!
    }

    override fun getMenuView(): ViewGroup {
        return mSlidingMenuLayout!!.getMenuView()
    }

    override fun getContentView(): ViewGroup {
        return mSlidingMenuLayout!!.getContentView()
    }

    override fun setContentView(v: View) {
        mSlidingMenuLayout!!.setContentView(v)
    }

    override fun getMenuFrameId(): Int {
        return mSlidingMenuLayout!!.getMenuFrameId()
    }

    override fun showMenu(show: Boolean) {
        mSlidingMenuLayout!!.showMenu(show)
    }

    override fun isShowMenu(): Boolean {
        return mSlidingMenuLayout!!.isShowMenu()
    }

    override fun setMenuEnable(enable: Boolean) {
        mSlidingMenuLayout!!.setMenuEnable(enable)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        /**
         * if (keyCode == KeyEvent.KEYCODE_BACK
         * && mSlidingMenuLayout.isShowMenu()) {
         * mSlidingMenuLayout.showMenu(false);
         * return false;
         * }  */
        if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenuLayout!!.isShowMenu()) {
            mSlidingMenuLayout!!.showMenu(false)
            return true
        }

        return false
    }

    override fun attachToActivity(activity: Activity) {
        mSlidingMenuLayout!!.attachToActivity(activity, (activity as SlidingNavigationFragmentActivity).isFloatActionBarEnabled())
    }

    override fun setBackgroundColor(color: Int) {
        mSlidingMenuLayout!!.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resId: Int) {
        mSlidingMenuLayout!!.setBackgroundResource(resId)
    }

    override fun getMaskView(): FrameLayout {
        return mMaskView!!
    }

    override fun displayMaskView(show: Boolean) {
        mMaskView!!.visibility = if (show) View.VISIBLE else View.GONE
    }

}