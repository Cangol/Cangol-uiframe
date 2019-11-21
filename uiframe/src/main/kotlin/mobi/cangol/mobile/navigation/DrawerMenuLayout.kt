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
import mobi.cangol.mobile.uiframe.R



class DrawerMenuLayout(context: Context, attrs: AttributeSet) : DrawerLayout(context, attrs) {
    private val mContentView: FrameLayout = FrameLayout(context)
    private val mMenuView: FrameLayout = FrameLayout(context)
    private val mMaskView: FrameLayout = FrameLayout(context)
    private val mMenuWidth = 0.75f
    private var isFloatActionBarEnabled: Boolean = false

    init {

        val lp1 = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp1.gravity = Gravity.NO_GRAVITY
        mContentView.id = R.id.content_view
        this.addView(mContentView, lp1)

        val lp3 = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp3.gravity = Gravity.NO_GRAVITY
        mMaskView.id = R.id.mask_view
        mMaskView.visibility = View.GONE
        this.addView(mMaskView, lp3)

        val width = (mMenuWidth * context.resources.displayMetrics.widthPixels).toInt()
        val lp2 = LayoutParams(width, LayoutParams.MATCH_PARENT)
        lp2.gravity = Gravity.LEFT
        mMenuView.id = R.id.menu_view
        this.addView(mMenuView, lp2)


    }

    fun getMenuFrameId(): Int {
        return mMenuView.id
    }

    fun getContentFrameId(): Int {
        return mContentView.id
    }

    fun getMenuView(): ViewGroup {
        return mMenuView
    }

    fun getContentView(): ViewGroup {
        return mContentView
    }

    fun setContentView(v: View) {
        mContentView.removeAllViews()
        mContentView.addView(v)
    }
    fun getMaskView(): FrameLayout {
        return mMaskView
    }
    fun displayMaskView(show: Boolean) {
        this.mMaskView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showMenu(show: Boolean) {
        if (show) {
            this.openDrawer(Gravity.LEFT)
        } else {
            this.closeDrawer(Gravity.LEFT)
        }
    }

    fun isShowMenu(): Boolean {
        return this.isDrawerOpen(Gravity.LEFT)
    }

    fun setMenuEnable(enable: Boolean) {
        this.setDrawerLockMode(if (enable) LOCK_MODE_UNLOCKED else LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT)
    }

    override fun fitSystemWindows(insets: Rect): Boolean {
        if (isFloatActionBarEnabled) {
            fitPadding(insets)
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
        val hasNavigationBar = checkDeviceHasNavigationBar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && hasNavigationBar) {
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            when (manager.defaultDisplay.rotation) {
                Surface.ROTATION_90 -> rect.right += getNavBarWidth()
                Surface.ROTATION_180 -> rect.top += getNavBarHeight()
                Surface.ROTATION_270 -> rect.left += getNavBarWidth()
                else -> rect.bottom += getNavBarHeight()
            }
        }
        mContentView.setPadding(rect.left, rect.top, rect.right, rect.bottom)
        mMenuView.setPadding(rect.left, rect.top, rect.right, rect.bottom)
        mMaskView.setPadding(rect.left, rect.top, rect.right, rect.bottom)
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

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        mMenuView.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resId: Int) {
        super.setBackgroundResource(resId)
        mMenuView.setBackgroundResource(resId)
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
        private val TAG = "DrawerMenuLayout"
    }
}