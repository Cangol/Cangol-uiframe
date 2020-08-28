package mobi.cangol.mobile.navigation;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mobi.cangol.mobile.base.BaseFragment;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.R;

/**
 * Created by xuewu.wei on 2017/9/19.
 */

public abstract class TabDrawerNavigationFragmentActivity extends BaseNavigationFragmentActivity {

    public static final String GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE = "getActivity is TabDrawerNavigationFragmentActivityDelegate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setNavigationFragmentActivityDelegate(new TabDrawerNavigationFragmentActivityDelegate(
                this));
        super.onCreate(savedInstanceState);
        this.getCustomActionBar().setTitleGravity(Gravity.CENTER);
        this.getCustomActionBar().setDisplayShowHomeEnabled(false);
        //默认是不能滑动的
        this.setDrawerEnable(Gravity.LEFT, false);
        this.setDrawerEnable(Gravity.RIGHT, false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (stack.size() <= 1) {
            stack.peek().onSupportNavigateUp();
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }

    public void setDrawerEnable(int gravity, boolean enable) {
        if (getNavigationFragmentActivityDelegate() instanceof TabDrawerNavigationFragmentActivityDelegate) {
            ((TabDrawerNavigationFragmentActivityDelegate) getNavigationFragmentActivityDelegate()).setDrawerEnable(gravity, enable);
        } else {
            throw new IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE);
        }
    }

    public void showDrawer(int gravity, boolean show) {
        if (getNavigationFragmentActivityDelegate() instanceof TabDrawerNavigationFragmentActivityDelegate) {
            ((TabDrawerNavigationFragmentActivityDelegate) getNavigationFragmentActivityDelegate()).showDrawer(gravity, show);
        } else {
            throw new IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE);
        }
    }

    public boolean isShowDrawer(int gravity) {
        if (getNavigationFragmentActivityDelegate() instanceof TabDrawerNavigationFragmentActivityDelegate) {
            return ((TabDrawerNavigationFragmentActivityDelegate) getNavigationFragmentActivityDelegate()).isShowDrawer(gravity);
        } else {
            throw new IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE);
        }
    }

    public void setDrawer(int gravity, Class<? extends BaseFragment> fragmentClass, Bundle args) {
        BaseFragment drawerFragment = (BaseFragment) Fragment.instantiate(this, fragmentClass.getName(), args);
        if (getNavigationFragmentActivityDelegate() instanceof TabDrawerNavigationFragmentActivityDelegate) {
            ((TabDrawerNavigationFragmentActivityDelegate) getNavigationFragmentActivityDelegate()).setDrawer(gravity, drawerFragment);
        } else {
            throw new IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE);
        }
    }

    public BaseFragment getDrawer(int gravity) {
        if (getNavigationFragmentActivityDelegate() instanceof TabDrawerNavigationFragmentActivityDelegate) {
            return ((TabDrawerNavigationFragmentActivityDelegate) getNavigationFragmentActivityDelegate()).getDrawer(gravity);
        } else {
            throw new IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE);
        }
    }

    public void removeDrawer(int gravity) {
        if (getNavigationFragmentActivityDelegate() instanceof TabDrawerNavigationFragmentActivityDelegate) {
            ((TabDrawerNavigationFragmentActivityDelegate) getNavigationFragmentActivityDelegate()).removeDrawer(gravity);
        } else {
            throw new IllegalStateException(GET_ACTIVITY_IS_TAB_DRAWER_NAVIGATION_FRAGMENT_ACTIVITY_DELEGATE);
        }
    }
}

class TabDrawerNavigationFragmentActivityDelegate extends AbstractNavigationFragmentActivityDelegate {

    private BaseNavigationFragmentActivity mActivity;
    private TabMenuDrawerLayout mDrawerLayout;
    private FrameLayout mMenuView;
    private FrameLayout mContentView;

