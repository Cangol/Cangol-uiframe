package mobi.cangol.mobile.uiframe.demo.fragment;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.SecondActivity;
import mobi.cangol.mobile.uiframe.demo.Singleton;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class NextFragment extends BaseContentFragment {
	protected  final String TAG = Log.makeLogTag(this.getClass());
	private int sno=1;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sno=getArguments().getInt("sno",1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_next, container,false);
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
		this.setTitle(this.getClass().getSimpleName()+sno);
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		findViewById(R.id.button1).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popBackStack();
			}

		});
		findViewById(R.id.button2).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				popBackStackAll();
			}

		});
		findViewById(R.id.button3).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
					Bundle bundle=new Bundle();
					bundle.putInt("sno",sno+1);
					setContentFragment(NextFragment.class,"NextFragment_"+(sno+1),bundle);

			}

		});
		findViewById(R.id.button4).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getContext(), SecondActivity.class));

			}

		});
		findViewById(R.id.button5).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Singleton.getInstance().notifyTest();

			}

		});
	}

	@Override
	protected FragmentInfo getNavigtionUpToFragment() {
		return new FragmentInfo(ItemFragment.class,"ItemFragment",null);
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public boolean isCleanStack() {
		return false;
	}

}
