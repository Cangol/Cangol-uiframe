package mobi.cangol.mobile.actionbar.view;

import java.util.List;

import mobi.cangol.mobile.R;
import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ActionMenuView extends LinearLayout implements OnClickListener, OnLongClickListener{
	
	private LayoutInflater mInflater;
    private LinearLayout mActionsView;
    private LinearLayout mPopupActionsView;
    private ImageView mMoreButton;
    private PopupWindow mPopuMenu;
    private ActionMenu mActionMenu;
    private OnActionClickListener mOnActionClickListener;
    private int mHideActions=0;
    public ActionMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.actionbar_menu_view, this, true);
        initViews();
        initPopuMenu(context);
	}
    
	public void setActionMenu(ActionMenu mActionMenu) {
		this.mActionMenu = mActionMenu;
	}

	private void initViews() {
		 mActionsView = (LinearLayout) this.findViewById(R.id.actionbar_menu_actions);
		 mMoreButton=(ImageView) this.findViewById(R.id.actionbar_menu_more);
	}
	private void initPopuMenu(Context context){
    	View popuLayout=mInflater.inflate(R.layout.actionbar_popup, null);
    	mPopupActionsView=(LinearLayout) popuLayout.findViewById(R.id.actionbar_popup_actions);
    	int width=(int) (180*context.getResources().getDisplayMetrics().density);
    	mPopuMenu=new PopupWindow(popuLayout, width, LayoutParams.WRAP_CONTENT, true);
    	mPopuMenu.setBackgroundDrawable(new BitmapDrawable()); 
        mMoreButton.setOnClickListener(new OnClickListener(){

 			@Override
 			public void onClick(View v) {
 				mPopuMenu.showAsDropDown(mMoreButton);
 			}
     		
     	});
	}
    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof ActionMenuItem) {
            final ActionMenuItem action = (ActionMenuItem) tag;
            if(mPopuMenu!=null&&mPopuMenu.isShowing()){
            	mPopuMenu.dismiss();
            }
            if(mOnActionClickListener!=null){
            	mOnActionClickListener.onActionClick(action);
            }
        }
    }
    
    @Override
	public boolean onLongClick(View view) {
    	final Object tag = view.getTag();
        if (tag instanceof ActionMenuItem) {
            final ActionMenuItem action = (ActionMenuItem) tag;
            Toast toast = Toast.makeText(this.getContext(), action.getText(), Toast.LENGTH_SHORT);
            int left=(mHideActions>0?mMoreButton.getWidth():0)+view.getWidth()*(mActionMenu.size()-mHideActions)-view.getLeft();
            toast.setGravity(Gravity.RIGHT | Gravity.TOP,  left,  this.getBottom());
            toast.show();
        }
		return true;
	}
    
	public void addActions(List<ActionMenuItem> actions) {
		for(int i=0;i<actions.size();i++){
			addAction(actions.get(i));
		}
	}
    
    /**
     * Adds a new {@link ActionMenuItem}.
     * @param action the action to add
     */
    public void addAction(ActionMenuItem action) {
    	
    	final int size = mActionMenu.size();
        int showActions=size-mHideActions;
        if(action.isShow()){
        	if(showActions<2){
       		 	mActionsView.addView(action.isIcon()?inflateActionIcon(action):inflateActionText(action));
       		 	mMoreButton.setVisibility(View.GONE);
    	   	}else{
    	   		mPopupActionsView.addView(inflateMenuAction(action));
    	   		mMoreButton.setVisibility(View.VISIBLE);
    	   		mHideActions++;
    	   	}
        }else{
        	mHideActions++;
        	mPopupActionsView.addView(inflateMenuAction(action));
	   		mMoreButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
    	mHideActions=0;
        mActionsView.removeAllViews();
        mPopupActionsView.removeAllViews();
        mMoreButton.setVisibility(View.GONE);
    }


    private View inflateActionIcon(ActionMenuItem action) {
        View view = mInflater.inflate(R.layout.actionbar_item_icon, mActionsView, false);

        ImageView labelView =
            (ImageView) view.findViewById(R.id.actionbar_item);
        labelView.setImageResource(action.getDrawable());
        view.setId(action.getId());
        view.setTag(action);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return view;
    }
    
    private View inflateActionText(ActionMenuItem action) {
        View view = mInflater.inflate(R.layout.actionbar_item_text, mActionsView, false);

        TextView labelView =(TextView) view.findViewById(R.id.actionbar_item);
        labelView.setText(action.getText());
        
        view.setId(action.getId());
        view.setTag(action);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return view;
    }
    
    private View inflateMenuAction(ActionMenuItem action) {
        View view = mInflater.inflate(R.layout.actionbar_popup_item, mPopupActionsView, false);
        TextView labelView =(TextView) view.findViewById(R.id.actionbar_popup_item_text);
        /**
        if(action.getDrawable()>0){
        	Drawable img=getResources().getDrawable(action.getDrawable());
        	img.setBounds(0, 0, img.getIntrinsicWidth(), img.getIntrinsicHeight());
        	labelView.setCompoundDrawables(img, null, null, null);
        }**/
        labelView.setText(action.getText());
        view.setId(action.getId());
        view.setTag(action);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return view;
    }
    

	public void setOnActionClickListener(OnActionClickListener mOnActionClickListener) {
		this.mOnActionClickListener = mOnActionClickListener;
	}

	public interface OnActionClickListener {
    	
		boolean onActionClick(ActionMenuItem action);
    }
}
