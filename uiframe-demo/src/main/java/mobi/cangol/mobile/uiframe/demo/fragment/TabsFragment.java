package mobi.cangol.mobile.uiframe.demo.fragment;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.view.TabManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class TabsFragment extends BaseContentFragment {
	private TabManager  mTabManager;
	private TabHost mTabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_tabs, container,false);
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
		mTabManager=new TabManager(this.getChildFragmentManager(),mTabHost,R.id.realtabcontent);
		Bundle args1=new Bundle();
		args1.putString("flag", "Simple1");
		mTabManager.addTab(mTabHost.newTabSpec("ItemFragment1").setIndicator("Simple1"), ItemFragment.class, args1);
		Bundle args2=new Bundle();
		args2.putString("flag", "Simple2");
		mTabManager.addTab(mTabHost.newTabSpec("ItemFragment2").setIndicator("Simple2"), ItemFragment.class, args2);
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroyView() {
		mTabManager.destroy();
		super.onDestroyView();
	}
}
