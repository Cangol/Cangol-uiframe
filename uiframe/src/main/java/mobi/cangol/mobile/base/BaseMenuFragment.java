/**
 * Copyright (c) 2013 Cangol
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.base;

import android.os.Bundle;

public abstract class BaseMenuFragment extends BaseFragment{
	private int currentModuleId;
	public BaseMenuFragment() {
		super();
	}
    /**
     * 获取当前模块Id
     * @return
     */
	public int getCurrentModuleId() {
		return currentModuleId;
	}

    /**
     * 设置当前模块
     * @param currentModuleId
     */
	public void setCurrentModuleId(int currentModuleId) {
		this.currentModuleId = currentModuleId;
		if(this.isEnable())
			onContentChange(currentModuleId);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //获取模块id
		if(savedInstanceState!=null)
			setCurrentModuleId(savedInstanceState.getInt("currentModuleId"));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        //存储模块id
		outState.putInt("currentModuleId", currentModuleId);
	}

    /**
     * 设置内容区域fragment
     * @param fragmentClass
     * @param tag
     * @param args
     */
	public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass,String tag,Bundle args) {
		
		if(getActivity()==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
			bfActivity.setContentFragment(fragmentClass, tag,args);
		}
	}

    /**
     * 设置内容区域fragment
     * @param fragmentClass
     * @param tag
     * @param args
     * @param moduleId 模块ID
     */
	public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass,String tag,Bundle args,int moduleId) {
		
		if(getActivity()==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
			bfActivity.setContentFragment(fragmentClass, tag,args,moduleId);
		}
	}

    /**
     * 显示或关闭 菜单
     * @param show
     */
	public void showMenu(boolean show) {
		
		if(getActivity()==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
			bfActivity.showMenu(show);
		}
	}
	
	abstract  protected void onContentChange(int moduleId);

    /**
     * 菜单打开时调用
     */
	abstract protected void onOpen();

    /**
     * 菜单关闭时调用
     */
	abstract protected void onClosed();

}
