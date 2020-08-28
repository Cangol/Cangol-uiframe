package mobi.cangol.mobile.uiframe.demo.view;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

import java.util.HashMap;

/**
 * @author Cangol
 * @version $Revision: 1.0 $
 * @Description:
 */
public class TabManager implements TabHost.OnTabChangeListener {
    private  TabHost mTabHost;
    private  int mContainerId;
    private  HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    private TabInfo mLastTab;
    private  FragmentManager mFragmentManager;
    private  Context mContext;

    public static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        public Bundle args;
        public Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabManager(FragmentManager fragmentManager, TabHost tabHost, int containerId) {
        mFragmentManager = fragmentManager;
        mTabHost = tabHost;
        mContext = tabHost.getContext();
        mContainerId = containerId;
        mTabHost.setOnTabChangedListener(this);
    }

    public Fragment getCurrentTab() {
        if (mLastTab != null) {
            return mLastTab.fragment;
        }
        return null;
    }

    public void destroy() {
        mFragmentManager = null;
        mTabs.clear();
        mTabHost.clearAllTabs();
        mLastTab=null;
        mContext=null;
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);

        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state.  If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        info.fragment = mFragmentManager.findFragmentByTag(tag);
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.detach(info.fragment);
            ft.commit();
        }

        mTabs.put(tag, info);
        mTabHost.addTab(tabSpec);
    }

    public void onTabChanged(String tabId) {
        TabInfo newTab = mTabs.get(tabId);
        if (mLastTab != newTab) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(mContext,
                            newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }

            mLastTab = newTab;
            ft.commit();
            mFragmentManager.executePendingTransactions();
        }
    }
}
