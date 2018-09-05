package mobi.cangol.mobile.uiframe.demo.fragment;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.demo.R;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemFragment extends BaseContentFragment {
	
	private String mFlag;
	private TextView mTextView1;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments()!=null)
		mFlag=this.getArguments().getString("flag");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_item, container,false);
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
		mTextView1=(TextView) view.findViewById(R.id.textView1);
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		mTextView1.setText(""+mFlag);
	}

	@Override
	protected FragmentInfo getNavigtionUpToFragment() {
		return null;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public boolean isCleanStack() {
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_F4&&event.getAction()==KeyEvent.ACTION_DOWN){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
