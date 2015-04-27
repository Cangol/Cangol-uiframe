package mobi.cangol.mobile.actionbar.internal;

import java.util.ArrayList;

import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.actionbar.view.ActionMenuView;
import mobi.cangol.mobile.actionbar.view.ActionMenuView.OnActionClickListener;

public class ActionMenuImpl implements ActionMenu{
	private ArrayList<ActionMenuItem> mActions=new ArrayList<ActionMenuItem>();
	private ActionMenuView mActionMenuView;
	
	public ActionMenuImpl(ActionMenuView view){
		this.mActionMenuView=view;
		this.mActionMenuView.setActionMenu(this);
	}
	
	@Override
	public ArrayList<ActionMenuItem> getActions() {
		return mActions;
	}

	@Override
	public void add(ActionMenuItem action) {
		mActionMenuView.addAction(action);
		mActions.add(action);
	}

	@Override
	public void addActions(ArrayList<ActionMenuItem> actions) {
		mActionMenuView.addActions(actions);
		mActions.addAll(actions);
	}

	@Override
	public void clear() {
		mActionMenuView.removeAllActions();
		mActions.clear();
	}

	@Override
	public int size() {
		return mActions.size();
	}

	@Override
	public ActionMenuItem getAction(int index) {
		return mActions.get(index);
	}

	@Override
	public void setOnActionClickListener(
			OnActionClickListener onActionClickListener) {
		mActionMenuView.setOnActionClickListener(onActionClickListener);
		
	}

}
