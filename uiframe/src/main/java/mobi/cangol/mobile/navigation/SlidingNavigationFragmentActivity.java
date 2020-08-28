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
import android.os.Bundle;
import androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.R;

public abstract class SlidingNavigationFragmentActivity extends
        BaseNavigationFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setNavigationFragmentActivityDelegate(new SlidingMenuNavigationFragmentActivityDelegate(this));
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onSupportNavigateUp() {
        if (stack.size() <= 1) {
            showMenu(!isShowMenu());
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }

}

class SlidingMenuNavigationFragmentActivityDelegate extends
        AbstractNavigationFragmentActivityDelegate {
    private BaseNavigationFragmentActivity mActivity;
    private ViewGroup mRootView;
    private SlidingMenuLayout mSlidingMenuLayout;
    private FrameLayout mMaskView;
    public SlidingMenuNavigationFragmentActivityDelegate(
            BaseNavigationFragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public BaseNavigationFragmentActivity getActivity() {
        return mActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mRootView= (ViewGroup) LayoutInflater.from(mActivity).inflate(R.layout.navigation_sliding_main, null);
        mMaskView= mRootView.findViewById(R.id.mask_view);
        mSlidingMenuLayout=  mRootView.findViewById(R.id.sidingMenuLayout);
        mSlidingMenuLayout.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelClosed(View view) {
                // 通知menu onClose
                mActivity.notifyMenuOnClose();
                if (mActivity.getCustomFragmentManager().size() <= 1) {
                    mActivity.getCustomActionBar().displayHomeIndicator();
                } else {
                    mActivity.getCustomActionBar().displayUpIndicator();
                }

            }

            @Override
            public void onPanelOpened(View view) {
                // 通知menu onClose
                mActivity.notifyMenuOnClose();
                mActivity.getCustomActionBar().displayUpIndicator();
            }

            @Override
            public void onPanelSlide(View view, float slideOffset) {
                mActivity.getCustomActionBar().displayIndicator(slideOffset);
            }

        });
    }

    @Override
    public ViewGroup getRootView() {
        return mRootView;
    }

    @Override
    public ViewGroup getMenuView() {
        return mSlidingMenuLayout.getMenuView();
    }

    @Override
    public ViewGroup getContentView() {
        return mSlidingMenuLayout.getContentView();
    }

    @Override
    public void setContentView(View v) {
        mSlidingMenuLayout.setContentView(v);
    }

    @Override
    public int getMenuFrameId() {
        return mSlidingMenuLayout.getMenuFrameId();
    }

    @Override
    public void showMenu(boolean show) {
        mSlidingMenuLayout.showMenu(show);
    }

    @Override
    public boolean isShowMenu() {
        return mSlidingMenuLayout.isShowMenu();
    }

    @Override
    public void setMenuEnable(boolean enable) {
        mSlidingMenuLayout.setMenuEnable(enable);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /**
         if (keyCode == KeyEvent.KEYCODE_BACK
         && mSlidingMenuLayout.isShowMenu()) {
         mSlidingMenuLayout.showMenu(false);
         return false;
         } **/
        if (keyCode == KeyEvent.KEYCODE_BACK
                && mSlidingMenuLayout.isShowMenu()) {
            mSlidingMenuLayout.showMenu(false);
            return true;
        }

        return false;
    }

    @Override
    public void attachToActivity(Activity activity) {
        mSlidingMenuLayout.attachToActivity(activity, ((SlidingNavigationFragmentActivity) activity).isFloatActionBarEnabled());
    }

    @Override
    public void setBackgroundColor(int color) {
        mSlidingMenuLayout.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resId) {
        mSlidingMenuLayout.setBackgroundResource(resId);
    }

    @Override
    public FrameLayout getMaskView() {
        return mMaskView;
    }

    @Override
    public void displayMaskView(boolean show) {mMaskView.setVisibility(show?View.VISIBLE:View.GONE);
    }

}
