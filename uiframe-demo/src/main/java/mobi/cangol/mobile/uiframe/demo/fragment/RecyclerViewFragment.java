package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;

public class RecyclerViewFragment extends BaseContentFragment {
    private RecyclerView mRecyclerView;
    private DataAdapter mDataAdapter;
    private NestedScrollView mScrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_recyleview, container, false);
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
        mRecyclerView = findViewById(R.id.recyclerView);
        //mScrollView=findViewById(R.id.scrollView);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.setTitle(this.getClass().getSimpleName());
        mDataAdapter = new DataAdapter(Arrays.asList(getResources().getStringArray(R.array.sts)));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mDataAdapter);
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
    }

    private static class DataAdapter extends RecyclerView.Adapter {
        private List<String> items;

        DataAdapter(List<String> items) {
            super();
            this.items = items;
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
