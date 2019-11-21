package mobi.cangol.mobile.navigation


import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import mobi.cangol.mobile.uiframe.R


class TabMenuDrawerLayout(context: Context, attrs: AttributeSet) : DrawerLayout(context, attrs) {
    private val mRootView: LinearLayout = LayoutInflater.from(context).inflate(R.layout.navigation_tab_main, null) as LinearLayout
    private val mLeftView: FrameLayout = FrameLayout(context)
    private val mRightView: FrameLayout = FrameLayout(context)
    private val mMaskView: FrameLayout = FrameLayout(context)
    private val mDrawerWidth = 0.618f
    private var isFloatActionBarEnabled: Boolean = false

    init {

        val lp1 = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp1.gravity = Gravity.NO_GRAVITY
        mRootView.id = R.id.main_view
        this.addView(mRootView, lp1)

        val lp4 = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp4.gravity = Gravity.NO_GRAVITY
        mMaskView.id = R.id.mask_view
        mMaskView.visibility = View.GONE
        this.addView(mMaskView, lp4)

        val width = (mDrawerWidth * context.resources.displayMetrics.widthPixels).toInt()
        val lp2 = LayoutParams(width, LayoutParams.MATCH_PARENT)
        lp2.gravity = Gravity.LEFT
        mLeftView.id = R.id.left_view
        this.addView(mLeftView, lp2)

        val lp3 = LayoutParams(width, LayoutParams.MATCH_PARENT)
        lp3.gravity = Gravity.RIGHT
        mRightView.id = R.id.right_view
        this.addView(mRightView, lp3)

    }

    fun getContentView(): ViewGroup {
        return mRootView.findViewById<View>(R.id.content_view) as ViewGroup
    }

    fun setContentView(v: View) {
        getContentView().removeAllViews()
        getContentView().addView(v)
    }

    fun getMaskView(): FrameLayout {
        return mMaskView
    }

    fun displayMaskView(show: Boolean) {
        this.mMaskView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showDrawer(gravity: Int, show: Boolean) {
        if (show) {
            if (gravity == Gravity.LEFT && mLeftView.childCount > 0) {
                this.openDrawer(gravity)
            } else if (gravity == Gravity.RIGHT && mRightView.childCount > 0) {
                this.openDrawer(gravity)
            } else {
                Log.e("showDrawer", "gravity is not LEFT|RIGHT,drawer is empty")
            }
        } else {
            this.closeDrawer(gravity)
        }
    }

    fun isShowDrawer(gravity: Int): Boolean {
        return this.isDrawerOpen(gravity)
    }

    fun setDrawerEnable(gravity: Int, enable: Boolean) {
        this.setDrawerLockMode(if (enable) LOCK_MODE_UNLOCKED else LOCK_MODE_LOCKED_CLOSED, gravity)
    }

    override fun fitSystemWindows(rect: Rect): Boolean {
        Log.d(TAG, "fitSystemWindows $rect")
        if (isFloatActionBarEnabled) {
            fitPadding(rect)
            fitDecorChild(this)
        }
        return true
    }

    private fun fitDecorChild(view: View) {
        val contentView = view.findViewById<ViewGroup>(R.id.actionbar_content_view)
        if (contentView != null) {
            val decorChild = contentView.getChildAt(0) as ViewGroup
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val layoutParams = decorChild.layoutParams as FrameLayout.LayoutParams
                when (manager.defaultDisplay.rotation) {
                    Surface.ROTATION_90 -> layoutParams.rightMargin = 0
                    Surface.ROTATION_180 -> layoutParams.topMargin = 0
                    Surface.ROTATION_270 -> layoutParams.leftMargin = 0
                    else -> layoutParams.bottomMargin = 0
                }
                decorChild.layoutParams = layoutParams
            }
        }
    }

    private fun fitPadding(rect: Rect) {
        Log.d(TAG, "fitPadding $rect")
        val hasNavigationBar = checkDeviceHasNavigationBar()
        Log.d(TAG, "checkDeviceHasNavigationBar=$hasNavigationBar")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && hasNavigationBar) {
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            when (manager.defaultDisplay.rotation) {
                Surface.ROTATION_90 -> rect.right += getNavBarWidth()
                Surface.ROTATION_180 -> rect.top += getNavBarHeight()
                Surface.ROTATION_270 -> rect.left += getNavBarWidth()
                else -> rect.bottom += getNavBarHeight()
            }
        }
        mRootView.setPadding(rect.left, rect.top, rect.right, rect.bottom)
        mLeftView.setPadding(rect.left, rect.top, rect.right, rect.bottom)
        mRightView.setPadding(rect.left, rect.top, rect.right, rect.bottom)
        mMaskView.setPadding(rect.left, rect.top, rect.right, rect.bottom)
    }

    private fun getNavBarWidth(): Int {
        return getNavBarDimen("navigation_bar_width")
    }

    private fun getNavBarHeight(): Int {
        return getNavBarDimen("navigation_bar_height")
    }

    private fun getNavBarDimen(resourceString: String): Int {
        val r = resources
        val id = r.getIdentifier(resourceString, "dimen", "android")
        return if (id > 0) {
            r.getDimensionPixelSize(id)
        } else {
            0
        }
    }

    /**
     * 检测是否具有底部导航栏
     * @return
     */
    private fun checkDeviceHasNavigationBar(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val realDisplayMetrics = DisplayMetrics()
            display.getRealMetrics(realDisplayMetrics)
            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels
            return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
        } else {
            var hasNavigationBar = false
            val resources = context.resources
            val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
            if (id > 0) {
                hasNavigationBar = resources.getBoolean(id)
            }
            try {
                val systemPropertiesClass = Class.forName("android.os.SystemProperties")
                val m = systemPropertiesClass.getMethod("get", String::class.java)
                val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
                if ("1" == navBarOverride) {
                    hasNavigationBar = false
                } else if ("0" == navBarOverride) {
                    hasNavigationBar = true
                }
            } catch (e: Exception) {
                Log.e(TAG, "checkDeviceHasNavigationBar", e)
            }

            return hasNavigationBar
        }
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        mRightView.setBackgroundColor(color)
        mLeftView.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resId: Int) {
        super.setBackgroundResource(resId)
        mRightView.setBackgroundResource(resId)
        mLeftView.setBackgroundResource(resId)
    }

    fun attachToActivity(activity: Activity, isFloatActionBarEnabled: Boolean) {
        // get the window background
        val a = activity.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val background = a.getResourceId(0, 0)
        a.recycle()

        this.isFloatActionBarEnabled = isFloatActionBarEnabled

        if (isFloatActionBarEnabled) {
            val decor = activity.window.decorView as ViewGroup
            val decorChild = decor.getChildAt(0) as ViewGroup
            if (decorChild.background != null) {
                this.background=decorChild.background
                decorChild.background=null
            } else {
                if (this.background == null)
                    this.setBackgroundResource(background)
            }
            decor.removeView(decorChild)
            decor.addView(this, 0)
            getContentView().addView(decorChild)
        } else {
            val contentParent = activity.findViewById<View>(android.R.id.content) as ViewGroup
            val content = contentParent.getChildAt(0) as ViewGroup
            contentParent.removeView(content)
            contentParent.addView(this, 0)
            getContentView().addView(content)
        }
    }

    companion object {
        private val TAG = "TabMenuDrawerLayout"
    }
}