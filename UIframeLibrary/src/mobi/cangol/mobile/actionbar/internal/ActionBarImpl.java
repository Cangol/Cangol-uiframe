package mobi.cangol.mobile.actionbar.internal;

import java.util.ArrayList;

import mobi.cangol.mobile.actionbar.ActionBar;
import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.actionbar.ActionMode;
import mobi.cangol.mobile.actionbar.ActionMode.Callback;
import mobi.cangol.mobile.actionbar.OnNavigationListener;
import mobi.cangol.mobile.actionbar.view.ActionBarView;
import mobi.cangol.mobile.actionbar.view.SearchView;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

public class ActionBarImpl extends ActionBar{
   	private ActionBarView mActionBarView;
   	
    public ActionBarImpl(ActionBarView view){
    	this.mActionBarView=view;
    }

	@Override
	public void setListNavigationCallbacks(BaseAdapter adapter,
			OnNavigationListener onNavigationListener) {
		mActionBarView.setListNavigationCallbacks(adapter,onNavigationListener);		
	}

	@Override
	public void clearListNavigation() {
		mActionBarView.clearListNavigation();
	}

	@Override
	public void setDisplayHomeAsUpEnabled(boolean show) {
		mActionBarView.setDisplayHomeAsUpEnabled(show);
	}

	@Override
	public void displayIndicator(float slideOffset){
		mActionBarView.displayIndicator(slideOffset);
	}

	@Override
	public void displayHomeIndicator() {
		mActionBarView.displayHomeIndicator();
	}

	@Override
	public void displayUpIndicator() {
		mActionBarView.displayUpIndicator();
	}

	@Override
	public String getTitle() {
		return mActionBarView.getTitle();
	}

	@Override
	public void setTitle(CharSequence title) {
		mActionBarView.setTitle(title);
	}

	@Override
	public void setTitle(int resid) {
		mActionBarView.setTitle(resid);
	}

	@Override
	public void setTitleGravity(int gravity) {
		mActionBarView.setTitleGravity(gravity);
	}

	@Override
	public void setOnTitleClickListener(OnClickListener listener) {
		mActionBarView.setOnTitleClickListener(listener);
	}

	@Override
	public ActionMode startActionMode(Callback callback) {
		return mActionBarView.startActionMode(callback);
	}

	@Override
	public void stopActionMode() {
		mActionBarView.stopActionMode();
		
	}

	@Override
	public void startProgress() {
		mActionBarView.startProgress();
		
	}

	@Override
	public void stopProgress() {
		mActionBarView.stopProgress();
	}

	@Override
	public SearchView startSearchMode() {
		// TODO Auto-generated method stub
		return mActionBarView.startSearchMode();
	}

	@Override
	public void stopSearchMode() {
		mActionBarView.stopSearchMode();
	}

	@Override
	public ActionMenu getActionMenu() {
		return mActionBarView.getActionMenu();
	}

	@Override
	public void clearActions() {
		mActionBarView.clearActions();
	}

	@Override
	public ArrayList<ActionMenuItem> getActions() {
		return mActionBarView.getActions();
	}

	@Override
	public void addActions(ArrayList<ActionMenuItem> actions) {
		mActionBarView.addActions(actions);
	}

	@Override
	public boolean onBackPressed() {
		return mActionBarView.onBackPressed();
	}

	@Override
	public void setShow(boolean show) {
		mActionBarView.setVisibility(show?View.VISIBLE:View.GONE);
		
	}
	public void setBackgroundColor(int color){
		mActionBarView.setBackgroundColor(color);
	}
	
	public void setBackgroundResource(int resId){
		mActionBarView.setBackgroundResource(resId);
	}

	@Override
	public boolean isShow() {
		return mActionBarView.getVisibility()==View.VISIBLE;
	}
}