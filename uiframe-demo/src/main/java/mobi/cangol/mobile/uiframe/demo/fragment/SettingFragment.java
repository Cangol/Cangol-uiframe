package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.demo.R;

public class SettingFragment extends BaseContentFragment {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_setting, container,false);
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

	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putInt("flag",1);
				setContentFragment(ItemFragment.class,"ItemFragment_"+1,bundle);

			}

		});
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
