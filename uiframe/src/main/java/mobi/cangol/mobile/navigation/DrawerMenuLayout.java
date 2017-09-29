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
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mobi.cangol.mobile.uiframe.R;


public class DrawerMenuLayout extends DrawerLayout {
    private FrameLayout mContentView;
    private FrameLayout mMenuView;
    private float mMenuWidth = 0.618f;
    private boolean isFloatActionBarEnabled;

    public DrawerMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMenuView = new FrameLayout(context);
        mContentView = new FrameLayout(context);

        DrawerLayout.LayoutParams lp1 = new DrawerLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContentView.setId(R.id.content_view);
        this.addView(mContentView, lp1);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    /*
     *  (non-Javadoc)
     * @see android.view.ViewGroup#fitSystemWindows(android.graphics.Rect)
     */
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        super.fitSystemWindows(insets);
        int leftPadding = insets.left;
        int rightPadding = insets.right;
        int topPadding = insets.top;
        int bottomPadding = insets.bottom;
        Log.e("fitSystemWindows","fitSystemWindows="+insets.toString());
        if (isFloatActionBarEnabled) {
            mContentView.setPadding(leftPadding,
                    topPadding,
                    rightPadding,
                    bottomPadding);
            mMenuView.setPadding(leftPadding,
                    topPadding,
                    rightPadding,
                    bottomPadding);
        }
        return true;
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
        // get the window background
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        this.isFloatActionBarEnabled = isFloatActionBarEnabled;

        if (isFloatActionBarEnabled) {
            ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
            if (decorChild.getBackground() != null) {
                this.setBackgroundDrawable(decorChild.getBackground());
                decorChild.setBackgroundDrawable(null);
            } else {
                if (this.getBackground() == null)
                    this.setBackgroundResource(background);
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
