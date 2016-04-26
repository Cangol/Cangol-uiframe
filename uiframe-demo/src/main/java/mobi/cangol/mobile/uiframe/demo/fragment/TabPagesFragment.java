package mobi.cangol.mobile.uiframe.demo.fragment;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.R;
import mobi.cangol.mobile.uiframe.demo.view.TabPageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class TabPagesFragment extends BaseContentFragment {
	private TabPageManager  mTabPageManager;
	private TabHost mTabHost;
	private ViewPager mViewPager;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
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
		// TODO Auto-generated method stub
				
	}

	@Override
	protected void findViews(View view) {
		this.setTitle(this.getClass().getSimpleName());
		mTabHost = (TabHost)view.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mViewPager = (ViewPager)view.findViewById(R.id.pager);
		mTabPageManager=new TabPageManager(this.getChildFragmentManager(),mTabHost,mViewPager);
		mTabPageManager.addTab(mTabHost.newTabSpec("ItemFragment1").setIndicator("Simple1"), SettingFragment.class, new Bundle());
		mTabPageManager.addTab(mTabHost.newTabSpec("ItemFragment2").setIndicator("Simple2"), ItemFragment.class, new Bundle());
		mTabPageManager.addTab(mTabHost.newTabSpec("ItemFragment3").setIndicator("Simple3"), SettingFragment.class, new Bundle());
	
	}
	@Override
	protected void initViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected FragmentInfo getNavigtionUpToFragment() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isCleanStack() {
		return true;
	}
	
}
