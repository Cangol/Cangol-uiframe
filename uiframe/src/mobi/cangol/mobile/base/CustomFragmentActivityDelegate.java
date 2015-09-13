package mobi.cangol.mobile.base;

import android.os.Bundle;


public  interface CustomFragmentActivityDelegate {
	
	public void initFragmentStack(int containerId);
	
	public void  replaceFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args);

	public CustomFragmentManager getCustomFragmentManager();
	
}
