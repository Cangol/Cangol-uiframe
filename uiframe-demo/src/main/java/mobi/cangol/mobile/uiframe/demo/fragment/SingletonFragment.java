package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;

public class SingletonFragment extends BaseContentFragment {
    private int sno;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            sno = this.getArguments().getInt("sno", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_single, container, false);
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
        this.setTitle("Single_" + sno);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        findViewById(R.id.button0).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("sno", sno + 1);
                setContentFragment(SingletonFragment.class, "Singleton_" + (sno + 1), bundle);
            }

        });

        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("sno", sno + 1);
                setContentFragment(SingletonFragment.class, "Singleton", bundle);
            }

        });
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
