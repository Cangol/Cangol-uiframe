package mobi.cangol.mobile.base;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.Session;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.Toast;
/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public  abstract class BaseFragmentActivity extends FragmentActivity implements BaseActivityDelegate,CustomFragmentActivityDelegate{
	protected String TAG = Utils.makeLogTag(BaseFragmentActivity.class);
	private static final boolean LIFECYCLE=Utils.LIFECYCLE;
	protected CoreApplication app;
	private CustomFragmentManager stack;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		TAG = Utils.makeLogTag(this.getClass());
		app = (CoreApplication) this.getApplication();
		app.addActivityToManager(this);
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
		if(LIFECYCLE)Log.v(TAG, "onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(LIFECYCLE)Log.v(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(LIFECYCLE)Log.v(TAG, "onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(LIFECYCLE)Log.v(TAG, "onRestart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(LIFECYCLE)Log.v(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		app.delActivityFromManager(this);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(LIFECYCLE)Log.v(TAG, "onRestoreInstanceState");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(null!=stack)stack.saveState(outState);
		super.onSaveInstanceState(outState);
		if(LIFECYCLE)Log.v(TAG, "onSaveInstanceState");
	}

	@Override
	public Object getLastCustomNonConfigurationInstance() {
		if(LIFECYCLE)Log.v(TAG, "getLastCustomNonConfigurationInstance");
		return super.getLastCustomNonConfigurationInstance();
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		if(LIFECYCLE)Log.v(TAG, "onRetainCustomNonConfigurationInstance");
		return super.onRetainCustomNonConfigurationInstance();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(LIFECYCLE)Log.v(TAG, "onConfigurationChanged");
	}
	@Override
	public void setFullScreen(boolean fullscreen) {
		if(fullscreen){
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
	}
	@Override
	final public void onBackPressed() {
		Log.e( "onBackPressed ");
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
	public void onBack(){
		if(LIFECYCLE)Log.v(TAG, "onBack");
		super.onBackPressed();
	}
}
