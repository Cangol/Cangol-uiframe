package mobi.cangol.mobile.navigation;

import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;


public abstract class AbstractNavigationFragmentActivityDelegate {
	
	public abstract void onCreate(Bundle savedInstanceState);
	
	public abstract ViewGroup getRootView();
	
	public abstract ViewGroup getMenuView();
	
	public abstract ViewGroup getContentView();
	
	public abstract void setContentView(View v);

	public abstract int getMenuFrameId() ;
	
	public abstract void onConfigurationChanged(Configuration newConfig);
	
	public abstract void showMenu(boolean show);
	
	public abstract boolean isShowMenu();
	
	public abstract void setMenuEnable(boolean enable);

	public abstract boolean onKeyUp(int keyCode, KeyEvent event);
	
	public abstract BaseNavigationFragmentActivity getActivity();
	
	public abstract void attachToActivity(Activity activity);
	
	
}