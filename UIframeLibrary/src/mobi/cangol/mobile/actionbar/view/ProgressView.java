package mobi.cangol.mobile.actionbar.view;

import mobi.cangol.mobile.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
* @Description: 
* @version $Revision: 1.0 $ 
* @author xuewu.wei
* @date: 2014-6-5
*/

public class ProgressView extends LinearLayout {
	private static final String	TAG = "ProgressView";
	private ProgressBar mProgressBar;
	private TextView mTextView;
	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.actionbar_progress_view, this, true);
        initViews();
	}
	private void initViews(){
		mProgressBar=(ProgressBar) this.findViewById(R.id.actionbar_progress);
		mTextView=(TextView) this.findViewById(R.id.actionbar_progress_text);
	}
	
	public void setProgressText(String text){
		mTextView.setText(text);
	}
	
	public void setProgressText(int resId){
		mTextView.setText(resId);
	}
	
	public void startProgress(){
		this.setVisibility(View.VISIBLE);
		this.startAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_in_top));
	}
	
	public void stopProgress(){
		this.setVisibility(View.GONE);
		this.startAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_out_top));
	}

	public boolean isProgress(){
		return this.getVisibility()==View.VISIBLE;
	}
}
