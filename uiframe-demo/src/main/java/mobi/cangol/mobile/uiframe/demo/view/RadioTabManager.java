package mobi.cangol.mobile.uiframe.demo.view;

import java.util.HashMap;

import mobi.cangol.mobile.base.BaseFragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public class RadioTabManager implements OnCheckedChangeListener {
	private static final String CUR_TAG = "RadioTabManager_TabId";
    protected final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    private TabInfo mLastTab;
    private  FragmentManager mFragmentManager;
    private final RadioGroup mRadioGroup;
    private final int mContainerId;
    public static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        public Bundle args;
        public BaseFragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    public RadioTabManager(FragmentManager fragmentManager, RadioGroup radioGroup, int containerId) {
    	mFragmentManager =  fragmentManager;
        mRadioGroup = radioGroup;
        mContainerId = containerId;
        mRadioGroup.setOnCheckedChangeListener(this);
    }
	public void setFragmentManager(FragmentManager mFragmentManager) {
		this.mFragmentManager = mFragmentManager;
	}

	public void onSaveState(Bundle outState) {
		outState.putInt(CUR_TAG, mRadioGroup.getCheckedRadioButtonId());
	}

	public void restoreState(Bundle state) {
		int tabId=state.getInt(CUR_TAG);
		//setCurrentTab(tabId);
	}
    public void setCurrentTab(int checkedId){
    	((RadioButton) mRadioGroup.findViewById(checkedId)).setChecked(true);
    }
    public BaseFragment getCurrentTab(){
    	if(mLastTab!=null){
    		return mLastTab.fragment;
    	}
    	return null;
    }
    public void addTab(int radioButtonId, Class<?> clss, String tag,Bundle args) {
        String tabId = ""+radioButtonId;

        TabInfo info = new TabInfo(tag, clss, args);
        info.fragment = (BaseFragment)mFragmentManager.findFragmentByTag(tag);
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft =mFragmentManager.beginTransaction();
            ft.detach(info.fragment);
            ft.commit();
        }
        mTabs.put(tabId, info);
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		 TabInfo newTab = mTabs.get(""+checkedId);
         if (mLastTab != newTab) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = (BaseFragment) Fragment.instantiate(group.getContext(),
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
