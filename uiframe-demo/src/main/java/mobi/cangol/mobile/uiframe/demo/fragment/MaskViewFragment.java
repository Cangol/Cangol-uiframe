package mobi.cangol.mobile.uiframe.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.demo.R;

public class MaskViewFragment extends BaseContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_maskview, container, false);
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
        findViewById(R.id.button0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseNavigationFragmentActivity)getActivity()).getMaskView().setBackgroundColor(Color.parseColor("#9F000000"));
                ((BaseNavigationFragmentActivity)getActivity()).displayMaskView(true);
            }

        });

        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseNavigationFragmentActivity)getActivity()).displayMaskView(false);
            }

        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }
    @Override
    public void onDestroyView() {
        ((BaseNavigationFragmentActivity)getActivity()).displayMaskView(false);
        super.onDestroyView();
    }
}
