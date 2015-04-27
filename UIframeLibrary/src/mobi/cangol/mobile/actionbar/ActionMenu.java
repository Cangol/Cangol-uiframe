package mobi.cangol.mobile.actionbar;

import java.util.ArrayList;

import mobi.cangol.mobile.actionbar.view.ActionMenuView.OnActionClickListener;

public interface ActionMenu {
	
	ArrayList<ActionMenuItem>  getActions();
	
	void add(ActionMenuItem action);
	
	void addActions(ArrayList<ActionMenuItem> actions);
	
	void clear();
	
	int size();
	
	ActionMenuItem getAction(int index);

	void setOnActionClickListener(OnActionClickListener onActionClickListener);	
}
