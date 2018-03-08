package mobi.cangol.mobile.uiframe.demo.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.navigation.TabNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.Singleton;
import mobi.cangol.mobile.uiframe.demo.fragment.HomeFragment;
import mobi.cangol.mobile.uiframe.demo.fragment.MenuBottomFragment;
import mobi.cangol.mobile.uiframe.demo.fragment.SettingFragment;

@SuppressLint("ResourceAsColor")
public class TabActivity extends TabNavigationFragmentActivity {
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
			this.setMenuFragment(MenuBottomFragment.class,null);
			this.setContentFragment(HomeFragment.class, "HomeFragment", null,MenuBottomFragment.MODULE_HOME);
		}
		findViews();
		initViews(savedInstanceState);
		initData(savedInstanceState);
		//this.setFloatActionBarEnabled(true);
        //this.initFragmentStack(R.id.content_frame);
        //if(savedInstanceState==null)this.replaceFragment(HomeFragment.class, "Home", null);
		Singleton.getInstance().setOnTestListener(new Singleton.OnTestListener() {
			@Override
			public void onTest() {
				setContentFragment(SettingFragment.class, "SettingFragment", null,MenuBottomFragment.MODULE_SETTING);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v("onStart " + System.currentTimeMillis());
	}

	@Override
	public void findViews() {
	}
	@Override
	public void initViews(Bundle savedInstanceState) {
		
	}
	@Override
	public void initData(Bundle savedInstanceState) {
		
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
