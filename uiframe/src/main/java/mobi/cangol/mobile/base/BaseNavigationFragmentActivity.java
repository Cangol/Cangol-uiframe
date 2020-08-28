/*
 * Copyright (c) 2013 Cangol
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.base;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.ref.SoftReference;

import mobi.cangol.mobile.navigation.AbstractNavigationFragmentActivityDelegate;

public abstract class BaseNavigationFragmentActivity extends BaseActionBarActivity {
    public static final String MENU_SHOW = "MENU_SHOW";
    private static final String MENU_TAG = "MenuFragment";
    private SoftReference<BaseMenuFragment> menuFragmentReference;
    private AbstractNavigationFragmentActivityDelegate mHelper;
    protected boolean mFloatActionBarEnabled;

    public boolean isFloatActionBarEnabled() {
        return mFloatActionBarEnabled;
    }

    public void setFloatActionBarEnabled(boolean floatActionBarEnabled) {
        mFloatActionBarEnabled = floatActionBarEnabled;
    }
    /**
     * 返回content布局的id
     *
     * @return
     */
    public abstract int getContentFrameId();

    public void setNavigationFragmentActivityDelegate(AbstractNavigationFragmentActivityDelegate mHelper) {
        this.mHelper = mHelper;
    }

    public AbstractNavigationFragmentActivityDelegate getNavigationFragmentActivityDelegate() {
        return mHelper;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mHelper.onCreate(savedInstanceState);
        this.initFragmentStack(getContentFrameId());
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.attachToActivity(this);
        if (savedInstanceState != null) {
            boolean show = savedInstanceState.getBoolean(MENU_SHOW);
            mHelper.showMenu(show);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MENU_SHOW, isShowMenu());
    }
    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mHelper.setBackgroundColor(color);
    }
    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        mHelper.setBackgroundResource(resId);
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v != null)
            return v;
        return mHelper.getRootView().findViewById(id);
    }

    @Override
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public FrameLayout getMaskView() {
        if(isFloatActionBarEnabled()){
           return mHelper.getMaskView();
        }else{
           return super.getMaskView();
        }
    }
    @Override
    public void displayMaskView(boolean show) {
        if(isFloatActionBarEnabled()){
            mHelper.displayMaskView(show);
        }else{
            super.displayMaskView(show);
        }
    }

    public void showMenu(boolean show) {
        mHelper.showMenu(show);
    }

    public boolean isShowMenu() {
        return mHelper.isShowMenu();
    }

    public void setMenuEnable(boolean enable) {
        mHelper.setMenuEnable(enable);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean b = mHelper.onKeyUp(keyCode, event);
        if (b) return b;
        return super.onKeyUp(keyCode, event);
    }

    public int getCurrentModuleId() {
        if (menuFragmentReference == null||menuFragmentReference.get()==null) {
            throw new IllegalStateException("menuFragment is null");
        } else {
            return menuFragmentReference.get().getCurrentModuleId();
        }
    }

    public BaseMenuFragment getMenuFragment() {
        if(menuFragmentReference!=null)
            return menuFragmentReference.get();
        else
            return null;
    }

    public void setCurrentModuleId(int moduleId) {
        if (menuFragmentReference != null&&menuFragmentReference.get()!=null) {
            menuFragmentReference.get().setCurrentModuleId(moduleId);
        }
    }

    public final void setMenuFragment(Class<? extends BaseMenuFragment> fragmentClass, Bundle args) {
        BaseMenuFragment menuFragment = (BaseMenuFragment) Fragment.instantiate(this, fragmentClass.getName(), args);
        menuFragmentReference=new SoftReference<>(menuFragment);
        FragmentTransaction t = this.getSupportFragmentManager()
                .beginTransaction();
        t.replace(mHelper.getMenuFrameId(), menuFragment, MENU_TAG);
        t.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
    }

    public final void setContentFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, int moduleId) {
        replaceFragment(fragmentClass, tag, args);
        setCurrentModuleId(moduleId);
    }

    public final void setContentFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        replaceFragment(fragmentClass, tag, args);
    }
    public final void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle args) {
        replaceFragment(fragmentClass, fragmentClass.getName(), args);
    }
    public void notifyMenuOnClose() {
        if (menuFragmentReference != null&&menuFragmentReference.get()!=null)
            menuFragmentReference.get().onClosed();
    }

    public void notifyMenuOnOpen() {
        if (menuFragmentReference != null&&menuFragmentReference.get()!=null)
            menuFragmentReference.get().onOpen();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        BaseMenuFragment menuFragment = (BaseMenuFragment) getSupportFragmentManager().findFragmentByTag(MENU_TAG);
        if(menuFragment!=null){
            menuFragmentReference=new SoftReference<>(menuFragment);
        }
    }
}