    public TabDrawerNavigationFragmentActivityDelegate(
            BaseNavigationFragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDrawerLayout = (TabMenuDrawerLayout) LayoutInflater.from(mActivity).inflate(
                R.layout.navigation_tab_drawer_main, null);

        mContentView = (FrameLayout) mDrawerLayout.findViewById(R.id.content_view);
        mMenuView = (FrameLayout) mDrawerLayout.findViewById(R.id.menu_view);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Fragment fragment = mActivity.getSupportFragmentManager().findFragmentById(drawerView.getId());
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onDrawerSlide(slideOffset);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Fragment fragment = mActivity.getSupportFragmentManager().findFragmentById(drawerView.getId());
                if (fragment instanceof BaseFragment) {
                    fragment.setUserVisibleHint(true);
                    ((BaseFragment) fragment).onDrawerOpened();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Fragment fragment = mActivity.getSupportFragmentManager().findFragmentById(drawerView.getId());
                if (fragment instanceof BaseFragment) {
                    fragment.setUserVisibleHint(false);
                    ((BaseFragment) fragment).onDrawerClosed();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //do nothings
            }
        });
    }

    public void setDrawerEnable(int gravity, boolean enable) {
        mDrawerLayout.setDrawerEnable(gravity, enable);
    }
    public FrameLayout getMaskView() {
        return mDrawerLayout.getMaskView();
    }

    public void displayMaskView(boolean show) {
        mDrawerLayout.displayMaskView(show);
    }
    public void showDrawer(int gravity, boolean show) {
        mDrawerLayout.showDrawer(gravity, show);
    }

    public boolean isShowDrawer(int gravity) {
        return mDrawerLayout.isShowDrawer(gravity);
    }

    @Override
    public int getMenuFrameId() {
        return mMenuView.getId();
    }

    @Override
    public ViewGroup getRootView() {
        return mDrawerLayout;
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isShowDrawer(Gravity.LEFT)) {
                mDrawerLayout.showDrawer(Gravity.LEFT, false);
                return true;
            } else if (mDrawerLayout.isShowDrawer(Gravity.RIGHT)) {
                mDrawerLayout.showDrawer(Gravity.RIGHT, false);
                return true;
            }
        }
        return false;
    }

    @Override
    public void attachToActivity(Activity activity) {
        mDrawerLayout.attachToActivity(activity, ((TabDrawerNavigationFragmentActivity) activity).isFloatActionBarEnabled());
    }

    @Override
    public BaseNavigationFragmentActivity getActivity() {
        return mActivity;
    }

    @Override
    public void setBackgroundColor(int color) {
        mDrawerLayout.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resId) {
        mDrawerLayout.setBackgroundResource(resId);
    }

    public void setDrawer(int gravity, BaseFragment fragment) {
        fragment.setUserVisibleHint(false);
        FragmentTransaction t = mActivity.getSupportFragmentManager()
                .beginTransaction();
        t.replace(gravity == Gravity.LEFT ? R.id.left_view : R.id.right_view, fragment, "DrawerFragment_" + gravity);
        t.commitAllowingStateLoss();
        mActivity.getSupportFragmentManager().executePendingTransactions();
    }

    public BaseFragment getDrawer(int gravity) {
        Fragment fragment = mActivity.getSupportFragmentManager().findFragmentById(gravity == Gravity.LEFT ? R.id.left_view : R.id.right_view);
        if(fragment!=null){
            return (BaseFragment) fragment;
        }else{
            return null;
        }
    }

    public void removeDrawer(int gravity) {
        Fragment fragment = mActivity.getSupportFragmentManager().findFragmentById(gravity == Gravity.LEFT ? R.id.left_view : R.id.right_view);
        if(fragment!=null){
            FragmentTransaction t = mActivity.getSupportFragmentManager()
                    .beginTransaction();
            t.remove(fragment);
            t.commitAllowingStateLoss();
            mActivity.getSupportFragmentManager().executePendingTransactions();
        }
    }

}


