package mobi.cangol.mobile.navigation;

import mobi.cangol.mobile.R;
import mobi.cangol.mobile.actionbar.ActionBarDrawerToggle;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.logging.Log;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class SlidingNavigationFragmentActivity extends
		BaseNavigationFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setNavigationFragmentActivityDelegate(new SlidingMenuNavigationFragmentActivityDelegate(this));
		super.onCreate(savedInstanceState);
	}

	@Override
	abstract public void findViews();

	@Override
	abstract public void initViews(Bundle savedInstanceState);

	@Override
	abstract public void initData(Bundle savedInstanceState);

}

class SlidingMenuNavigationFragmentActivityDelegate extends
		AbstractNavigationFragmentActivityDelegate {
	private BaseNavigationFragmentActivity mActivity;
	private FrameLayout mMenuView;
	private FrameLayout mContentView;
	private DrawerLayout mRootView;

	public SlidingMenuNavigationFragmentActivityDelegate(
			BaseNavigationFragmentActivity activity) {
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mRootView = (DrawerLayout) LayoutInflater.from(mActivity).inflate(
				R.layout.slidingmenu_main, null);
		mContentView = (FrameLayout) mRootView.findViewById(R.id.content_frame);
		mMenuView = (FrameLayout) mRootView.findViewById(R.id.menu_frame);
		mRootView.setDrawerListener(new ActionBarDrawerToggle(mActivity,
				mRootView,R.drawable.actionbar_home_indicator) {

			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				Log.d("onDrawerClosed");
				if (!mRootView.isDrawerOpen(Gravity.LEFT)) {
					if (mActivity.getCustomFragmentManager().size() <= 1) {
						mRootView.setDrawerLockMode(Gravity.LEFT,
								DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
					} else {
						mRootView.setDrawerLockMode(Gravity.LEFT,
								DrawerLayout.LOCK_MODE_UNLOCKED);
						mActivity.getCustomActionBar().displayUpIndicator();
					}
				}
				// 通知menu onClose
				mActivity.notifyMenuOnClose();
			}

			@Override
			public void onDrawerOpened(View view) {
				super.onDrawerOpened(view);
				Log.d("onDrawerOpened");
				// 通知menu onOpen
				mActivity.notifyMenuOnOpen();
			}

		});
	}

	@Override
	public int getMenuFrameId() {
		return R.id.menu_frame;
	}

	@Override
	public int getContentFrameId() {
		return R.id.content_frame;
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
			mRootView.openDrawer(Gravity.LEFT);
		} else {
			mRootView.closeDrawer(Gravity.LEFT);
		}
	}

	@Override
	public boolean isShowMenu() {
		return mRootView.isDrawerOpen(Gravity.LEFT);
	}

	@Override
	public void setMenuEnable(boolean enable) {
		mRootView.setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED
				: DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& mRootView.isDrawerOpen(Gravity.LEFT)) {
			mRootView.closeDrawer(Gravity.LEFT);
			return true;
		}
		return false;
	}

	@Override
	public BaseNavigationFragmentActivity getActivity() {
		return mActivity;
	}

}
