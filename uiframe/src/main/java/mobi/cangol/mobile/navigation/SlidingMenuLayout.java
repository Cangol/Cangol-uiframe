/**
 * Copyright (c) 2013 Cangol
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.navigation;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mobi.cangol.mobile.R;

public class SlidingMenuLayout extends PagerEnabledSlidingPaneLayout {
    private FrameLayout mContentView;
    private FrameLayout mMenuView;
    private float mMenuWidth = 0.70f;
    private boolean isFloatActionBarEnabled;
    private boolean mMenuEnable = true;

    public SlidingMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMenuView = new FrameLayout(context);
        mContentView = new FrameLayout(context);

        int width = (int) (mMenuWidth * context.getResources().getDisplayMetrics().widthPixels);
        ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(width, LayoutParams.MATCH_PARENT);
        mMenuView.setId(R.id.menu_view);
        this.addView(mMenuView, lp1);

        ViewGroup.LayoutParams lp2 = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContentView.setId(R.id.content_view);
        this.addView(mContentView, lp2);

        mContentView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });

        this.setSliderFadeColor(color.transparent);

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
            this.openPane();
        } else {
            this.closePane();
        }
    }

    public boolean isShowMenu() {
        return this.isOpen();
    }

    public void setMenuEnable(boolean enable) {
        mMenuEnable = enable;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mMenuEnable) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mMenuEnable) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    /*
     *  (non-Javadoc)
     * @see android.view.ViewGroup#fitSystemWindows(android.graphics.Rect)
     */
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        int leftPadding = insets.left;
        int rightPadding = insets.right;
        int topPadding = insets.top;
        int bottomPadding = insets.bottom;
        if (isFloatActionBarEnabled) {
//			mContentView.setPadding(mContentView.getPaddingLeft()+leftPadding,
//					mContentView.getPaddingTop()+topPadding,
//					mContentView.getPaddingRight()+rightPadding,
//					mContentView.getPaddingBottom()+bottomPadding);
//			mMenuView.setPadding(mMenuView.getPaddingLeft()+leftPadding,
//					mMenuView.getPaddingTop()+topPadding,
//					mMenuView.getPaddingRight()+rightPadding,
//					mMenuView.getPaddingBottom()+bottomPadding);

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
        //mMenuView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        //mMenuView.setBackgroundResource(resId);
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
