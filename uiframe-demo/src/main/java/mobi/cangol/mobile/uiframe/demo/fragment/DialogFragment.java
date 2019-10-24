package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;

public class DialogFragment extends BaseContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_dialog, container, false);
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
                final CommonDialogFragment.Builder builder = new CommonDialogFragment.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("测试Dialog");
                builder.setCenterButtonInfo("OK", new CommonDialogFragment.OnButtonClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                builder.create().show(getFragmentManager(), "CommonDialogFragment");
            }

        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }
}
