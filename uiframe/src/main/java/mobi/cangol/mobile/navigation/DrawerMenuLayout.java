/*
 * Copyright (c) 2013 Cangol
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Method;

import mobi.cangol.mobile.uiframe.R;


public class DrawerMenuLayout extends DrawerLayout {
    private static final String TAG="DrawerMenuLayout";
    private FrameLayout mContentView;
    private FrameLayout mMenuView;
    private FrameLayout mMaskView;
    private float mMenuWidth = 0.75f;
    private boolean isFloatActionBarEnabled;

    public DrawerMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMenuView = new FrameLayout(context);
        mContentView = new FrameLayout(context);
        mMaskView = new FrameLayout(context);

        DrawerLayout.LayoutParams lp1 = new DrawerLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp1.gravity = Gravity.NO_GRAVITY;
        mContentView.setId(R.id.content_view);
        this.addView(mContentView, lp1);

        LayoutParams lp3 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp3.gravity = Gravity.NO_GRAVITY;
        mMaskView.setId(R.id.mask_view);
        mMaskView.setVisibility(GONE);
        this.addView(mMaskView, lp3);

        int width = (int) (mMenuWidth * context.getResources().getDisplayMetrics().widthPixels);
        DrawerLayout.LayoutParams lp2 = new DrawerLayout.LayoutParams(width, LayoutParams.MATCH_PARENT);
        lp2.gravity = Gravity.LEFT;
        mMenuView.setId(R.id.menu_view);
        this.addView(mMenuView, lp2);



    }

    public int getMenuFrameId() {
        return mMenuView.getId();
    }

    public int getContentFrameId() {
        return mContentView.getId();
    }

    public ViewGroup getMenuView() {
        return mMenuView;
    }

    public ViewGroup getContentView() {
        return mContentView;
    }

    public void setContentView(View v) {
        mContentView.removeAllViews();
        mContentView.addView(v);
    }
    public FrameLayout getMaskView() {
        return mMaskView;
    }

    public void displayMaskView(boolean show) {
        this.mMaskView.setVisibility(show?VISIBLE:GONE);
    }

    public void showMenu(boolean show) {
        if (show) {
            this.openDrawer(Gravity.LEFT);
        } else {
            this.closeDrawer(Gravity.LEFT);
        }
    }

    public boolean isShowMenu() {
        return this.isDrawerOpen(Gravity.LEFT);
    }

    public void setMenuEnable(boolean enable) {
        this.setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED
                : DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (isFloatActionBarEnabled) {
            fitPadding(insets);
            fitDecorChild(this);
        }
        return true;
    }
    private void fitDecorChild(View view){
        ViewGroup contentView= view.findViewById(R.id.actionbar_content_view);
        if(contentView!=null){
            ViewGroup decorChild= (ViewGroup)contentView.getChildAt(0);
            if(decorChild!=null&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
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
        boolean hasNavigationBar=checkDeviceHasNavigationBar();
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
        mContentView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
        mMenuView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
        mMaskView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
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
    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mMenuView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        mMenuView.setBackgroundResource(resId);
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
