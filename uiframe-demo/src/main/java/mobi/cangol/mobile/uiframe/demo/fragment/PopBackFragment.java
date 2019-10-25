package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;

public class PopBackFragment extends BaseContentFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_pop, container, false);
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
        findViewById(R.id.button0).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popBackStackImmediate();
            }

        });
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popBackStack();
            }

        });
        findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popBackStackAll();
            }

        });

        findViewById(R.id.button3).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               popBackStack("PopBackFragment",-1);
            }

        });
        findViewById(R.id.button4).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popBackStackImmediate("PopBackFragment",-1);
            }

        });
    }
}
