package mobi.cangol.mobile.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mobi.cangol.mobile.uiframe.R;


public class TabMenuDrawerLayout extends DrawerLayout {
    private ViewGroup mRootView;
    private FrameLayout mLeftView;
    private FrameLayout mRightView;
    private float mDrawerWidth = 0.618f;
    private boolean isFloatActionBarEnabled;

    public TabMenuDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLeftView = new FrameLayout(context);
        mRightView = new FrameLayout(context);

        mRootView= (ViewGroup) LayoutInflater.from(context).inflate(R.layout.navigation_tab_main, null);
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRootView.setId(R.id.main_view);
        this.addView(mRootView, lp1);

        int width = (int) (mDrawerWidth * context.getResources().getDisplayMetrics().widthPixels);

        LayoutParams lp2 = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        lp2.gravity = Gravity.LEFT;
        mLeftView.setId(R.id.left_view);
        this.addView(mLeftView, lp2);

        LayoutParams lp3 = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        lp3.gravity = Gravity.RIGHT;
        mRightView.setId(R.id.right_view);
        this.addView(mRightView, lp3);
    }

    public ViewGroup getContentView() {
        return (ViewGroup) mRootView.findViewById(R.id.content_view);
    }

    public void setContentView(View v) {
        getContentView().removeAllViews();
        getContentView().addView(v);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void showDrawer(int gravity, boolean show) {
        if (show) {
            if(gravity==Gravity.LEFT&&mLeftView.getChildCount()>0){
                this.openDrawer(gravity);
            }else if(gravity==Gravity.RIGHT&&mRightView.getChildCount()>0){
                this.openDrawer(gravity);
            }else{
                Log.e("showDrawer","gravity is not LEFT|RIGHT,drawer is empty");
            }
        } else {
            this.closeDrawer(gravity);
        }
    }

    public boolean isShowDrawer(int gravity) {
        return this.isDrawerOpen(gravity);
    }

    public void setDrawerEnable(int gravity, boolean enable) {
        this.setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED, gravity);
    }

    /*
     *  (non-Javadoc)
     * @see android.view.ViewGroup#fitSystemWindows(android.graphics.Rect)
     */
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        int leftPadding = insets.left;
        int rightPadding = insets.right;
        int topPadding = insets.top;
        int bottomPadding = insets.bottom;
        Log.e("fitSystemWindows","fitSystemWindows="+insets.toString());
        if (isFloatActionBarEnabled) {
            mRootView.setPadding(leftPadding,
                    topPadding,
                    rightPadding,
                    bottomPadding);
            mLeftView.setPadding(leftPadding,
                    topPadding,
                    rightPadding,
                    bottomPadding);
            mRightView.setPadding(leftPadding,
                    topPadding,
                    rightPadding,
                    bottomPadding);
        }
        return true;
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mRightView.setBackgroundColor(color);
        mLeftView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        mRightView.setBackgroundResource(resId);
        mLeftView.setBackgroundResource(resId);
    }

    public void attachToActivity(Activity activity, boolean isFloatActionBarEnabled) {
        // get the window background
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        this.isFloatActionBarEnabled = isFloatActionBarEnabled;

        if (isFloatActionBarEnabled) {
            ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
            if (decorChild.getBackground() != null) {
                this.setBackgroundDrawable(decorChild.getBackground());
                decorChild.setBackgroundDrawable(null);
            } else {
                if (this.getBackground() == null)
                    this.setBackgroundResource(background);
            }
            decor.removeView(decorChild);
            decor.addView(this, 0);
            getContentView().addView(decorChild);
        } else {
            ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
            ViewGroup content = (ViewGroup) contentParent.getChildAt(0);
            contentParent.removeView(content);
            contentParent.addView(this, 0);
            getContentView().addView(content);
        }
    }
}
