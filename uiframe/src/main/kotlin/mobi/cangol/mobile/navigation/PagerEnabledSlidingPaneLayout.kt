package mobi.cangol.mobile.navigation

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.support.v4.widget.SlidingPaneLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration

/**
 * SlidingPaneLayout that, if closed, checks if children can scroll before it intercepts
 * touch events.  This allows it to contain horizontally scrollable children without
 * intercepting all of their touches.
 *
 * To handle cases where the user is scrolled very far to the right, but should still be
 * able to open the pane without the need to scroll all the way back to the start, this
 * view also adds edge touch detection, so it will intercept edge swipes to open the pane.
 */
open class PagerEnabledSlidingPaneLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SlidingPaneLayout(context, attrs) {

    private var mInitialMotionX: Float = 0.toFloat()
    private val mEdgeSlop: Float

    init {
        val config = ViewConfiguration.get(context)
        mEdgeSlop = config.scaledEdgeSlop.toFloat()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_DOWN -> mInitialMotionX = ev.x

            MotionEvent.ACTION_MOVE -> {
                val x = ev.x
                val y = ev.y
                // The user should always be able to "close" the pane, so we only check
                // for child scrollability if the pane is currently closed.
                if (mInitialMotionX > mEdgeSlop && !isOpen && canScroll(this, false,
                                Math.round(x - mInitialMotionX), Math.round(x), Math.round(y))) {

                    // How do we set super.mIsUnableToDrag = true?

                    // send the parent a cancel event
                    val cancelEvent = MotionEvent.obtain(ev)
                    cancelEvent.action = MotionEvent.ACTION_CANCEL
                    return super.onInterceptTouchEvent(cancelEvent)
                }
            }
            else -> {
            }
        }

        return super.onInterceptTouchEvent(ev)
    }
}