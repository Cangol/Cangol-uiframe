package mobi.cangol.mobile.uiframe;

import mobi.cangol.mobile.base.BaseActionBarActivity;
import mobi.cangol.mobile.uiframe.fragment.HomeFragment;
import mobi.cangol.mobile.logging.Log;

import android.annotation.SuppressLint;
import android.os.Bundle;

@SuppressLint("ResourceAsColor")
public class MainActivity extends BaseActionBarActivity  {
	private static long back_pressed;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.setStatusBarTintColor(R.color.red);
//		this.setNavigationBarTintColor(R.color.black);
		setContentView(R.layout.activity_main);
		this.getCustomActionBar().setBackgroundResource(R.color.red);
//		if (savedInstanceState == null) {
//			this.setMenuFragment(MenuFragment2.class,null);
//			this.setContentFragment(HomeFragment.class, "HomeFragment", null);
//		}
		findViews();
		initViews(savedInstanceState);
		initData(savedInstanceState);
		//this.setFloatActionBarEnabled(true);
        this.initFragmentStack(R.id.content_frame);
        if(savedInstanceState==null)
            this.replaceFragment(HomeFragment.class, "Home", null);
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
		return R.id.content_frame;
	}
}
