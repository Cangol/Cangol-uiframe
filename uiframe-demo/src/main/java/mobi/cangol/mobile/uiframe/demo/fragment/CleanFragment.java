package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.uiframe.demo.R;

import static mobi.cangol.mobile.uiframe.demo.fragment.MenuFragment.MODULE_CLEAN;
import static mobi.cangol.mobile.uiframe.demo.fragment.MenuFragment.MODULE_HOME;

public class CleanFragment extends BaseContentFragment {
    private int sno;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
           sno=getArguments().getInt("sno") ;
        }
    }

    @Override
    public void onNewBundle(Bundle bundle) {
        super.onNewBundle(bundle);
        if(bundle!=null){
            sno=bundle.getInt("sno") ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_result, container, false);
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

    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.setTitle(this.getClass().getSimpleName());
        ((TextView)findViewById(R.id.textView1)).setText("Click me "+sno);
        findViewById(R.id.textView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentFragment(ResultFragment.class, ResultFragment.class.getName(), null);
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
}
