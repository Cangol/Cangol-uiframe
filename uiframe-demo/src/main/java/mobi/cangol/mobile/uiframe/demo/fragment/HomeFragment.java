package mobi.cangol.mobile.uiframe.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.navigation.TabDrawerNavigationFragmentActivity;
import mobi.cangol.mobile.parser.JsonUtils;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.cache.CacheManager;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.model.Station;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.shape.RectLightShape;
import zhy.com.highlight.view.HightLightView;

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
        findViewById(R.id.button0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //replaceFragment(LeakFragment.class, "LeakFragment", null);
                hightLight();
            }

        });
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
        if(getActivity() instanceof TabDrawerNavigationFragmentActivity){
            findViewById(R.id.button5).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof TabDrawerNavigationFragmentActivity)
                        ((TabDrawerNavigationFragmentActivity)getActivity()).setDrawer(Gravity.RIGHT,ListFragment.class, null);

                }

            });
            findViewById(R.id.button6).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof TabDrawerNavigationFragmentActivity)
                        showToast(""+((TabDrawerNavigationFragmentActivity)getActivity()).getDrawer(Gravity.RIGHT));

                }

            });
            findViewById(R.id.button7).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof TabDrawerNavigationFragmentActivity)
                        ((TabDrawerNavigationFragmentActivity)getActivity()).showDrawer(Gravity.RIGHT,true);

                }

            });
            findViewById(R.id.button8).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof TabDrawerNavigationFragmentActivity)
                        ((TabDrawerNavigationFragmentActivity)getActivity()).removeDrawer(Gravity.RIGHT);

                }

            });
        }else{
            findViewById(R.id.button5).setVisibility(View.GONE);
            findViewById(R.id.button6).setVisibility(View.GONE);
            findViewById(R.id.button7).setVisibility(View.GONE);
            findViewById(R.id.button8).setVisibility(View.GONE);
        }

        findViewById(R.id.button9).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseNavigationFragmentActivity)getActivity()).getMaskView().setBackgroundColor(Color.parseColor("#9F000000"));
                ((BaseNavigationFragmentActivity)getActivity()).displayMaskView(true);
            }

        });

        findViewById(R.id.button10).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseNavigationFragmentActivity)getActivity()).displayMaskView(false);
            }

        });
    }
    private HighLight mHighLight;
    private void hightLight(){
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
                .anchor(((BaseNavigationFragmentActivity)getActivity()).getMaskView().getRootView())
                .setOnShowCallback(new HighLightInterface.OnShowCallback() {
                    @Override
                    public void onShow(HightLightView hightLightView) {
                        ((BaseNavigationFragmentActivity)getActivity()).displayMaskView(true);
                    }
                })
                .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {
                    @Override
                    public void onRemove() {
                        ((BaseNavigationFragmentActivity)getActivity()).displayMaskView(false);
                    }
                })
                .addHighLight(R.id.button0,R.layout.hight_layout,new OnLeftPosCallback(45),new RectLightShape());

        ((BaseNavigationFragmentActivity)getActivity()).displayMaskView(true);
        mHighLight.show();
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
    public void onDestroy() {
        getHandler().getLooper().quit();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume "+this.hashCode());
    }

    @Override
    public boolean isCleanStack() {
        return true;
    }
}
