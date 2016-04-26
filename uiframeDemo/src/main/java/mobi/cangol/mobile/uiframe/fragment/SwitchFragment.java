package mobi.cangol.mobile.uiframe.fragment;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SwitchFragment extends BaseContentFragment {
	
	private Button mButton1;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_switch, container,false);
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
		initFragmentStack(R.id.switch_container);
		initViews(savedInstanceState);
		initData(savedInstanceState);
		swictherFragement();
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
				swictherFragement();
			}
			
		});

	}
	private boolean mIsDownload=true;
	protected void swictherFragement() {
		Bundle args=new Bundle();
		if(!mIsDownload){
			args.putBoolean("mIsDownload", mIsDownload);
			replaceChildFragment(ItemFragment.class, "ItemFragment1", args);
		}else{
			args.putBoolean("mIsDownload", mIsDownload);
			replaceChildFragment(ItemFragment.class, "ItemFragment2", args);
		}
		mIsDownload = !mIsDownload;
	}
	
	@Override
	protected FragmentInfo getNavigtionUpToFragment() {
		return null;
	}
	
	@Override
	public boolean isCleanStack() {
		return true;
	}
}
