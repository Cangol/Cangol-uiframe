package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.BaseFragment;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.uiframe.demo.R;


public class CleanFragment extends BaseContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        Log.d(TAG,""+getApp());
    }


    @Override
    protected void findViews(View view) {
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.setTitle(this.getClass().getSimpleName());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean isCleanStack() {
        return true;
    }
}
