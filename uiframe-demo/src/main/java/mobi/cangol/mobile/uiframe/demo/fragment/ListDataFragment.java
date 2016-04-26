package mobi.cangol.mobile.uiframe.demo.fragment;

import java.util.List;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.R;
import mobi.cangol.mobile.uiframe.demo.db.StationService;
import mobi.cangol.mobile.uiframe.demo.model.Station;
import mobi.cangol.mobile.logging.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListDataFragment extends BaseContentFragment {
	StationService stationService;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stationService=new StationService(this.getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_list, container,false);
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
		Station staticon=new Station();
		staticon.setName("aaaa");
		stationService.save(staticon);
		stationService.getCount();
		List<Station> list=stationService.findList(2, 10);
		for(int i=0;i<list.size();i++){
			Log.d(i+":"+list.get(i));
		}
	}

	@Override
	protected void findViews(View view) {
		this.setTitle(this.getClass().getSimpleName());

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
		// TODO Auto-generated method stub
		return true;
	}
	
}
