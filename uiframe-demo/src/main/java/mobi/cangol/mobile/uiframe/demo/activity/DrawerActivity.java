package mobi.cangol.mobile.uiframe.demo.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.navigation.DrawerNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.LeakSingleton;
import mobi.cangol.mobile.uiframe.demo.fragment.HomeFragment;
import mobi.cangol.mobile.uiframe.demo.fragment.ListFragment;
import mobi.cangol.mobile.uiframe.demo.fragment.MenuFragment;

@SuppressLint("ResourceAsColor")
public class DrawerActivity extends DrawerNavigationFragmentActivity {
	private static long back_pressed;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.setStatusBarTintColor(Color.DKGRAY);
		this.setNavigationBarTintColor(Color.DKGRAY);
		this.getCustomActionBar().setBackgroundColor(Color.DKGRAY);
		this.setFloatActionBarEnabled(true);
		if (savedInstanceState == null) {
			Bundle bundle=new Bundle();
			bundle.putBoolean("isBottom",false);
			this.setMenuFragment(MenuFragment.class,bundle);
			this.setContentFragment(HomeFragment.class, "TestFragment", null, MenuFragment.MODULE_HOME);
		}
		findViews();
		initViews(savedInstanceState);
		initData(savedInstanceState);
        //this.initFragmentStack(R.id.content_frame);
        //if(savedInstanceState==null)this.replaceFragment(TestFragment.class, "Home", null);
		LeakSingleton.getInstance().setOnTestListener(new LeakSingleton.OnTestListener() {
			@Override
			public void onTest() {
				setContentFragment(ListFragment.class, "ListFragment", null, MenuFragment.MODULE_CLEAN);
			}
		});
	}

	@Override
	public void findViews() {
	}
	@Override
	public void initViews(Bundle savedInstanceState) {

	}
		
	@Override
	public void initData(Bundle savedInstanceState) {
		Log.d(TAG,"initData isStateSaved="+getCustomFragmentManager().isStateSaved());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG,"onSaveInstanceState isStateSaved="+getCustomFragmentManager().isStateSaved());
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG,"onDestroy isStateSaved="+getCustomFragmentManager().isStateSaved());
		super.onDestroy();
	}
	@Override
	public void onBack() {
		if(back_pressed+2000>System.currentTimeMillis()){
			super.onBack();
			app.exit();
		}else{
			back_pressed=System.currentTimeMillis();
            showToast("Please on back");
		}
	}
	public int getContentFrameId() {
		return R.id.frame_main;
	}
}
