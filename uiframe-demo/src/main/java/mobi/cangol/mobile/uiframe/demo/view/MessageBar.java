package mobi.cangol.mobile.uiframe.demo.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mobi.cangol.mobile.uiframe.demo.R;

public class MessageBar extends LinearLayout{
	private View mMessageBarView;
	private TextView mTextTv;
	private ImageView mCloseBtn;
	private Context mContext;
	private Intent mIntent;
	private OnClickAction mOnClickAction;
	public MessageBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		this.setOrientation(LinearLayout.VERTICAL);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMessageBarView = mInflater.inflate(R.layout.common_messagebar_view, this);
		mCloseBtn = (ImageView) mMessageBarView.findViewById(R.id.messagebar_close);
		mTextTv = (TextView) mMessageBarView.findViewById(R.id.messagebar_text);
		initViews();
		this.setVisibility(View.GONE);
	}
	public void initViews(){
		mCloseBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mMessageBarView.setVisibility(View.GONE);
			}
		});
		mTextTv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mMessageBarView.setVisibility(View.GONE);
				if(mIntent!=null){
					mContext.startActivity(mIntent);
					mIntent=null;
				}
				if(mOnClickAction!=null)mOnClickAction.OnClick();
			}
		});
	}
	public void setMessage(CharSequence msg) {
		mTextTv.setText(msg);
		mMessageBarView.setVisibility(View.VISIBLE);
		this.mIntent=null;
	}
	public void setMessage(int resid) {
		mTextTv.setText(resid);
		mMessageBarView.setVisibility(View.VISIBLE);
		this.mIntent=null;
	}
	public void setMessage(CharSequence msg,Intent intent) {
		mTextTv.setText(msg);
		mMessageBarView.setVisibility(View.VISIBLE);
		this.mIntent=intent;
	}
	public void setMessage(int resid,Intent intent) {
		mTextTv.setText(resid);
		mMessageBarView.setVisibility(View.VISIBLE);
		this.mIntent=intent;
	}
	public void setMessage(int resid,OnClickAction mOnClickAction) {
		mTextTv.setText(resid);
		mMessageBarView.setVisibility(View.VISIBLE);
		this.mOnClickAction = mOnClickAction;
	}
	public void setMessage(CharSequence msg,OnClickAction mOnClickAction) {
		mTextTv.setText(msg);
		mMessageBarView.setVisibility(View.VISIBLE);
		this.mOnClickAction = mOnClickAction;
	}
	public interface OnClickAction{
		void OnClick();
	}
}
