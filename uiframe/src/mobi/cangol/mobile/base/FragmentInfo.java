package mobi.cangol.mobile.base;

import android.os.Bundle;

public  class FragmentInfo {
    final String tag;
    final Class<? extends BaseFragment> clss;
    final Bundle args;

    public FragmentInfo(Class<? extends BaseFragment> _class,String _tag,Bundle _args) {
        tag = _tag;
        clss = _class;
        args = _args;
    }
    public FragmentInfo(BaseFragment fragment) {
    	tag = fragment.getTag();
        clss = fragment.getClass();
        args = fragment.getArguments();
    }
	@Override
	public String toString() {
		return "FragmentInfo [tag=" + tag + ", clss=" + clss + ", args=" + args
				+ "]";
	}
    
}
