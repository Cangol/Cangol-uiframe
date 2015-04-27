package mobi.cangol.mobile.actionbar.internal;

import mobi.cangol.mobile.actionbar.ActionMenuInflater;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.actionbar.ActionMode;
import mobi.cangol.mobile.actionbar.view.ActionMenuView.OnActionClickListener;
import mobi.cangol.mobile.actionbar.view.ActionModeView;
import android.app.Activity;
import android.view.View;

public class ActionModeImpl extends ActionMode {
	private ActionModeView mActionModeView;
	private ActionMode.Callback mActionModeCallback;
	private boolean isActionMode;
	private Activity mContext;
	public ActionModeImpl(Activity context, ActionModeView view){
		mContext=context;
		mActionModeView=view;
		mActionModeView.setActionMode(this);
	}
	@Override
	public void setTitle(CharSequence title) {
		mActionModeView.setTitle(title);
	}

	@Override
	public void setTitle(int resId) {
		mActionModeView.setTitle(resId);
	}

	@Override
	public CharSequence getTitle() {
		return mActionModeView.getTitle();
	}
	
	@Override
	public void finish() {
		mActionModeView.getActionMenu().clear();
		mActionModeView.setVisibility(View.GONE);
		if(mActionModeCallback!=null){
			mActionModeCallback.onDestroyActionMode(this);
		}
		isActionMode=false;
	}
	@Override
	public void start(Callback callback){
		mActionModeView.setVisibility(View.VISIBLE);
		mActionModeCallback=callback;
		if(mActionModeCallback!=null){
			mActionModeCallback.onCreateActionMode(this,mActionModeView.getActionMenu());
		}
		mActionModeView.setOnActionClickListener(new OnActionClickListener(){

			@Override
			public boolean onActionClick(ActionMenuItem action) {
				if(mActionModeCallback!=null){
					return mActionModeCallback.onActionItemClicked(ActionModeImpl.this,action);
				}
				return false;
			}
			
		});
		
		isActionMode=true;
	}

	@Override
	public ActionMenuInflater getMenuInflater() {
		return new ActionMenuInflater(mActionModeView.getActionMenu(),mContext);
	}
	
	@Override
	public boolean isActionMode() {
		return isActionMode;
	}
}
