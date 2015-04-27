package mobi.cangol.mobile.base;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.Session;
import mobi.cangol.mobile.actionbar.ActionBar;
import mobi.cangol.mobile.actionbar.ActionBarActivity;
import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.actionbar.ActionMode;
import mobi.cangol.mobile.actionbar.ActionMode.Callback;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public abstract class BaseFragment extends Fragment{
	protected String TAG = Utils.makeLogTag(BaseFragment.class);
	public static final int RESULT_CANCELED    = 0;
	/** Standard Fragment result: operation succeeded. */
	public static final int RESULT_OK = -1;
	private static final boolean LIFECYCLE=Utils.LIFECYCLE;
	private String title;
	private CustomFragmentManager stack;
	protected CoreApplication app;
	protected FragmentInfo mUpFragment;
	
	private long starttime;
	
	private int resultCode = RESULT_CANCELED;
	private Bundle resultData;
	
	public int getResultCode() {
		return resultCode;
	}
	
	public float getIdletime(){
		 return (System.currentTimeMillis()-starttime)/1000.0f;
	}
	abstract protected void findViews(View view);

	abstract protected void initViews(Bundle savedInstanceState);
	
	abstract protected void initData(Bundle savedInstanceState);
	
	abstract protected FragmentInfo getNavigtionUpToFragment();	
	public ActionBar getCustomActionBar() {
		ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
		if(abActivity==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			return abActivity.getCustomActionBar();
		}
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		if(this.getParentFragment()!=null)return;
		this.title=title;
		if(getCustomActionBar()!=null)
		getCustomActionBar().setTitle(title);
	}
	public void setTitle(int title) {
		if(this.getParentFragment()!=null)return;
		this.title=getString(title);
		if(getCustomActionBar()!=null)
		getCustomActionBar().setTitle(title);
	}
	public final View findViewById(int id) {
		return this.getView().findViewById(id);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		TAG = Utils.makeLogTag(this.getClass());
		if(LIFECYCLE)Log.v(TAG,"onAttach");
	}
	public void setStatusBarColor(int color){
		ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
		if(abActivity==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			abActivity.setStatusBarTintColor(color);
		}
	}
	
	public void setNavigationBarTintColor(int color){
		ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
		if(abActivity==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			abActivity.setNavigationBarTintColor(color);
		}
	}
	public void setFullScreen(boolean fullscreen){
		if(this.getActivity()==null)return;
		if(fullscreen){
			this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			this.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		 
	}
	public void startProgress(){
		ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
		if(abActivity==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			 abActivity.getCustomActionBar().startProgress();
		}
	}
	public void stopProgress(){
		ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
		if(abActivity==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			 abActivity.getCustomActionBar().stopProgress();
		}
	}
	public ActionMode startCustomActionMode(Callback callback){
		ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
		if(abActivity==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			return abActivity.startCustomActionMode(callback);
		}
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(LIFECYCLE)Log.v(TAG, "onCreate");
		setHasOptionsMenu(true);
		setMenuVisibility(true);
		app = (CoreApplication) this.getActivity().getApplication();
		if(savedInstanceState==null){
		}else{
			title=savedInstanceState.getString("title");
			if(null!=stack)stack.restoreState(savedInstanceState);
		}
	}
	protected void initFragmentStack(int containerId){
		if(null==stack)
		stack = CustomFragmentManager.forContainer(this.getActivity(), containerId,this.getChildFragmentManager());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(LIFECYCLE){Log.v(TAG, "onCreateView");
			starttime=System.currentTimeMillis();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(LIFECYCLE)Log.v(TAG, "onViewCreated");
		if(this.getParentFragment()==null){
			getCustomActionBar().clearActions();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(LIFECYCLE)Log.v(TAG, "onActivityCreated");
		ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
		if(abActivity==null){
			throw new IllegalStateException("getActivity is null");
		}else{
			this.onMenuActionCreated(abActivity.getCustomActionBar().getActionMenu());
		}
	}
	

	@Override
	public void onPause() {
		super.onPause();
		if(LIFECYCLE)Log.v(TAG, "onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		if(LIFECYCLE)Log.v(TAG, "onResume "+getIdletime()+"s");
	}

	@Override
	public void onStart() {
		super.onStart();
		if(LIFECYCLE)Log.v(TAG, "onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		if(LIFECYCLE)Log.v(TAG, "onStop");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if(LIFECYCLE)Log.v(TAG, "onDetach");
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(LIFECYCLE)Log.v(TAG, "onDestroyView "+getIdletime()+"s");
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(LIFECYCLE)Log.v(TAG, "onDestroy");
	}
	protected boolean onMenuActionCreated(ActionMenu actionMenu) {
		
		return false;
	}
	
	protected boolean onMenuActionSelected(ActionMenuItem action) {
		
		return false;
	}
	public void showToast(int resId){
		Toast.makeText(this.getActivity(),resId,Toast.LENGTH_SHORT).show();
	}
	public void showToast(String str){
		Toast.makeText(this.getActivity(),str,Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(LIFECYCLE)Log.v(TAG, "onConfigurationChanged");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(LIFECYCLE)Log.v(TAG, "onActivityResult");
		
	}
	
	public boolean onBackPressed() {
		if(LIFECYCLE)Log.v(TAG, "onBackPressed");
		if(null==stack)return false;
		if (stack.size() <= 1) {
			return false;
		} else {
			if (stack.peek().onBackPressed()) {
				return true;
			} else {
				stack.pop();
				return true;
			}
		}
	}
	public boolean isSingleton(){
		return true;
	}
	public boolean isCleanStack(){
		return false;
	}
	public boolean onSupportNavigateUp() {
		
		return false;
	}
	public void  popBackStack(){
		BaseFragment parent=(BaseFragment) this.getParentFragment();
		if(parent!=null){
			parent.getCustomFragmentManager().pop();
		}else{
			if(getActivity()==null){
				throw new IllegalStateException("getActivity is null");
			}else{
				CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
				bfActivity.getCustomFragmentManager().pop();
			}
		}
	}
	
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		if(LIFECYCLE)Log.v(TAG, "onFragmentResult");
    }	
	
	public void setResult(int resultCode){
		this.setResult(resultCode, null);
	}
	
	public void setResult(int resultCode,Bundle resultData){
			this.resultCode=resultCode;
			this.resultData=resultData;
	}
	public void notifyResult(){
		BaseFragment taget=(BaseFragment) getTargetFragment();
		if(taget!=null){
			taget.onFragmentResult(getTargetRequestCode(),resultCode, resultData);
		}else{
			throw new IllegalStateException("Target Fragment is null");
		}
	}
	public CustomFragmentManager getCustomFragmentManager() {
		return stack;
	}
	
	public void replaceFragment(Class<? extends BaseFragment> fragmentClass){
		replaceFragment(fragmentClass, fragmentClass.getSimpleName(), null);
	}
	
	public void replaceFragment(Class<? extends BaseFragment> fragmentClass , Bundle bundle){
		replaceFragment(fragmentClass, fragmentClass.getSimpleName(), bundle);
	}
	
	public void replaceFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args) {
		this.replaceFragment(fragmentClass, tag, args,null);
	}
	
	public void replaceFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args,int requestCode) {
		if(requestCode!=-1){
			this.replaceFragment(fragmentClass, tag, args,new CustomFragmentTransaction().setTargetFragment(this, requestCode));	
		}else{
			throw new IllegalStateException("requestCode!=-1");
		}
	}
	public void replaceFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args,CustomFragmentTransaction customFragmentTransaction){
		BaseFragment parent=(BaseFragment) this.getParentFragment();
		CustomFragmentManager stack=null;
		if(parent!=null){
			stack=parent.getCustomFragmentManager();
		}else{
			if(getActivity()==null){
				throw new IllegalStateException("getActivity is null");
			}else{
				CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
				stack=bfActivity.getCustomFragmentManager();
			}
		}
		stack.replace(fragmentClass, tag,args,customFragmentTransaction);
		stack.commit();
	}
	public void replaceParentFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args) {
		replaceParentFragment(fragmentClass, tag, args,null);
	}
	public void replaceParentFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args,int requestCode) {
		if(requestCode!=-1){
			this.replaceParentFragment(fragmentClass, tag, args,new CustomFragmentTransaction().setTargetFragment(this, requestCode));	
		}else{
			throw new IllegalStateException("requestCode!=-1");
		}
	}
	public void replaceParentFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args,CustomFragmentTransaction customFragmentTransaction){
		BaseFragment parent=(BaseFragment) this.getParentFragment();
		if(parent!=null){
			parent.replaceFragment(fragmentClass, tag,args,customFragmentTransaction);
		}else{
			throw new IllegalStateException("ParentFragment is null");
		}
	}
	public void replaceChildFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args) {
		replaceChildFragment(fragmentClass, tag, args,null);
	}
	
	public void replaceChildFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args,int enter,int exit) {
		replaceChildFragment(fragmentClass, tag,args,new CustomFragmentTransaction().setCustomAnimations(enter, exit));	
	}
	
	public void replaceChildFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args,int enter,int exit,int popEnter, int popExit) {
		replaceChildFragment(fragmentClass, tag,args,new CustomFragmentTransaction().setCustomAnimations(enter,exit,popEnter,popExit));	
	}
	
	public void replaceChildFragment(Class<? extends BaseFragment> fragmentClass,String tag,Bundle args,CustomFragmentTransaction customFragmentTransaction) {
		if(stack!=null){
			stack.replace(fragmentClass, tag,args,customFragmentTransaction);
			stack.commit();
		}else{
			throw new IllegalStateException("fragment'CustomFragmentManager is null, Pleaser initFragmentStack");
		}
		
	}
	
//	/**ActionBarActivity method**/
//	public void requestWindowFeature(int featureId){
//		if(getActivity()==null){
//			throw new IllegalStateException("getActivity is null");
//		}else{
//			ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
//			abActivity.requestWindowFeature(featureId);
//		}
//	}
	
	public AppService getAppService(String name) {
		return app.getAppService(name);
	}
	public Session getSession() {
		return app.getSession();
	}
	public boolean isEnable() {
		if (null == getActivity() || !isAdded() || isRemoving() || isDetached()) {
			return false;
		}
		return true;
	}
	
}
