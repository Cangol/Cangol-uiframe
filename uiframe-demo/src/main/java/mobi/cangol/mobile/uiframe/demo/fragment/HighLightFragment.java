package mobi.cangol.mobile.uiframe.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.demo.R;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.shape.RectLightShape;
import zhy.com.highlight.view.HightLightView;

public class HighLightFragment extends BaseContentFragment {
    private HighLight mHighLight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_hightlight, container, false);
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
                showHighLight();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHighLight != null) mHighLight.remove();
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    private void showHighLight() {
        mHighLight = new HighLight(getContext())
                .autoRemove(true)
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {
                        Toast.makeText(getContext(), "clicked and remove HightLight view by yourself", Toast.LENGTH_SHORT).show();
                        mHighLight.remove();
                    }
                })
                .maskColor(Color.parseColor("#6f000000"))
                .anchor(((BaseNavigationFragmentActivity) getActivity()).getMaskView().getRootView())
                .setOnShowCallback(new HighLightInterface.OnShowCallback() {
                    @Override
                    public void onShow(HightLightView hightLightView) {
                        ((BaseNavigationFragmentActivity) getActivity()).displayMaskView(true);
                    }
                })
                .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {
                    @Override
                    public void onRemove() {
                        ((BaseNavigationFragmentActivity) getActivity()).displayMaskView(false);
                    }
                })
                .addHighLight(R.id.button0, R.layout.hight_layout, new OnLeftPosCallback(45), new RectLightShape());

        mHighLight.show();
    }

    @Override
    public void onDestroyView() {
        if (mHighLight != null) mHighLight.remove();
        super.onDestroyView();
    }
}
