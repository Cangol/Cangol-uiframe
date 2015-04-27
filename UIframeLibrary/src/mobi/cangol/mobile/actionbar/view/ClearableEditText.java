package mobi.cangol.mobile.actionbar.view;

import mobi.cangol.mobile.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
/**
* @Description: 
* @version $Revision: 1.0 $ 
* @author xuewu.wei
* @date: 2012-6-5
*/
public class ClearableEditText extends EditText {

    final Drawable imgX = getResources().getDrawable(R.drawable.actionbar_clear);// X image //android.R.drawable.presence_offline 

    public ClearableEditText(Context context) {
        super(context);

        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }


    void init()  {
        // Set bounds of our X button
        imgX.setBounds(0, 0, imgX.getIntrinsicWidth(), imgX.getIntrinsicHeight());      

        // There may be initial text in the field, so we may need to display the button
        manageClearButton(); 

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ClearableEditText et = ClearableEditText.this;

                // Is there an X showing?
                if (et.getCompoundDrawables()[2] == null) return false;
                // Only do this for up touches
                if (event.getAction() != MotionEvent.ACTION_UP) return false;
                // Is touch on our clear button?
                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgX.getIntrinsicWidth()) {
                    et.setText("");
                    removeClearButton();
                }
                return false;
            }
        });

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            	manageClearButton();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					removeClearButton();
				}else {
					manageClearButton();
				}
			}
		});
    }

    void manageClearButton() {
        if (this.getText().toString().equals("")){
        	removeClearButton();
        }else if(this.hasFocus()){
        	addClearButton();
        }
            
    }
    void addClearButton() {
        this.setCompoundDrawables(this.getCompoundDrawables()[0], 
                this.getCompoundDrawables()[1],
                getError()==null?imgX: this.getCompoundDrawables()[2],
                this.getCompoundDrawables()[3]);
    }
    void removeClearButton() {
        this.setCompoundDrawables(this.getCompoundDrawables()[0], 
                this.getCompoundDrawables()[1],
                getError()==null?null: this.getCompoundDrawables()[2],
                this.getCompoundDrawables()[3]);
    }

}