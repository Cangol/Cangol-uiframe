package mobi.cangol.mobile.actionbar;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;

public class ActionBarDrawerToggle implements DrawerListener {
	private static final float TOGGLE_DRAWABLE_OFFSET = 1 / 3f;
	private final ActionBarActivity mActivity;
	private final DrawerLayout mDrawerLayout;

	private final int mDrawerImageResource;
	private Drawable mDrawerImage;
	private SlideDrawable mSlider;
	
	public ActionBarDrawerToggle(ActionBarActivity activity, DrawerLayout drawerLayout,
			int drawerImageRes) {
		mActivity = activity;
		mDrawerLayout = drawerLayout;
		mDrawerImageResource = drawerImageRes;
		mDrawerImage = activity.getResources().getDrawable(drawerImageRes);
		mSlider = new SlideDrawable(mDrawerImage);
        mSlider.setOffset(TOGGLE_DRAWABLE_OFFSET);
	}
	public void onConfigurationChanged(Configuration newConfig) {
        mDrawerImage = mActivity.getResources().getDrawable(mDrawerImageResource);
        syncState();
    }
	
	public void syncState() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mSlider.setPosition(1);
        } else {
            mSlider.setPosition(0);
        }
        setActionBarUpIndicator(mSlider);
    }
	
	@Override
	public void onDrawerClosed(View view) {
		 if (!mDrawerLayout.isDrawerVisible(Gravity.LEFT)) {
				 mSlider.setPosition(0);
				 setActionBarUpIndicator(mSlider);
		 }
	}

	@Override
	public void onDrawerOpened(View view) {
		 if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				 mSlider.setPosition(1);
				 setActionBarUpIndicator(mSlider);
		 }
	}

	@Override
	public void onDrawerSlide(View view, float slideOffset) {
		float glyphOffset = mSlider.getPosition();
        if (slideOffset > 0.5f) {
            glyphOffset = Math.max(glyphOffset, Math.max(0.f, slideOffset - 0.5f) * 2);
        } else {
            glyphOffset = Math.min(glyphOffset, slideOffset * 2);
        }
        mSlider.setPosition(glyphOffset);
	}

	@Override
	public void onDrawerStateChanged(int newState) {

	}
	
	void setActionBarUpIndicator(Drawable upDrawable) {
        if (mActivity != null) {
            mActivity.getCustomActionBar().setActionBarUpIndicator(upDrawable);
            return;
        }
    }
	
	private class SlideDrawable extends LevelListDrawable implements Drawable.Callback {
        private final boolean mHasMirroring = Build.VERSION.SDK_INT > 18;
        private final Rect mTmpRect = new Rect();

        private float mPosition;
        private float mOffset;

        private SlideDrawable(Drawable wrapped) {
            super();

            if (DrawableCompat.isAutoMirrored(wrapped)) {
                DrawableCompat.setAutoMirrored(this, true);
            }

            addLevel(0, 0, wrapped);
        }

        /**
         * Sets the current position along the offset.
         *
         * @param position a value between 0 and 1
         */
        public void setPosition(float position) {
            mPosition = position;
            invalidateSelf();
        }

        public float getPosition() {
            return mPosition;
        }

        /**
         * Specifies the maximum offset when the position is at 1.
         *
         * @param offset maximum offset as a fraction of the drawable width,
         *            positive to shift left or negative to shift right.
         * @see #setPosition(float)
         */
        public void setOffset(float offset) {
            mOffset = offset;
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            copyBounds(mTmpRect);
            canvas.save();

            // Layout direction must be obtained from the activity.
            final boolean isLayoutRTL = ViewCompat.getLayoutDirection(
                    mActivity.getWindow().getDecorView()) == ViewCompat.LAYOUT_DIRECTION_RTL;
            final int flipRtl = isLayoutRTL ? -1 : 1;
            final int width = mTmpRect.width();
            canvas.translate(-mOffset * width * mPosition * flipRtl, 0);

            // Force auto-mirroring if it's not supported by the platform.
            if (isLayoutRTL && !mHasMirroring) {
                canvas.translate(width, 0);
                canvas.scale(-1, 1);
            }

            super.draw(canvas);
            canvas.restore();
        }
    }
}
