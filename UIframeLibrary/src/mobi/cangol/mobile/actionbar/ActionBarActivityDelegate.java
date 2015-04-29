package mobi.cangol.mobile.actionbar;

import java.util.ArrayList;

import mobi.cangol.mobile.R;
import mobi.cangol.mobile.actionbar.internal.ActionBarImpl;
import mobi.cangol.mobile.actionbar.view.ActionBarView;
import mobi.cangol.mobile.logging.Log;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class ActionBarActivityDelegate {
	
	private ActionBarActivity mActivity;
	private ViewGroup mContainerView;
	private ActionBar mActionBar;
	private FrameLayout	mContentView;
	private ActionMenuInflater mActionMenuInflater;
	private boolean mActionbarOverlay = false;
	
	public ActionBarActivityDelegate(ActionBarActivity activity) {
		mActivity = activity;
	}
	
	protected void onCreate(Bundle savedInstanceState){
		Log.d("onCreate");
		mContainerView =  (ViewGroup) LayoutInflater.from(mActivity).inflate(R.layout.actionbar_activity_main, null);
		mContentView=(FrameLayout) mContainerView.findViewById(R.id.content_view);
		mActionBar= new ActionBarImpl((ActionBarView) mContainerView.findViewById(R.id.actionbar));
	}
	
	public ActionMenuInflater getActionMenuInflater() {
        if (mActionMenuInflater == null) {
            ActionBar ab = getCustomActionBar();
            mActionMenuInflater = new ActionMenuInflater(ab.getActionMenu(),mActivity);
        }
        return mActionMenuInflater;
    }
	public void setActionBarUpIndicator(int resId){
		mActionBar.setActionBarUpIndicator(resId);
	}
	public boolean isActionbarOverlay() {
		return mActionbarOverlay;
	}
	
	public void setActionbarOverlay(boolean mActionbarOverlay) {
		this.mActionbarOverlay = mActionbarOverlay;
		DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics(); 
		if(mActionbarOverlay){
			((RelativeLayout.LayoutParams)mContentView.getLayoutParams()).topMargin=0;
		}else {
			((RelativeLayout.LayoutParams)mContentView.getLayoutParams()).topMargin=(int) (48*displayMetrics.density);
		}
		this.mActionbarOverlay = mActionbarOverlay;
	}
	public void setActionbarShow(boolean show) {
		mActionBar.setShow(show);
	}
	public ActionBar getCustomActionBar() {
		return mActionBar;
	}
	public void setBackgroundColor(int color){
		mContainerView.setBackgroundColor(color);
	}
	
	public void setBackgroundResource(int resId){
		mContainerView.setBackgroundResource(resId);
	}
	protected void onPostCreate(Bundle savedInstanceState){
		Log.d("onPostCreate");
		attachToActivity(mActivity,mContainerView);
		if(savedInstanceState!=null){
			String title=savedInstanceState.getString("ActionBar.title");
			mActionBar.setTitle(title);
			ArrayList<ActionMenuItem> actions=savedInstanceState.getParcelableArrayList("ActionBar.actions");
			mActionBar.clearActions();
			mActionBar.addActions(actions);
		}else{
			mActivity.onMenuActionCreated(mActionBar.getActionMenu());
		}
	}

	private void attachToActivity(Activity activity,View layout) {
		// get the window background
		TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});
		int background = a.getResourceId(0, 0);
		a.recycle();	
		
		//如果已设置过颜色 则不继承theme颜色
		if(mContentView.getBackground()==null){
			mContentView.setBackgroundResource(background);
		}
		
		ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
		
		decor.removeView(decorChild);
		decor.addView(layout);
		setContent(decorChild);
	}
	public void setContent(View view) {
		setContentView(view);
	}
	
	public void setContentView(View v) {
		mContentView.removeAllViews();
		mContentView.addView(v);
	}
	
	public void setContentView(View v,LayoutParams params) {
		mContentView.removeAllViews();
		mContentView.addView(v,params);
	}
	
	public View findViewById(int id) {
		View v;
		if (mContainerView != null) {
			v = mContainerView.findViewById(id);
			if (v != null)
				return v;
		}
		return null;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) {
			return mActionBar.onBackPressed();
		}
		return false;
	}

	public void onSaveInstanceState(Bundle outState) {
		outState.putString("ActionBar.title", mActionBar.getTitle());
		outState.putParcelableArrayList("ActionBar.actions", mActionBar.getActions());
	}

	public void setTitle(int titleId) {
		mActionBar.setTitle(titleId);
	}

	public void setTitle(CharSequence title) {
		mActionBar.setTitle(title);
	}

	public void setActionBarIndicator(int actionbar_home_indicator,
			int actionbar_up_indicator) {
		mActionBar.setActionBarIndicator(actionbar_home_indicator,actionbar_up_indicator);
	}
	
}
