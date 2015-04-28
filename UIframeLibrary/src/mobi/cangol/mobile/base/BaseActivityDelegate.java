package mobi.cangol.mobile.base;

import android.os.Bundle;
import mobi.cangol.mobile.Session;
import mobi.cangol.mobile.service.AppService;

public interface BaseActivityDelegate {
	
	public void setFullScreen(boolean fullscreen);
	
	public  void findViews();

	public  void initViews(Bundle savedInstanceState);

	public  void initData(Bundle savedInstanceState);
	
	public  void showToast(int resId);
	
	public  void showToast(String str);
	
	public  void showToast(int resId,int duration);
	
	public  void showToast(String str,int duration);
	
	public AppService getAppService(String name);
	
	public Session getSession();
	
	public void onBack();
}
