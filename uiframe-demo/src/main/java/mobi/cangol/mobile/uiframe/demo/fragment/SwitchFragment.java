package mobi.cangol.mobile.uiframe.demo.fragment;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.demo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SwitchFragment extends BaseContentFragment {
	
	private Button mButton1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_switch, container,false);
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findViews(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initFragmentStack(R.id.switch_container);
		initViews(savedInstanceState);
		initData(savedInstanceState);
		switchFragment();
	}
	@Override
	protected void initData(Bundle savedInstanceState) {
	}

	@Override
	protected void findViews(View view) {
		mButton1=(Button) view.findViewById(R.id.button1);

	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		mButton1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				switchFragment();
			}
			
		});

	}
	private boolean mIsDownload=true;
	protected void switchFragment() {
		Bundle args=new Bundle();
		if(!mIsDownload){
			args.putString("flag", "Up");
			replaceChildFragment(ItemFragment.class, "ItemFragment1", args);
		}else{
			args.putString("flag", "Down");
			replaceChildFragment(ItemFragment.class, "ItemFragment2", args);
		}
		mIsDownload = !mIsDownload;
	}
	
}
