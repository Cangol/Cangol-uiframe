package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;

public class ActionMenuFragment extends BaseContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_action, container, false);
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
//                getCustomActionBar().getActionMenu()
//                        .addMenu(3, R.string.action_add, R.drawable.ic_action_delete, 1);
            }
        });
        getUiHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentFragment(CleanFragment.class,null);
            }
        },2000L);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
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
            case 3:
                showToast("add");
                break;
        }
        return super.onMenuActionSelected(action);
    }

}
