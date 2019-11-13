package mobi.cangol.mobile.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout

/**
 * Created by jince on 2015/12/5.
 */
class SoftKeyboardHandledLinearLayout : LinearLayout {

    private var isKeyboardShown: Boolean = false
    private var listener: SoftKeyboardVisibilityChangeListener? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    @SuppressLint("NewApi")
    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle) {
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && isKeyboardShown) {
            // Keyboard is hidden <<< RIGHT
            isKeyboardShown = false
        }
        return super.dispatchKeyEventPreIme(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val proposedHeight = MeasureSpec.getSize(heightMeasureSpec)
        val actualHeight = height
        if (actualHeight > proposedHeight) {
            // Keyboard is shown
            if (!isKeyboardShown) {
                isKeyboardShown = true
                listener!!.onSoftKeyboardShow()
            }
        } else {
            if (isKeyboardShown) {
                isKeyboardShown = false
                listener!!.onSoftKeyboardHide()
            }
            // Keyboard is hidden <<< this doesn't work sometimes, so I don't use it
        }
    }

    fun setOnSoftKeyboardVisibilityChangeListener(listener: SoftKeyboardVisibilityChangeListener) {
        this.listener = listener
    }

    // Callback
    interface SoftKeyboardVisibilityChangeListener {
        fun onSoftKeyboardShow()

        fun onSoftKeyboardHide()
    }
}