package mobi.cangol.mobile.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

import mobi.cangol.mobile.uiframe.R;


public class TabMenuDrawerLayout extends DrawerLayout  {
    private static final String TAG="TabMenuDrawerLayout";
    private LinearLayout mRootView;
    private FrameLayout mLeftView;
    private FrameLayout mRightView;
    private FrameLayout mMaskView;
    private float mDrawerWidth = 0.618f;
    private boolean isFloatActionBarEnabled;

    public TabMenuDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLeftView = new FrameLayout(context);
        mRightView = new FrameLayout(context);
        mMaskView = new FrameLayout(context);

        mRootView= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.navigation_tab_main, null);
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp1.gravity = Gravity.NO_GRAVITY;
        mRootView.setId(R.id.main_view);
        this.addView(mRootView, lp1);

        LayoutParams lp4 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp4.gravity = Gravity.NO_GRAVITY;
        mMaskView.setId(R.id.mask_view);
        mMaskView.setVisibility(GONE);
        this.addView(mMaskView, lp4);

        int width = (int) (mDrawerWidth * context.getResources().getDisplayMetrics().widthPixels);
        LayoutParams lp2 = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        lp2.gravity = Gravity.LEFT;
        mLeftView.setId(R.id.left_view);
        this.addView(mLeftView, lp2);

        LayoutParams lp3 = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        lp3.gravity = Gravity.RIGHT;
        mRightView.setId(R.id.right_view);
        this.addView(mRightView, lp3);

    }

    public ViewGroup getContentView() {
        return (ViewGroup) mRootView.findViewById(R.id.content_view);
    }

    public void setContentView(View v) {
        getContentView().removeAllViews();
        getContentView().addView(v);
    }

    public FrameLayout getMaskView() {
        return mMaskView;
    }

    public void displayMaskView(boolean show) {
        this.mMaskView.setVisibility(show?VISIBLE:GONE);
    }

    public void showDrawer(int gravity, boolean show) {
        if (show) {
            if(gravity==Gravity.LEFT&&mLeftView.getChildCount()>0){
                this.openDrawer(gravity);
            }else if(gravity==Gravity.RIGHT&&mRightView.getChildCount()>0){
                this.openDrawer(gravity);
            }else{
                Log.e("showDrawer","gravity is not LEFT|RIGHT,drawer is empty");
            }
        } else {
            this.closeDrawer(gravity);
        }
    }

    public boolean isShowDrawer(int gravity) {
        return this.isDrawerOpen(gravity);
    }

    public void setDrawerEnable(int gravity, boolean enable) {
        this.setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED, gravity);
    }
    @Override
    protected boolean fitSystemWindows(Rect rect) {
        Log.d(TAG,"fitSystemWindows "+rect.toString());
        if (isFloatActionBarEnabled) {
            fitPadding(rect);
            fitDecorChild(this);
        }
        return true;
    }

    private void fitDecorChild(View view){
        ViewGroup contentView=  view.findViewById(R.id.actionbar_content_view);
        if(contentView!=null){
            ViewGroup decorChild= (ViewGroup)contentView.getChildAt(0);
            if(decorChild!=null
                &&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    FrameLayout.LayoutParams layoutParams=(FrameLayout.LayoutParams)decorChild.getLayoutParams();
                    switch (manager.getDefaultDisplay().getRotation()) {
                        case Surface.ROTATION_90:
                            layoutParams.rightMargin=0;
                            break;
                        case Surface.ROTATION_180:
                            layoutParams.topMargin=0;
                            break;
                        case Surface.ROTATION_270:
                            layoutParams.leftMargin=0;
                            break;
                        default:
                            layoutParams.bottomMargin=0;
                            break;
                    }
                    decorChild.setLayoutParams(layoutParams);
            }
        }
    }

    private void fitPadding(Rect rect) {
        Log.d(TAG,"fitPadding "+rect.toString());
        boolean hasNavigationBar=checkDeviceHasNavigationBar();
        Log.d(TAG,"checkDeviceHasNavigationBar="+hasNavigationBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&hasNavigationBar) {
            WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            switch (manager.getDefaultDisplay().getRotation()) {
                case Surface.ROTATION_90:
                    rect.right += getNavBarWidth();
                    break;
                case Surface.ROTATION_180:
                    rect.top += getNavBarHeight();
                    break;
                case Surface.ROTATION_270:
                    rect.left += getNavBarWidth();
                    break;
                default:
                    rect.bottom += getNavBarHeight();
                    break;
            }
        }
        mRootView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
        mLeftView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
        mRightView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
        mMaskView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    private int getNavBarWidth() {
        return getNavBarDimen("navigation_bar_width");
    }
    private int getNavBarHeight() {
        return getNavBarDimen("navigation_bar_height");
    }
    private int getNavBarDimen(String resourceString) {
        Resources r = getResources();
        int id = r.getIdentifier(resourceString, "dimen", "android");
        if (id > 0) {
            return r.getDimensionPixelSize(id);
        } else {
            return 0;
        }
    }
    /**
     * 检测是否具有底部导航栏
     * @return
     */
    private  boolean checkDeviceHasNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            display.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;
            return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else {
            boolean hasNavigationBar = false;
            Resources resources = getContext().getResources();
            int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = resources.getBoolean(id);
            }
            try {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride)) {
                    hasNavigationBar = false;
                } else if ("0".equals(navBarOverride)) {
                    hasNavigationBar = true;
                }
            } catch (Exception e) {
                Log.e(TAG,"checkDeviceHasNavigationBar",e);
            }
            return hasNavigationBar;
        }
    }
    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mRightView.setBackgroundColor(color);
        mLeftView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        mRightView.setBackgroundResource(resId);
        mLeftView.setBackgroundResource(resId);
    }
    public void attachToActivity(Activity activity, boolean isFloatActionBarEnabled) {
        this.isFloatActionBarEnabled = isFloatActionBarEnabled;

        if (isFloatActionBarEnabled) {
            ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
            if (decorChild.getBackground() != null) {
                this.setBackgroundDrawable(decorChild.getBackground());
                decorChild.setBackgroundDrawable(null);
            }
            decor.removeView(decorChild);
            decor.addView(this, 0);
            getContentView().addView(decorChild);
        } else {
            ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
            ViewGroup content = (ViewGroup) contentParent.getChildAt(0);
            contentParent.removeView(content);
            contentParent.addView(this, 0);
            getContentView().addView(content);
        }
    }
}
