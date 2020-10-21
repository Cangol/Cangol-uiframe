package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;

public class RecyclerViewFragment extends BaseContentFragment {
    private RecyclerView mRecyclerView;
    private DataAdapter mDataAdapter;
    private NestedScrollView mScrollView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataAdapter=new DataAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //return onRetainView(R.layout.fragment_recyleview, container, false);
        return inflater.inflate(R.layout.fragment_recyleview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews(getView());
        initViews(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void findViews(View view) {
        mRecyclerView = findViewById(R.id.recyclerView);
        mScrollView=findViewById(R.id.scrollView);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.setTitle(this.getClass().getSimpleName());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mDataAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mDataAdapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                String name = mDataAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("flag", name);
                setContentFragment(ItemFragment.class, "ItemFragment_" + name, bundle);
            }
        });
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

    private static class DataAdapter extends RecyclerView.Adapter {
        private List<String> items;
        DataAdapter() {
            items=new ArrayList<>();
        }
        DataAdapter(List<String> items) {
            super();
            this.items = items;
        }
        void cleanAddAll(List<String> list){
            items.clear();
            items.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            ((TextView) viewHolder.itemView.findViewById(android.R.id.text1)).setText(getItem(i));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClicked(v, i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        String getItem(int position) {
            return items.get(position);
        }

        private OnItemClickListener onItemClickListener;

        void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public interface OnItemClickListener {
            void onItemClicked(View view, int position);
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
