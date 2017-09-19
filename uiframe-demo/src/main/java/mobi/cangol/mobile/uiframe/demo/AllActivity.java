package mobi.cangol.mobile.uiframe.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mobi.cangol.mobile.base.BaseFragmentActivity;
import mobi.cangol.mobile.uiframe.demo.activity.DrawerActivity;
import mobi.cangol.mobile.uiframe.demo.activity.SlidingActivity;
import mobi.cangol.mobile.uiframe.demo.activity.TabActivity;
import mobi.cangol.mobile.uiframe.demo.activity.TabDrawerActivity;

public class AllActivity extends BaseFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all);
		findViews();
		initViews(savedInstanceState);
		initData(savedInstanceState);
	}

	@Override
	public void findViews() {}

	@Override
	public void initViews(Bundle savedInstanceState) {
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AllActivity.this, SlidingActivity.class));
				finish();
			}

		});
		findViewById(R.id.button2).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AllActivity.this, DrawerActivity.class));
				finish();
			}

		});
		findViewById(R.id.button3).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AllActivity.this, TabActivity.class));
				finish();
			}

		});
		findViewById(R.id.button4).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AllActivity.this, TabDrawerActivity.class));
				finish();
			}

		});
	}

	@Override
	public void initData(Bundle savedInstanceState) {
	}
}
