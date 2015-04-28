package mobi.cangol.mobile.actionbar;

import mobi.cangol.mobile.R;
import mobi.cangol.mobile.actionbar.ActionMode.Callback;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class ActionBarActivity extends FragmentActivity{
	private ActionBarActivityDelegate mDelegate;
	private SystemBarTintManager mTintManager;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDelegate = new ActionBarActivityDelegate(this);
		mDelegate.onCreate(savedInstanceState);
		mTintManager = new SystemBarTintManager(this);
		mDelegate.setActionBarIndicator(R.drawable.actionbar_home_indicator,R.drawable.actionbar_up_indicator);
	}
	@TargetApi(19) 
	public void setStatusBarTintColor(int colorId){
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(colorId);
	}
	@TargetApi(19) 
	public void setNavigationBarTintColor(int colorId){
		mTintManager.setNavigationBarTintEnabled(true);
		mTintManager.setNavigationBarTintColor(colorId);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mDelegate.setTitle(title);
	}
	@Override
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		mDelegate.setTitle(titleId);
	}
	public void setBackgroundColor(int color){
		mDelegate.setBackgroundColor(color);
	}
	
	public void setBackgroundResource(int resId){
		mDelegate.setBackgroundResource(resId);
	}
	@TargetApi(19) 
	public void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDelegate.onPostCreate(savedInstanceState);
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null)
			return v;
		return mDelegate.findViewById(id);
	}
	public boolean isActionbarOverlay() {
		return mDelegate.isActionbarOverlay();
	}
	public void setActionbarOverlay(boolean mActionbarOverlay) {
		this.mDelegate.setActionbarOverlay(mActionbarOverlay);
	}
	public void setActionbarShow(boolean show) {
		if(show){
			this.mDelegate.setActionbarOverlay(false);
		}else{
			this.mDelegate.setActionbarOverlay(true);
		}
		this.mDelegate.setActionbarShow(show);
		
	}

	public ActionMenuInflater getActionMenuInflater() {
        return mDelegate.getActionMenuInflater();
	}
	public void onMenuActionCreated(ActionMenu actionMenu) {
		
	}
	
	public boolean onMenuActionSelected(ActionMenuItem actionMenu) {
		return false;
	}
	protected boolean dispatchActionSelected(ActionMenuItem actionMenu) {
		if(onMenuActionSelected(actionMenu)){
			return true;
		}else{
			return dispatchFragmentActionSelected(actionMenu);
		}
	}
	protected boolean dispatchFragmentActionSelected(ActionMenuItem actionMenu) {
		return false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mDelegate.onSaveInstanceState(outState);
	}
	public ActionBar getCustomActionBar(){
		return mDelegate.getCustomActionBar();
	}
	public ActionMode startCustomActionMode(Callback callback){
		return getCustomActionBar().startActionMode(callback);
	}
	public boolean onSupportNavigateUp() {
		this.onBackPressed();
		return true;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean b = mDelegate.onKeyUp(keyCode, event);
		if (b) return b;
		return super.onKeyUp(keyCode, event);
	}
	public void setFullScreen(boolean fullscreen) {
		if(fullscreen){
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
	}
}
