package mobi.cangol.mobile.base;

import android.os.Bundle;
/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public abstract class BaseMenuFragment extends BaseFragment{
	private int currentModuleId;
	
	public int getCurrentModuleId() {
		return currentModuleId;
	}
	
	public void setCurrentModuleId(int currentModuleId) {
		this.currentModuleId = currentModuleId;
		if(this.isEnable())
			onContentChange(currentModuleId);
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null)
			setCurrentModuleId(savedInstanceState.getInt("currentModuleId"));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentModuleId", currentModuleId);
	}
	
	public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass,String tag,Bundle args) {
		
		if(getActivity()==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
			bfActivity.setContentFragment(fragmentClass, tag,args);
		}
	}

	public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass,String tag,Bundle args,int moduleId) {
		
		if(getActivity()==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
			bfActivity.setContentFragment(fragmentClass, tag,args,moduleId);
		}
	}
	public void showMenu(boolean show) {
		
		if(getActivity()==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
			bfActivity.showMenu(show);
		}
	}
	
	abstract  protected void onContentChange(int moduleId);
	
	abstract protected void onOpen();
	
	abstract protected void onClosed();

}
