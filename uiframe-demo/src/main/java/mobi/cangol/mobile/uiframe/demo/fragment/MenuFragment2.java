package mobi.cangol.mobile.uiframe.demo.fragment;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.BaseMenuFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.demo.ModuleMenuIDS;
import mobi.cangol.mobile.uiframe.R;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuFragment2 extends BaseMenuFragment{
	
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
		View v = inflater.inflate(R.layout.fragment_menu2, container,false);
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
				setContentFragment(HomeFragment.class, "HomeFragment",null,ModuleMenuIDS.MODULE_HOME);
			}
		
		});
		textView2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(ListDataFragment.class, "ListFragment",null,ModuleMenuIDS.MODULE_LIST);
			}
		
		});
		textView3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(SettingFragment.class, "SettingFragment",null,ModuleMenuIDS.MODULE_SETTING);
			}
		
		});
		textView4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(SwitchFragment.class, "SwitchFragment",null,ModuleMenuIDS.MODULE_SWITCH);
			}
		
		});
		textView5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(TabsFragment.class, "TabsFragment",null,ModuleMenuIDS.MODULE_TABS);
			}
		
		});
		textView6.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setContentFragment(TabPagesFragment.class, "TabPagesFragment",null,ModuleMenuIDS.MODULE_PAGES);
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
		switch(moduleId){
			case ModuleMenuIDS.MODULE_HOME:
				textView1.setTextColor(Color.RED);
				textView2.setTextColor(Color.BLACK);
				textView3.setTextColor(Color.BLACK);
				textView4.setTextColor(Color.BLACK);
				textView5.setTextColor(Color.BLACK);
				textView6.setTextColor(Color.BLACK);
				break;
			case ModuleMenuIDS.MODULE_LIST:
				textView1.setTextColor(Color.BLACK);
				textView2.setTextColor(Color.RED);
				textView3.setTextColor(Color.BLACK);
				textView4.setTextColor(Color.BLACK);
				textView5.setTextColor(Color.BLACK);
				textView6.setTextColor(Color.BLACK);
				break;
			case ModuleMenuIDS.MODULE_SETTING:
				textView1.setTextColor(Color.BLACK);
				textView2.setTextColor(Color.BLACK);
				textView3.setTextColor(Color.RED);
				textView4.setTextColor(Color.BLACK);
				textView5.setTextColor(Color.BLACK);
				textView6.setTextColor(Color.BLACK);
				break;
			case ModuleMenuIDS.MODULE_SWITCH:
				textView1.setTextColor(Color.BLACK);
				textView2.setTextColor(Color.BLACK);
				textView3.setTextColor(Color.BLACK);
				textView4.setTextColor(Color.RED);
				textView5.setTextColor(Color.BLACK);
				textView6.setTextColor(Color.BLACK);
				break;
			case ModuleMenuIDS.MODULE_TABS:
				textView1.setTextColor(Color.BLACK);
				textView2.setTextColor(Color.BLACK);
				textView3.setTextColor(Color.BLACK);
				textView4.setTextColor(Color.BLACK);
				textView5.setTextColor(Color.RED);
				textView6.setTextColor(Color.BLACK);
				break;
			case ModuleMenuIDS.MODULE_PAGES:
				textView1.setTextColor(Color.BLACK);
				textView2.setTextColor(Color.BLACK);
				textView3.setTextColor(Color.BLACK);
				textView4.setTextColor(Color.BLACK);
				textView5.setTextColor(Color.BLACK);
				textView6.setTextColor(Color.RED);
				break;
		}
	}
	@Override
	protected FragmentInfo getNavigtionUpToFragment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onOpen() {
		// TODO Auto-generated method stub
		
	}

}
