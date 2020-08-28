package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;

public class ListFragment extends BaseContentFragment {
    private ListView mListView;
    private DataAdapter mDataAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataAdapter=new DataAdapter();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_list, container, false);
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
        this.setTitle(this.getClass().getSimpleName());
        mListView=findViewById(R.id.listView);
        mListView.setAdapter(mDataAdapter);
        mListView .setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                Bundle bundle=new Bundle();
                bundle.putString("flag",name);
                setContentFragment(ItemFragment.class,"ItemFragment_"+name,bundle);
            }
        });
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getUiHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isEnable())return;
                String[] array=getResources().getStringArray(R.array.sts);
                List<String> temp=new ArrayList<>();
                for (int i = 0; i <array.length ; i++) {
                    temp.add(array[i]);
                }
                mDataAdapter.cleanAddAll(temp);
                Log.d(TAG,"cleanAddAll "+temp.size());
            }
        },3000L);
    }

    private static class DataAdapter extends BaseAdapter {
        private List<String> items;

        DataAdapter() {
            super();
            items = new ArrayList<>();
        }

        DataAdapter(List<String> items) {
            super();
            this.items = items;
        }

        void cleanAddAll(List<String> list) {
            items.clear();
            items.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView textView=view.findViewById(android.R.id.text1);
            textView.setText(String.valueOf(getItem(position)));
            return textView;
        }
    }
}
