package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.view.TabPageManager;

public class TabPagesFragment extends BaseContentFragment {
	private TabPageManager  mTabPageManager;
	private TabHost mTabHost;
	private ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_tabs_pager, container,false);
		return v;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findViews(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews(savedInstanceState);
		initData(savedInstanceState);
	}
	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void findViews(View view) {
		this.setTitle(this.getClass().getSimpleName());
		mTabHost = (TabHost)view.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mViewPager = (ViewPager)view.findViewById(R.id.pager);
		mTabPageManager=new TabPageManager(this.getChildFragmentManager(),mTabHost,mViewPager);
		Bundle args1=new Bundle();
		args1.putString("flag", "Simple1");
		mTabPageManager.addTab(mTabHost.newTabSpec("ItemFragment1").setIndicator("Simple1"), ItemFragment.class, args1);
		Bundle args2=new Bundle();
		args2.putString("flag", "Simple2");
		mTabPageManager.addTab(mTabHost.newTabSpec("ItemFragment2").setIndicator("Simple2"), ItemFragment.class, args2);
		Bundle args3=new Bundle();
		args3.putString("flag", "Simple3");
		mTabPageManager.addTab(mTabHost.newTabSpec("ItemFragment3").setIndicator("Simple3"), ItemFragment.class, args3);
	
	}
	@Override
	protected void initViews(Bundle savedInstanceState) {

	}

	@Override
	public void onDestroyView() {
		mTabPageManager.destroy();
		super.onDestroyView();
	}
}
