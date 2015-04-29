package mobi.cangol.mobile.navigation;

import mobi.cangol.mobile.R;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.logging.Log;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;


public abstract class AbstractNavigationFragmentActivityDelegate {
	
	public static final String MENU_SHOW="MENU_SHOW";
	
	public abstract void onCreate(Bundle savedInstanceState);
	
	public abstract ViewGroup getRootView();
	
	public abstract ViewGroup getMenuView();
	
	public abstract ViewGroup getContentView();
	
	public abstract void setContentView(View v);

	public abstract int getMenuFrameId() ;
	
	public abstract int getContentFrameId();
	
	public abstract void onConfigurationChanged(Configuration newConfig);
	
	public abstract void showMenu(boolean show);
	
	public abstract boolean isShowMenu();
	
	public abstract void setMenuEnable(boolean enable);

	public abstract boolean onKeyUp(int keyCode, KeyEvent event);
	
	public abstract BaseNavigationFragmentActivity getActivity();
	
	public  View findViewById(int id){
		View v;
		if (getRootView() != null) {
			v = getRootView().findViewById(id);
			if (v != null)
				return v;
		}
		return null;
	}

	public void onPostCreate(Bundle savedInstanceState){
		
		attachToActivity(getActivity(),getRootView());
		if(savedInstanceState!=null){
			boolean show=savedInstanceState.getBoolean(MENU_SHOW);
			showMenu(show);
		}
	}

	public  void onSaveInstanceState(Bundle outState){
		outState.putBoolean(MENU_SHOW, isShowMenu());
	}
	
	public void attachToActivity(Activity activity,View layout) {
		// get the window background
		TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});
		int background = a.getResourceId(0, 0);
		a.recycle();
		
		ViewGroup contentView = (ViewGroup)getActivity().findViewById(R.id.content_view);
		Log.d("contentView "+contentView.getBackground());
		if (contentView.getBackground() == null)
			contentView.setBackgroundResource(background);
		
		ViewGroup contentParent = (ViewGroup)getActivity().findViewById(android.R.id.content);
		ViewGroup content = (ViewGroup) contentParent.getChildAt(0);
		
		contentParent.removeView(content);
		contentParent.addView(layout);
		getContentView().addView(content);
		
	}
}