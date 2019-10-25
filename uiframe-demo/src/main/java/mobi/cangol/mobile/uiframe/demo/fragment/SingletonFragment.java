package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.uiframe.demo.R;

public class SingletonFragment extends BaseContentFragment {
    protected final String TAG = Log.makeLogTag(this.getClass());
    private int sno = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        sno = getArguments().getInt("sno", 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_singleton, container, false);
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

    }

    @Override
    protected FragmentInfo getNavigtionUpToFragment() {
        return null;
    }

    @Override
    protected void findViews(View view) {
        this.setTitle(this.getClass().getSimpleName() + sno);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("flag",""+ sno + 1);
                setContentFragment(ItemFragment.class, "ItemFragment_" + (sno + 1), bundle);

            }

        });
        findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("flag", ""+sno + 1);
                setContentFragment(SingletonFragment.class, "SingletonFragment", bundle);

            }

        });
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public boolean isCleanStack() {
        return false;
    }

}
