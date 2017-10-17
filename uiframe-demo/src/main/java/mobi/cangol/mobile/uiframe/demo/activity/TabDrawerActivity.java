package mobi.cangol.mobile.uiframe.demo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;

import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.navigation.TabDrawerNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.fragment.HomeFragment;
import mobi.cangol.mobile.uiframe.demo.fragment.ListFragment;
import mobi.cangol.mobile.uiframe.demo.fragment.MenuBottomFragment;

@SuppressLint("ResourceAsColor")
public class TabDrawerActivity extends TabDrawerNavigationFragmentActivity {
	private static long back_pressed;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setStatusBarTintColor(R.color.red);
		this.setNavigationBarTintColor(R.color.black);

		setContentView(R.layout.activity_main);
		this.getCustomActionBar().setBackgroundResource(R.color.red);
		this.setFloatActionBarEnabled(true);
		if (savedInstanceState == null) {
			this.setMenuFragment(MenuBottomFragment.class,null);
			this.setContentFragment(HomeFragment.class, "HomeFragment", null,MenuBottomFragment.MODULE_HOME);
		}
		findViews();
		initViews(savedInstanceState);
		initData(savedInstanceState);
		//this.setDrawerEnable(Gravity.RIGHT,true);
	}
	@Override
	protected void onStart() {
		super.onStart();
		Log.v("onStart " + getDrawer(Gravity.RIGHT));

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
			//app.exit();
		}else{
			back_pressed=System.currentTimeMillis();
            showToast("Please on back");
		}
	}

	public int getContentFrameId() {
		return R.id.frame_main;
	}
}
