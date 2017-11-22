package mobi.cangol.mobile.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import mobi.cangol.mobile.logging.Log;

/**
 * Created by xuewu.wei on 2017/11/22.
 */

public class FragmentLifecycleCallbacksLog extends FragmentManager.FragmentLifecycleCallbacks {
    protected final static String TAG = "Lifecycle";
    private static final boolean LIFECYCLE = Log.getLevel() >= android.util.Log.VERBOSE;
    @Override
    public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentPreAttached(fm, f, context);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onPreAttached");
    }

    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentAttached(fm, f, context);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onAttached");
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentCreated(fm, f, savedInstanceState);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onCreated");
    }

    @Override
    public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onActivityCreated");
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onViewCreated");
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        super.onFragmentStarted(fm, f);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onStarted");
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        super.onFragmentResumed(fm, f);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onResumed");
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        super.onFragmentPaused(fm, f);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onPaused");
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        super.onFragmentStopped(fm, f);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onStopped");
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        super.onFragmentSaveInstanceState(fm, f, outState);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onSaveInstanceState");
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onViewDestroyed");
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentDestroyed(fm, f);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onDestroyed");
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        super.onFragmentDetached(fm, f);
        if(LIFECYCLE)Log.d(TAG,f.getTag()+" onDetached");
    }
}
