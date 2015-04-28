package mobi.cangol.mobile.actionbar;

import java.util.ArrayList;

import mobi.cangol.mobile.actionbar.ActionMode.Callback;
import mobi.cangol.mobile.actionbar.view.SearchView;
import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

 public abstract class ActionBar {

	abstract public void setListNavigationCallbacks(BaseAdapter adapter,
			OnNavigationListener onNavigationListener);

	abstract public void clearListNavigation();

	abstract public void setDisplayHomeAsUpEnabled(boolean show);

	abstract public void setActionBarIndicator(int homeIndicator, int upIndicator);

	abstract public void displayHomeIndicator();

	abstract public void displayUpIndicator();

	abstract public void setActionBarUpIndicator(int resId);

	abstract public void setActionBarUpIndicator(Drawable drawable);

	abstract public String getTitle();

	abstract public void setTitle(CharSequence title);

	abstract public void setTitle(int resid);

	abstract public void setTitleGravity(int gravity);

	abstract public void setOnTitleClickListener(OnClickListener listener);

	abstract public ActionMode startActionMode(Callback callback);

	abstract public void stopActionMode();

	abstract public void startProgress();

	abstract public void stopProgress();

	abstract public SearchView startSearchMode();

	abstract public void stopSearchMode();

	abstract public ActionMenu getActionMenu();

	abstract public void clearActions();

	abstract public ArrayList<ActionMenuItem> getActions();

	abstract public void addActions(ArrayList<ActionMenuItem> actions);

	abstract public boolean onBackPressed();

	abstract public void setShow(boolean show);
	
	abstract public void setBackgroundColor(int color);
	
	abstract public void setBackgroundResource(int resId);
}
