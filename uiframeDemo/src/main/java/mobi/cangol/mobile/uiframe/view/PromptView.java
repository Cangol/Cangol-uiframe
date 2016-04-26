package mobi.cangol.mobile.uiframe.view;

import mobi.cangol.mobile.uiframe.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public class PromptView extends LinearLayout {
	private View retryLayout;
	private View tipsLayout;
	private View loadingLayout;
	private TextView retryText;
	private TextView tipsText;
	private TextView loadingText;
	public PromptView(Context context) {
		super(context);
		init(context);
	}
	public PromptView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.setGravity(Gravity.CENTER);
		this.setVisibility(View.GONE);
		this.setOrientation(LinearLayout.VERTICAL);
		retryLayout=inflater.inflate(R.layout.common_view_retry, null);
		tipsLayout=inflater.inflate(R.layout.common_view_tips, null);
		loadingLayout=inflater.inflate(R.layout.common_view_loading, null);
		retryText=(TextView) retryLayout.findViewById(R.id.common_retry_text);
		tipsText=(TextView) tipsLayout.findViewById(R.id.common_tips_text);
		loadingText=(TextView) loadingLayout.findViewById(R.id.common_loading_text);
		this.addView(tipsLayout);
		this.addView(retryLayout);
		this.addView(loadingLayout);
		
	}
	
	public void showContent(){
		tipsLayout.setVisibility(View.GONE);
		retryLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
		this.setVisibility(View.GONE);
	}
	
	public void showLoading(){
		tipsLayout.setVisibility(View.GONE);
		retryLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.VISIBLE);
		this.setVisibility(View.VISIBLE);
	}
	
	public void showLoading(String str){
		showLoading();
		loadingText.setText(str);
	}
	
	public void showLoading(int resId){
		loadingText.setText(resId);
	}
	
	private void showTips(){
		tipsLayout.setVisibility(View.VISIBLE);
		retryLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
		this.setVisibility(View.VISIBLE);
	}
	public void showTips(String str,Drawable topIcon){
		showTips();
		tipsText.setText(str);
		tipsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tipsText.setCompoundDrawables(null,topIcon,null,null);
	}
	
	public void showTips(int resId,Drawable topIcon){
		showTips();
		tipsText.setText(resId);
		tipsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tipsText.setCompoundDrawables(null,topIcon,null,null);
	}
	
	/***设置无数据提示***/
	public void showEmpty(){
		showTips();
		tipsText.setOnClickListener(null);
		tipsText.setText(R.string.common_empty);
	}
	public void showEmpty(String str){
		showTips();
		tipsText.setText(str);
	}
	public void showEmpty(int resId){
		showTips();
		tipsText.setText(resId);
	}
	
	/***设置错误提示***/
	public void showError(){
		showTips();
		tipsText.setText(R.string.common_error);
	}
	public void showError(String str){
		showTips();
		tipsText.setText(str);
	}
	public void showError(int resId){
		showTips();
		tipsText.setText(resId);
	}
	
	/***设置重试提示***/
	public void showRetry(){
		tipsLayout.setVisibility(View.GONE);
		retryLayout.setVisibility(View.VISIBLE);
		loadingLayout.setVisibility(View.GONE);
		this.setVisibility(View.VISIBLE);
		retryText.setText(R.string.common_retry);
	}
	public void showRetry(String str){
		showRetry();
		retryText.setText(str);
	}
	public void showRetry(int resId){
		showRetry();
		retryText.setText(resId);
	}
	public void setRetryListener(OnClickListener onClickListener){
		retryText.setOnClickListener(onClickListener);
	}
}
