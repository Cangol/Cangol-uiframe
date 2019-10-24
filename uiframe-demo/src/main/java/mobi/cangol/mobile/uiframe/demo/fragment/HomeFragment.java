package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.navigation.TabDrawerNavigationFragmentActivity;
import mobi.cangol.mobile.uiframe.demo.R;

public class HomeFragment extends BaseContentFragment {
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        mListView = findViewById(R.id.listView);

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.setTitle(this.getClass().getSimpleName());
        List<String> list = new ArrayList<>();
        list.add(ActionFragment.class.getSimpleName());
        list.add(TabsFragment.class.getSimpleName());
        list.add(TabPagesFragment.class.getSimpleName());
        list.add(SwitchFragment.class.getSimpleName());
        list.add(DialogFragment.class.getSimpleName());
        list.add(MaskViewFragment.class.getSimpleName());
        list.add(HighLightFragment.class.getSimpleName());
        list.add(InputFragment.class.getSimpleName());
        list.add(ResultFragment.class.getSimpleName());
        list.add(SingletonFragment.class.getSimpleName());
        list.add(PopBackFragment.class.getSimpleName());
        list.add(WebFragment.class.getSimpleName());
        if (getActivity() instanceof TabDrawerNavigationFragmentActivity)
            list.add(DrawerFragment.class.getSimpleName());

        mListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String className = (String) parent.getItemAtPosition(position);
                try {
                    setContentFragment((Class<? extends BaseContentFragment>) Class.forName("mobi.cangol.mobile.uiframe.demo.fragment." + className), className, null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public boolean isCleanStack() {
        return true;
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
}
