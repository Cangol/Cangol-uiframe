package mobi.cangol.mobile.navigation;

import mobi.cangol.mobile.R;
import mobi.cangol.mobile.actionbar.view.DrawerArrowDrawable;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class SlidingNavigationFragmentActivity extends
		BaseNavigationFragmentActivity {
	private boolean mFloatActionBarEnabled;
	public void setFloatActionBarEnabled(boolean floatActionBarEnabled) {
		mFloatActionBarEnabled = floatActionBarEnabled;
	}
	
	public boolean isFloatActionBarEnabled() {
		return mFloatActionBarEnabled;
	}
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
	private SlidingMenuLayout mSlidingMenuLayout;
	@Override
	public BaseNavigationFragmentActivity getActivity() {
		return mActivity;
	}
	public SlidingMenuNavigationFragmentActivityDelegate(
			BaseNavigationFragmentActivity activity) {
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mSlidingMenuLayout = (SlidingMenuLayout) LayoutInflater.from(mActivity).inflate(R.layout.navigation_sliding_main, null);
		mSlidingMenuLayout.setPanelSlideListener(new PanelSlideListener(){
			@Override
			public void onPanelClosed(View view) {
				// 通知menu onClose
				mActivity.notifyMenuOnClose();
				if (mActivity.getCustomFragmentManager().size() <= 1) {
					mActivity.getCustomActionBar().displayHomeIndicator();
				}else{
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
		return mSlidingMenuLayout;
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
	public void onConfigurationChanged(Configuration newConfig) {
		mSlidingMenuLayout.onConfigurationChanged(newConfig);
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
				&& !mSlidingMenuLayout.isShowMenu()) {
			mSlidingMenuLayout.showMenu(true);
			return true;
		}
		return false;
	}
	@Override
	public void attachToActivity(Activity activity) {
		mSlidingMenuLayout.attachToActivity(activity,((SlidingNavigationFragmentActivity)activity).isFloatActionBarEnabled());
	}
	@Override
	public void setBackgroundColor(int color) {
		mSlidingMenuLayout.setBackgroundColor(color);
	}
	@Override
	public void setBackgroundResource(int resId) {
		mSlidingMenuLayout.setBackgroundResource(resId);
	}
	
}
