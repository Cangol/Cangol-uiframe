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

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.R;

public abstract class TabNavigationFragmentActivity extends
        BaseNavigationFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setNavigationFragmentActivityDelegate(new TabNavigationFragmentActivityDelegate(
                this));
        super.onCreate(savedInstanceState);
        this.getCustomActionBar().setTitleGravity(Gravity.CENTER);
        this.getCustomActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    abstract public void findViews();

    @Override
    abstract public void initViews(Bundle savedInstanceState);

    @Override
    abstract public void initData(Bundle savedInstanceState);

    @Override
    public boolean onSupportNavigateUp() {
        if (stack.size() <= 1) {
            stack.peek().onSupportNavigateUp();
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }
}

class TabNavigationFragmentActivityDelegate extends
        AbstractNavigationFragmentActivityDelegate {
    private BaseNavigationFragmentActivity mActivity;
    private ViewGroup mRootView;
    private FrameLayout mMenuView;
    private FrameLayout mContentView;

    public TabNavigationFragmentActivityDelegate(
            BaseNavigationFragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mRootView = (ViewGroup) LayoutInflater.from(mActivity).inflate(
                R.layout.navigation_tab_main, null);
        mContentView = (FrameLayout) mRootView.findViewById(R.id.content_view);
        mMenuView = (FrameLayout) mRootView.findViewById(R.id.menu_view);
//        mRootView.setOnSoftKeyboardVisibilityChangeListener(new SoftKeyboardHandledLinearLayout.SoftKeyboardVisibilityChangeListener() {
//            boolean justShowMenu = false;
//
//            @Override
//            public void onSoftKeyboardShow() {
//                if (isShowMenu()) {
//                    justShowMenu = true;
//                    showMenu(false);
//                } else {
//                    justShowMenu = false;
//                }
//            }
//
//            @Override
//            public void onSoftKeyboardHide() {
//                if (justShowMenu) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            showMenu(true);
//                        }
//                    }, 300L);
//                }
//            }
//        });
    }

    @Override
    public int getMenuFrameId() {
        return mMenuView.getId();
    }

    @Override
    public ViewGroup getRootView() {
        return mRootView;
    }

    @Override
    public ViewGroup getMenuView() {
        return mMenuView;
    }

    @Override
    public ViewGroup getContentView() {
        return mContentView;
    }

    @Override
    public void setContentView(View v) {
        mContentView.addView(v);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void showMenu(boolean show) {
        if (show) {
            mMenuView.setVisibility(View.VISIBLE);
        } else {
            mMenuView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isShowMenu() {
        return mMenuView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void setMenuEnable(boolean enable) {
        if (enable) {
            mMenuView.setVisibility(View.VISIBLE);
        } else {
            mMenuView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public BaseNavigationFragmentActivity getActivity() {
        return mActivity;
    }

    @Override
    public void attachToActivity(Activity activity) {
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup contentParent = (ViewGroup) getActivity().findViewById(android.R.id.content);
        ViewGroup content = (ViewGroup) contentParent.getChildAt(0);
        contentParent.removeView(content);
        contentParent.addView(mRootView, 0);
        getContentView().addView(content);

    }

    @Override
    public void setBackgroundColor(int color) {
        mRootView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resId) {
        mRootView.setBackgroundResource(resId);

    }
}
