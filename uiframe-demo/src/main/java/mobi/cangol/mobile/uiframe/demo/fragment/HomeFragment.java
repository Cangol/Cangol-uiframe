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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //return onRetainView(R.layout.fragment_home, container,false);
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
        list.add(ActionMenuFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(SingletonFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(TagFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(ResultFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(PopBackFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(SwitchAnimFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(TabsFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(TabPagesFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(DialogFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(MaskViewFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(HighLightFragment.class.getSimpleName().replace("Fragment", ""));
        if (getActivity() instanceof TabDrawerNavigationFragmentActivity)
            list.add(DrawerFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(WebFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(ListFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(RecyclerViewFragment.class.getSimpleName().replace("Fragment", ""));
        list.add(InputFragment.class.getSimpleName().replace("Fragment", ""));
        mListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                String className = "mobi.cangol.mobile.uiframe.demo.fragment." + name + "Fragment";
                try {
                    if("Result".equals(name))
                        replaceFragmentForResult((Class<? extends BaseContentFragment>) Class.forName(className), name + "Fragment", null,1);
                    else
                        setContentFragment((Class<? extends BaseContentFragment>) Class.forName(className), name + "Fragment", null);
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
