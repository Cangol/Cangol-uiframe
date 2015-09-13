/** 
 * Copyright (c) 2013 Cangol
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.base;

import mobi.cangol.mobile.actionbar.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public abstract class BaseContentFragment extends BaseFragment{
	
	
	final public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass,String tag,Bundle args) {
		replaceFragment(fragmentClass, tag, args);
	}
	
	
	final public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass,String tag,Bundle args,int moduleId) {
		setContentFragment(fragmentClass, tag, args);
		notifyMenuChange(moduleId);
		
	}
	
	final public void notifyMenuChange(int moduleId){
		if(getActivity()==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			if(this.getActivity() instanceof BaseNavigationFragmentActivity){
				BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
				bfActivity.setCurrentModuleId(moduleId);
			}
		}
	}
	
	final private void setActionBarUpIndicator(){
		BaseFragment parent=(BaseFragment) this.getParentFragment();
		if(parent==null){
			if(getActivity()==null){
				throw new IllegalStateException("getActivity is null");
			}else{
				ActionBarActivity bfActivity = (ActionBarActivity) this.getActivity();
				if(isCleanStack()){
					bfActivity.getCustomActionBar().displayHomeIndicator();
				}else{
					bfActivity.getCustomActionBar().displayUpIndicator();
				}
			}
		}else{
			
		}
		
	}
	final private void setMenuEnable(boolean enable) {
		BaseFragment parent=(BaseFragment) this.getParentFragment();
		if(parent==null){
			if(getActivity()==null){
				throw new IllegalStateException("getActivity is null");
			}else if(this.getParentFragment()==null){
				if(this.getActivity() instanceof BaseNavigationFragmentActivity){
					BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
					bfActivity.setMenuEnable(enable);
				}
			}
		}
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setActionBarUpIndicator();
		setMenuEnable(isCleanStack());
	}


	
}
