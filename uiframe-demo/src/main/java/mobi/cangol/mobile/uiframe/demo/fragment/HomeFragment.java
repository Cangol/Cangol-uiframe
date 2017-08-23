package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.parser.JsonUtils;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.cache.CacheManager;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.model.Station;

public class HomeFragment extends BaseContentFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
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
        getActionBarActivity().setActionbarShow(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        CacheManager mCacheManager = (CacheManager) getAppService(AppService.CACHE_MANAGER);
        Station st = new Station();
        st.set_id(1);
        st.setName("test");
        Log.d("ff=" + JsonUtils.toJSONObject(st, false));
    }

    @Override
    protected void findViews(View view) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.setTitle(this.getClass().getSimpleName());
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentForResult(ResultFragment.class, "ResultFragment", null, 1);
            }

        });
        findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

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

        findViewById(R.id.button3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("sno", 1);
                setContentFragment(NextFragment.class, "NextFragment_" + 1, bundle);

            }

        });
        findViewById(R.id.button4).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("sno", 1);
                setContentFragment(SingletonFragment.class, "SingletonFragment", bundle);

            }

        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode=" + requestCode + ",resultCode=" + requestCode + ",data=" + data);
        switch (requestCode) {
            case 1:
                showToast("onFragmentResult resultCode=" + resultCode);
                break;
        }
    }

    @Override
    public boolean onMenuActionCreated(ActionMenu actionMenu) {
        super.onMenuActionCreated(actionMenu);
        actionMenu.addMenu(1, R.string.action_setting, R.drawable.ic_action_delete, 1);
        actionMenu.addMenu(2, R.string.action_selectAll, R.drawable.ic_action_select, 1);
        return true;
    }

    @Override
    public boolean onMenuActionSelected(ActionMenuItem action) {
        switch (action.getId()) {
            case 1:
                showToast("setting");
                break;
            case 2:
                getActionBarActivity().startSearchMode();
                break;
        }
        return super.onMenuActionSelected(action);
    }

    @Override
    public FragmentInfo getNavigtionUpToFragment() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean isCleanStack() {
        return true;
    }
}
