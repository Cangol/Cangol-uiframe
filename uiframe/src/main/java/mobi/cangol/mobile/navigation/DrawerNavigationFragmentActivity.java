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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.uiframe.R;

public abstract class DrawerNavigationFragmentActivity extends BaseNavigationFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setNavigationFragmentActivityDelegate(new DrawerMenuNavigationFragmentActivityDelegate(this));
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

class DrawerMenuNavigationFragmentActivityDelegate extends
        AbstractNavigationFragmentActivityDelegate {

    private BaseNavigationFragmentActivity mActivity;
    private DrawerMenuLayout mDrawerMenuLayout;

    public DrawerMenuNavigationFragmentActivityDelegate(
            BaseNavigationFragmentActivity activity) {
        mActivity = activity;

    }

    @Override
    public BaseNavigationFragmentActivity getActivity() {
        return mActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDrawerMenuLayout = (DrawerMenuLayout) LayoutInflater.from(mActivity).inflate(R.layout.navigation_drawer_main, null);
        mDrawerMenuLayout.setDrawerListener(new DrawerListener() {

            @Override
            public void onDrawerClosed(View view) {
                Log.d("onDrawerClosed");
                if (mActivity.getCustomFragmentManager().size() <= 1) {
                    mDrawerMenuLayout.setDrawerLockMode(Gravity.LEFT, DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    mActivity.getCustomActionBar().displayHomeIndicator();
                } else {
                    mDrawerMenuLayout.setDrawerLockMode(Gravity.LEFT, DrawerLayout.LOCK_MODE_UNLOCKED);
                    mActivity.getCustomActionBar().displayUpIndicator();
                }
                // 通知menu onClose
                mActivity.notifyMenuOnClose();
            }

            @Override
            public void onDrawerOpened(View view) {
                Log.d("onDrawerOpened");
                // 通知menu onOpen
                mActivity.notifyMenuOnOpen();
                mActivity.getCustomActionBar().displayUpIndicator();
            }

            @Override
            public void onDrawerSlide(View view, float slideOffset) {
                mActivity.getCustomActionBar().displayIndicator(slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int arg0) {
                //do nothings
            }

        });
    }

    @Override
    public ViewGroup getRootView() {
        return mDrawerMenuLayout;
    }

    @Override
    public ViewGroup getMenuView() {
        return mDrawerMenuLayout.getMenuView();
    }

    @Override
    public ViewGroup getContentView() {
        return mDrawerMenuLayout.getContentView();
    }

    @Override
    public void setContentView(View v) {
        mDrawerMenuLayout.setContentView(v);
    }

    @Override
    public int getMenuFrameId() {
        return mDrawerMenuLayout.getMenuFrameId();
    }

    @Override
    public void showMenu(boolean show) {
        mDrawerMenuLayout.showMenu(show);
    }

    @Override
    public boolean isShowMenu() {
        return mDrawerMenuLayout.isShowMenu();
    }

    @Override
    public void setMenuEnable(boolean enable) {
        mDrawerMenuLayout.setMenuEnable(enable);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && mDrawerMenuLayout.isShowMenu()) {
            mDrawerMenuLayout.showMenu(false);
            return true;
        }
        return false;
    }

    @Override
    public void attachToActivity(Activity activity) {
        mDrawerMenuLayout.attachToActivity(activity, ((DrawerNavigationFragmentActivity) activity).isFloatActionBarEnabled());
    }

    @Override
    public void setBackgroundColor(int color) {
        mDrawerMenuLayout.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resId) {
        mDrawerMenuLayout.setBackgroundResource(resId);
    }

    @Override
    public FrameLayout getMaskView() {
        return mDrawerMenuLayout.getMaskView();
    }

    @Override
    public void displayMaskView(boolean show) {
        mDrawerMenuLayout.displayMaskView(show);
    }
}
