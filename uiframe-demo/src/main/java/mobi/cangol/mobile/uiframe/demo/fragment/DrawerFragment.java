package mobi.cangol.mobile.uiframe.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.navigation.TabDrawerNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.demo.R;

public class DrawerFragment extends BaseContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_drawer, container, false);
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
    protected void findViews(View view) {
    }

    protected void initViews(Bundle savedInstanceState) {
        this.setTitle(this.getClass().getSimpleName());
        if(getActivity() instanceof TabDrawerNavigationFragmentActivity){
            findViewById(R.id.button0).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof TabDrawerNavigationFragmentActivity)
                        ((TabDrawerNavigationFragmentActivity)getActivity()).setDrawer(Gravity.RIGHT,ListFragment.class, null);

                }

            });
            findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof TabDrawerNavigationFragmentActivity)
                        showToast(""+((TabDrawerNavigationFragmentActivity)getActivity()).getDrawer(Gravity.RIGHT));

                }

            });
            findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof TabDrawerNavigationFragmentActivity)
                        ((TabDrawerNavigationFragmentActivity)getActivity()).showDrawer(Gravity.RIGHT,true);

                }

            });
            findViewById(R.id.button3).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof TabDrawerNavigationFragmentActivity)
                        ((TabDrawerNavigationFragmentActivity)getActivity()).removeDrawer(Gravity.RIGHT);

                }

            });
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }
}
