package mobi.cangol.mobile.actionbar.view;

import java.util.ArrayList;

import mobi.cangol.mobile.R;
import mobi.cangol.mobile.actionbar.ActionBarActivity;
import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.actionbar.ActionMode;
import mobi.cangol.mobile.actionbar.ActionMode.Callback;
import mobi.cangol.mobile.actionbar.OnNavigationListener;
import mobi.cangol.mobile.actionbar.internal.ActionMenuImpl;
import mobi.cangol.mobile.actionbar.internal.ActionModeImpl;
import mobi.cangol.mobile.actionbar.view.ActionMenuView.OnActionClickListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActionBarView extends RelativeLayout {
	public static final String TAG    = "ActionBar";
    private LayoutInflater mInflater;
    private View mRootView;
    private ImageView mIndicator;
    private TextView mTitleView;
    private PopupWindow mPopuMenu;
    private ProgressView mProgressView;
    private SearchView mSearchView;
    private ActionMenu mActionMenu;
    private ActionMode  mActionMode;
    private ActionBarActivity mActionBarActivity;
    private boolean mIsSearchMode;
   	private DrawerArrowDrawable mDrawerArrowDrawable;
   	
    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActionBarActivity=(ActionBarActivity) context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        mDrawerArrowDrawable=new DrawerArrowDrawable(context.getResources(),false);
        mDrawerArrowDrawable.setStrokeColor(Color.WHITE);
        
        mInflater.inflate(R.layout.actionbar_layout, this);
        mRootView=this.findViewById(R.id.actionbar_main_layout);
        mIndicator = (ImageView) this.findViewById(R.id.actionbar_main_indicator);
        mTitleView = (TextView) this.findViewById(R.id.actionbar_main_title);
        mProgressView= (ProgressView) this.findViewById(R.id.actionbar_main_progress);
        mSearchView = (SearchView) this.findViewById(R.id.actionbar_main_search);
        mActionMenu=new ActionMenuImpl((ActionMenuView) this.findViewById(R.id.actionbar_main_menu));
        mActionMode=new ActionModeImpl(mActionBarActivity,(ActionModeView) this.findViewById(R.id.actionbar_main_mode));
        setTitle(context.getApplicationInfo().name);
        initListeners();
    }
    public void setBackgroundColor(int color){
    	mRootView.setBackgroundColor(color);
	}
	
	public void setBackgroundResource(int resId){
		mRootView.setBackgroundResource(resId);
	}
    private void initListeners(){
    	mIndicator.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mActionBarActivity!=null)
				mActionBarActivity.onSupportNavigateUp();
			}
    		
    	});
    	mActionMenu.setOnActionClickListener(new OnActionClickListener(){

			@Override
			public boolean onActionClick(ActionMenuItem action) {
				if(mActionBarActivity!=null){
					return mActionBarActivity.onMenuActionSelected(action);
				}
				return false;
			}
    		
    	});
    }
    public void setListNavigationCallbacks(BaseAdapter adapter, OnNavigationListener onNavigationListener){
    	initNavigationPopuMenu(mActionBarActivity,adapter,onNavigationListener);
    }
    public void clearListNavigation(){
    	mTitleView.setCompoundDrawables(null, null, null, null);
    	mTitleView.setOnClickListener(null);
    }
	private void initNavigationPopuMenu(Context context,BaseAdapter adapter, final OnNavigationListener onNavigationListener){
    	final View popuLayout=mInflater.inflate(R.layout.actionbar_navigation_list, null);
    	ListView listView=(ListView) popuLayout.findViewById(R.id.actionbar_popup_actions_list);
    	listView.setAdapter(adapter);
    	final int width=(int) (200*context.getResources().getDisplayMetrics().density);
    	mPopuMenu=new PopupWindow(popuLayout, width, LayoutParams.WRAP_CONTENT, true);
    	mPopuMenu.setBackgroundDrawable(new BitmapDrawable()); 
    	
    	listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(onNavigationListener!=null)
					onNavigationListener.onNavigationItemSelected(position, id);
				mPopuMenu.dismiss();
			}
    		
    	});
    	Drawable imgV = getResources().getDrawable(R.drawable.actionbar_dropdown);
    	imgV.setBounds(0, 0, imgV.getIntrinsicWidth(), imgV.getIntrinsicHeight());      
    	mTitleView.setCompoundDrawables(imgV, null, null, null);
    	mTitleView.setOnClickListener(new OnClickListener(){

 			@Override
 			public void onClick(View v) {
 				if(!mPopuMenu.isShowing()){
 					int xoff=-(width-mTitleView.getWidth())/2;
 					mPopuMenu.showAsDropDown(mTitleView, xoff, 0);
 				}
 			}
     		
     	});
	}
	
    public void setDisplayHomeAsUpEnabled(boolean show) {
    	mIndicator.setVisibility(show? View.VISIBLE : View.INVISIBLE);
    }
    public void displayHomeIndicator() {
    	mDrawerArrowDrawable.setParameter(0);
    	mIndicator.setImageDrawable(mDrawerArrowDrawable);
	}
	public void displayUpIndicator() {
		mDrawerArrowDrawable.setParameter(1);
		mIndicator.setImageDrawable(mDrawerArrowDrawable);
	}
	public void displayIndicator(float slideOffset) {
		if(slideOffset >= .995) {
			mDrawerArrowDrawable.setFlip(true);
        }else if (slideOffset <= .005) {
        	mDrawerArrowDrawable.setFlip(false);
        }
		mDrawerArrowDrawable.setParameter(slideOffset);
		mIndicator.setImageDrawable(mDrawerArrowDrawable);
	}
	public String getTitle(){
    	return (String) mTitleView.getText()	;
    }
    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public void setTitle(int resid) {
        mTitleView.setText(resid);
    }
    public void setTitleGravity(int gravity) {
        mTitleView.setGravity(gravity);
    }
    public void setOnTitleClickListener(OnClickListener listener) {
        mTitleView.setOnClickListener(listener);
    }
    
    public ActionMode startActionMode(Callback callback){
    	if(!mActionMode.isActionMode()){
    		mActionMode.start(callback);
    	}
    	return mActionMode;
    }
    public void stopActionMode(){
    	if(mActionMode.isActionMode()){
    		mActionMode.finish();
    	}
    }
    
    public void startProgress(){
    	mProgressView.startProgress();
    }
    
    public void stopProgress(){
    	mProgressView.stopProgress();
    }
    
    public SearchView startSearchMode(){
    	mIsSearchMode=true;
    	mSearchView.setVisibility(View.VISIBLE);
    	return mSearchView;
    }
    
    public void stopSearchMode(){
    	mIsSearchMode=false;
    	mSearchView.setVisibility(View.GONE);
    }

	public ActionMenu getActionMenu() {
		return mActionMenu;
	}

	public void clearActions() {
		mActionMenu.clear();
	}

	public ArrayList<ActionMenuItem> getActions() {
		return mActionMenu.getActions();
	}

	public void addActions(ArrayList<ActionMenuItem> actions) {
		mActionMenu.addActions(actions);
	}
	
	public boolean onBackPressed(){
		if(mProgressView.isProgress())
			mProgressView.stopProgress();
		
		if(mIsSearchMode){
			stopSearchMode();
			return true;
		}
		if(mActionMode.isActionMode()){
			stopActionMode();
			return true;
		}
		
		return false;
	}
}