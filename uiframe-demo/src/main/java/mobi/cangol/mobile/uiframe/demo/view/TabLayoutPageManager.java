package mobi.cangol.mobile.uiframe.demo.view;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING;

/**
 * @author Cangol
 * @version $Revision: 1.0 $
 * @Description:
 */
public class TabLayoutPageManager extends FragmentStatePagerAdapter implements
        TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<TabInfo> mTabs = new ArrayList<>();
    private FragmentManager mFragmentManager;

    public TabLayoutPageManager(FragmentManager fragmentManager, TabLayout tabLayout, ViewPager pager) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;
        mContext = tabLayout.getContext();
        mTabLayout = tabLayout;
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(this);
    }
    public void addTab(int tabId, TabLayout.Tab tab, Class<?> clazz, Bundle args) {
        String tag = "" + tabId;
        tab.setTag(tag);

        TabInfo info = new TabInfo(tag, clazz, args);
        mTabs.add(info);

        mTabLayout.addTab(tab);
        notifyDataSetChanged();
    }

    public Fragment getCurrentTab() {
        TabInfo info = mTabs.get(mViewPager.getCurrentItem());
        return mFragmentManager.findFragmentByTag(info.tag);
    }
    public void removeTab(int tabId) {
        String tag = "" + tabId;
        if (!mTabs.contains(tag)){
            //not found~!
            return;
        }else{
            mTabs.remove(tag);
            mTabLayout.removeTabAt(tabId);
        }
    }

    public void destroy() {
        mFragmentManager = null;
        mTabs.clear();
        mTabLayout.removeOnTabSelectedListener(this);
        mViewPager.removeOnPageChangeListener(this);
        mContext = null;
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        mTabLayout.setScrollPosition(position,0,true);
    }

    public void onPageScrollStateChanged(int state) {
        if (mTabs.get(mViewPager.getCurrentItem()).fragment == null) return;
        switch (state) {
            case SCROLL_STATE_DRAGGING:
                mTabs.get(mViewPager.getCurrentItem()).fragment.setUserVisibleHint(false);
                break;
            case SCROLL_STATE_SETTLING:
                mTabs.get(mViewPager.getCurrentItem()).fragment.setUserVisibleHint(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        public Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }
}
