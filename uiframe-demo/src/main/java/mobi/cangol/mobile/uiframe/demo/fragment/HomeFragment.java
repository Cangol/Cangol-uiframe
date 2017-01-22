package mobi.cangol.mobile.uiframe.demo.fragment;

import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.model.Station;
import mobi.cangol.mobile.http.AsyncHttpClient;
import mobi.cangol.mobile.http.JsonHttpResponseHandler;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.parser.JsonUtils;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.cache.CacheManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends BaseContentFragment {
    private Button mButton1,mButton2;

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
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        CacheManager mCacheManager = (CacheManager) getAppService(AppService.CACHE_MANAGER);
        Station st = new Station();
        st.set_id(1);
        st.setName("test");
        Log.d("ff=" + JsonUtils.toJSONObject(st,false));
    }

    @Override
    protected void findViews(View view) {
        mButton1 = (Button) view.findViewById(R.id.button1);
        mButton2 = (Button) view.findViewById(R.id.button2);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.setTitle(this.getClass().getSimpleName());
        mButton2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                toFragmentForResult();
//				 Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
//		         startActivityForResult(intent,1);
                final CommonDialogFragment.Builder builder=new CommonDialogFragment.Builder(getActivity());
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
        mButton1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               toFragmentForResult();
            }

        });
        AsyncHttpClient mAsyncHttpClient = AsyncHttpClient.build("11");
        mAsyncHttpClient.get(this.getActivity(), "http://192.168.2.62:83/index.php/friend/NewFriendLis", null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                Log.d("onStart");
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Log.d("onSuccess");
            }

            @Override
            public void onFailure(Throwable e, String errorResponse) {
                Log.d("onFailure");
            }

        });
    }

    private void toFragmentForResult() {
        replaceFragment(DetailsFragment.class, "DetailsFragment", new Bundle(), 1);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode=" + requestCode + ",resultCode=" + requestCode + ",data=" + data);
        switch (requestCode) {
            case 1:
                showToast("onFragmentResult resultCode=" + resultCode);
                mButton1.setText("OK");
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
    public boolean isCleanStack() {
        return true;
    }
}
