package mobi.cangol.mobile.base;

import mobi.cangol.mobile.navigation.AbstractNavigationFragmentActivityDelegate;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public  abstract class BaseNavigationFragmentActivity extends BaseActionBarActivity  {
	protected String TAG = Utils.makeLogTag(BaseNavigationFragmentActivity.class);
	private BaseMenuFragment menuFragment;
	private static final String MENU_TAG="MenuFragment";
	private AbstractNavigationFragmentActivityDelegate mHelper;
	private int mContentFrameId;
	private int mMenuFrameId;
	public int getContentFrameId() {
		return mContentFrameId;
	}
	
	public int getMenuFrameId() {
		return mMenuFrameId;
	}
	
	public void setNavigationFragmentActivityDelegate(AbstractNavigationFragmentActivityDelegate mHelper) {
		this.mHelper = mHelper;
		this.mMenuFrameId=mHelper.getMenuFrameId();
		this.mContentFrameId=mHelper.getContentFrameId();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper.onCreate(savedInstanceState);
		this.initFragmentStack(mContentFrameId);
	}
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}
	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null)
			return v;
		return mHelper.findViewById(id);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mHelper.onSaveInstanceState(outState);
	}

	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	@Override
	public void setContentView(View v) {
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public void setContentView(View v, LayoutParams params) {
		super.setContentView(v, params);
	}
	
	public void showMenu(boolean show){
		mHelper.showMenu(show);
	}
	
	public boolean isShowMenu(){
		return mHelper.isShowMenu();
	}
	public void setMenuEnable(boolean enable) {
		mHelper.setMenuEnable(enable);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean b = mHelper.onKeyUp(keyCode, event);
		if (b) return b;
		return super.onKeyUp(keyCode, event);
	}

	public void setCurrentModuleId(int moduleId) {
		if(menuFragment==null){
			throw new IllegalStateException("menuFragment is null");
		}else
			menuFragment.setCurrentModuleId(moduleId);
	}
	
	final public void setMenuFragment(Class<? extends BaseMenuFragment> fragmentClass,Bundle args) {
		menuFragment = (BaseMenuFragment) Fragment.instantiate(this,fragmentClass.getName(), args);
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		t.replace(this.getMenuFrameId(), menuFragment,MENU_TAG);
		t.commit();
		getSupportFragmentManager().executePendingTransactions();
	}
	final public void setContentFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args,int moduleId) {
		replaceFragment(fragmentClass,tag,args);
		setCurrentModuleId(moduleId);
	}
	final public void setContentFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args) {
		replaceFragment(fragmentClass, tag,args);
	}
	public void notifyMenuOnClose(){
		if(menuFragment!=null)
		menuFragment.onClosed();
	}
	
	public void notifyMenuOnOpen(){
		if(menuFragment!=null)
			menuFragment.onOpen();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		menuFragment=(BaseMenuFragment) getSupportFragmentManager().findFragmentByTag(MENU_TAG);
	}
	@Override
	public boolean onSupportNavigateUp() {
		if (stack.size() <= 1) {
			if(!isShowMenu()){
				showMenu(true);
			}else{
				showMenu(false);
			}
			return true;
		} else {
			return super.onSupportNavigateUp();
		}
	}
}

