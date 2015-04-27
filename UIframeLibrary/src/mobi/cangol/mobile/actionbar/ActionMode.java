package mobi.cangol.mobile.actionbar;


public abstract class ActionMode {
	
	public abstract void setTitle(CharSequence title);
	
	public abstract void setTitle(int resId);
	
	public abstract CharSequence getTitle();
	
	public abstract void start(Callback callback);	
	  
	public abstract void finish();

    public abstract ActionMenuInflater getMenuInflater();
    
    public interface Callback {
    	
        public void onCreateActionMode(ActionMode mode,ActionMenu actionMenu);

        public boolean onActionItemClicked(ActionMode mode, ActionMenuItem menuItem);

        public void onDestroyActionMode(ActionMode mode);
    }

	public abstract boolean isActionMode();

}
