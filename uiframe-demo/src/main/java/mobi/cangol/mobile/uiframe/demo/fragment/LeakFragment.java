
package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hugo.weaving.DebugLog;
import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.uiframe.demo.R;

@DebugLog
public class LeakFragment extends BaseContentFragment {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_leak, container,false);
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
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postRunnable(new Runnable(){
					public void run(){
						Log.d("sleep start "+this.hashCode());
						try {
							Thread.sleep(10000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Log.d("sleep stop "+this.hashCode());
					}
				});
			}
		});
		findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postRunnable(new StaticInnerRunnable(){
					public void run(){
						Log.d("sleep start "+this.hashCode());
						try {
							Thread.sleep(15000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Log.d("sleep stop "+this.hashCode());
					}
				});
			}
		});
		findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncTask asyncTask=new AsyncTask() {
					@Override
					protected Object doInBackground(Object[] params) {
						Log.d("sleep start "+this.hashCode());
						try {
							Thread.sleep(15000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Log.d("sleep stop "+this.hashCode());
						return null;
					}
				};
				asyncTask.execute();
			}
		});
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
	public void onDestroy() {
		handlerThread.getLooper().quit();
		getHandler().getLooper().quit();
		super.onDestroy();
	}
}
