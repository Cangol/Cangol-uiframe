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

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.Session;
import mobi.cangol.mobile.actionbar.ActionBarActivity;
import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public abstract  class BaseActionBarActivity extends ActionBarActivity implements BaseActivityDelegate, CustomFragmentActivityDelegate{
	protected String TAG = Utils.makeLogTag(BaseActionBarActivity.class);
	private static final boolean LIFECYCLE=Utils.LIFECYCLE;
	protected CoreApplication app;
	protected CustomFragmentManager stack;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		TAG = Utils.makeLogTag(this.getClass());
		app = (CoreApplication) this.getApplication();
		app.addActivityToManager(this);
		getCustomActionBar().setDisplayHomeAsUpEnabled(true);
		if(savedInstanceState==null){
			
		}else{
			if(null!=stack)stack.restoreState(savedInstanceState);
		}
	}

	public  void showToast(int resId){
		Toast.makeText(this,resId,Toast.LENGTH_SHORT).show();
	}
	public  void showToast(String str){
		Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
	}
	public  void showToast(int resId,int duration){
		Toast.makeText(this,resId,duration).show();
	}
	public  void showToast(String str,int duration){
		Toast.makeText(this,str,duration).show();
	}
	public void initFragmentStack(int containerId){
		if(containerId<=0){
			throw new IllegalStateException("getContainerId must return a valid  containerId");
		}
		if(null==stack)
		stack = CustomFragmentManager.forContainer(this, containerId,this.getSupportFragmentManager());
	}
	public void replaceFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args) {
		if(null==stack){
			throw new IllegalStateException("stack is null");
		}
		stack.replace(fragmentClass, tag,args);
		stack.commit();
	}
	@Override
	public CustomFragmentManager getCustomFragmentManager() {
		return stack;
	}
	public AppService getAppService(String name) {
		return app.getAppService(name);
	}
	public Session getSession() {
		return app.getSession();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(LIFECYCLE)Log.v(TAG,"onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(LIFECYCLE)Log.v(TAG,"onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(LIFECYCLE)Log.v(TAG,"onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(LIFECYCLE)Log.v(TAG,"onRestart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(LIFECYCLE)Log.v(TAG,"onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		app.delActivityFromManager(this);
	}
	public void onBack(){
		if(LIFECYCLE)Log.v(TAG,"onBack");
		super.onBackPressed();
	}
	@Override
	final public void onBackPressed() {
		Log.e("onBackPressed ");
		if(null==stack){
			onBack();
			return;
		}
		if (stack.size() <= 1) {
			onBack();
		} else {
			if (stack.peek().onBackPressed()) {
				return;
			} else {
				stack.pop();
				return;
			}
		}
	}
	@Override
	public boolean onSupportNavigateUp() {
		if(LIFECYCLE)
			Log.v(TAG, "onSupportNavigateUp ");
		if (stack.size() <= 1) {
			return true;
		} else {
			if (stack.peek().onSupportNavigateUp()) {
				return true;
			} else {
				FragmentInfo upFragment=stack.peek().getNavigtionUpToFragment();
				if(upFragment!=null){
					replaceFragment(upFragment.clss,upFragment.tag,upFragment.args);
				}else{
					stack.pop();
				}
				return true;
			}
		}
	}
	public void onMenuActionCreated(ActionMenu actionMenu) {
		if(stack!=null&&stack.size()>0){
			stack.peek().onMenuActionCreated(actionMenu);
		}
	}
	
	public boolean onMenuActionSelected(ActionMenuItem action) {
		if(null!=stack){
			return stack.peek().onMenuActionSelected(action);
		}
		return false;
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(LIFECYCLE)Log.v(TAG,"onRestoreInstanceState");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(null!=stack)stack.saveState(outState);
		super.onSaveInstanceState(outState);
		if(LIFECYCLE)Log.v(TAG,"onSaveInstanceState");
	}

	@Override
	public Object getLastCustomNonConfigurationInstance() {
		if(LIFECYCLE)Log.v(TAG,"getLastCustomNonConfigurationInstance");
		return super.getLastCustomNonConfigurationInstance();
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		if(LIFECYCLE)Log.v(TAG,"onRetainCustomNonConfigurationInstance");
		return super.onRetainCustomNonConfigurationInstance();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(LIFECYCLE)Log.v(TAG,"onConfigurationChanged");
	}
}
