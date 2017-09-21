package mobi.cangol.mobile.uiframe.demo.fragment;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.BaseMenuFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.uiframe.demo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuLeftFragment extends BaseMenuFragment{
	public static final int MODULE_HOME=0;

	public static final int MODULE_LIST=1;

	public static final int MODULE_SETTING=2;

	public static final int MODULE_SWITCH=4;

	public static final int MODULE_TABS=5;

	public static final int MODULE_PAGES=6;
	public TextView textView1;
	public TextView textView2;
	public TextView textView3;
	public TextView textView4;
	public TextView textView5;
	public TextView textView6;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_menu_left, container,false);
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
		Log.d("getUserVisibleHint "+getUserVisibleHint());
	}
	
	@Override
	protected void initData(Bundle savedInstanceState) {
		if(savedInstanceState!=null){
			updateFocus(this.getCurrentModuleId());
		}
	}

	@Override
	protected void findViews(View v) {
		textView1=(TextView) v.findViewById(R.id.textView1);
		textView2=(TextView) v.findViewById(R.id.textView2);
		textView3=(TextView) v.findViewById(R.id.textView3);
		textView4=(TextView) v.findViewById(R.id.textView4);
		textView5=(TextView) v.findViewById(R.id.textView5);
		textView6=(TextView) v.findViewById(R.id.textView6);
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		textView1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(HomeFragment.class, "HomeFragment",null,MODULE_HOME);
			}
		
		});
		textView2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(ListFragment.class, "ListFragment",null,MODULE_LIST);
			}
		
		});
		textView3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(SettingFragment.class, "SettingFragment",null,MODULE_SETTING);
			}
		
		});
		textView4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(SwitchFragment.class, "SwitchFragment",null,MODULE_SWITCH);
			}
		
		});
		textView5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(TabsFragment.class, "TabsFragment",null,MODULE_TABS);
			}
		
		});
		textView6.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(TabPagesFragment.class, "TabPagesFragment",null,MODULE_PAGES);
			}
		
		});
	}
	public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass,String tag,Bundle args,int moduleId) {
		super.setContentFragment(fragmentClass,tag,args,moduleId);
		showMenu(false);
	}
	@Override
	protected void onContentChange(int moduleId) {
		if(this.getView()!=null){
			updateFocus(moduleId);
		}
	}

	private void updateFocus(int moduleId) {

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		Log.d("setUserVisibleHint "+isVisibleToUser);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("getUserVisibleHint "+getUserVisibleHint());
	}

	@Override
	protected FragmentInfo getNavigtionUpToFragment() {
		return null;
	}

	@Override
	protected void onClosed() {

	}

	@Override
	protected void onOpen() {

	}

}
