package mobi.cangol.mobile.actionbar;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.util.AttributeSet;


public class ActionMenuInflater {

	private ActionMenu mActionMenu;
	private Activity mActivity;
	
	public ActionMenuInflater(ActionMenu menu, Activity mActivity) {
		mActionMenu=menu;
		mActivity=mActivity;
	}

	public void inflater(int menuRes,ActionMenu menu) {

	}
	
	public void parseMenu(XmlPullParser parser, AttributeSet attrs, ActionMenu menu){
		 
	}
}
