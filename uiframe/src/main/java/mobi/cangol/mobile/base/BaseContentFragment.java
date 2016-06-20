/**
 * Copyright (c) 2013 Cangol
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.base;

import android.os.Bundle;
import android.view.View;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.actionbar.ActionBar;
import mobi.cangol.mobile.actionbar.ActionBarActivity;
import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.actionbar.ActionMode;
import mobi.cangol.mobile.logging.Log;

public abstract class BaseContentFragment extends BaseFragment {

    private CharSequence title;

    public BaseContentFragment() {
        super();
    }

    /**
     * 获取ActionBarActivity,由于原getActivity为final，故增加此方法
     *
     * @return
     */
    public final ActionBarActivity getActionBarActivity() {
        return (ActionBarActivity) getActivity();
    }

    /**
     * 获取自定义actionbar
     *
     * @return
     */
    public ActionBar getCustomActionBar() {
        ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
        if (abActivity == null) {
            throw new IllegalStateException("getActivity is null");
        } else {
            return abActivity.getCustomActionBar();
        }
    }

    /**
     * 获取标题
     *
     * @return
     */
    public CharSequence getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (this.getParentFragment() != null) return;
        this.title = title;
        if (getCustomActionBar() != null)
            getCustomActionBar().setTitle(title);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(int title) {
        if (this.getParentFragment() != null) return;
        this.title = getString(title);
        if (getCustomActionBar() != null)
            getCustomActionBar().setTitle(title);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    public void setStatusBarColor(int color) {
        ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
        if (abActivity == null) {
            throw new IllegalStateException("getActivity is null");
        } else {
            abActivity.setStatusBarTintColor(color);
        }
    }

    /**
     * 设置导航栏颜色
     *
     * @param color
     */
    public void setNavigationBarTintColor(int color) {
        ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
        if (abActivity == null) {
            throw new IllegalStateException("getActivity is null");
        } else {
            abActivity.setNavigationBarTintColor(color);
        }
    }

    /**
     * 开始progress模式
     */
    public void startProgress() {
        ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
        if (abActivity == null) {
            throw new IllegalStateException("getActivity is null");
        } else {
            abActivity.getCustomActionBar().startProgress();
        }
    }

    /**
     * 停止progress模式
     */
    public void stopProgress() {
        ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
        if (abActivity == null) {
            throw new IllegalStateException("getActivity is null");
        } else {
            abActivity.getCustomActionBar().stopProgress();
        }
    }

    /**
     * 开启自定义actionbar模式
     *
     * @param callback
     * @return
     */
    public ActionMode startCustomActionMode(ActionMode.Callback callback) {
        ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
        if (abActivity == null) {
            throw new IllegalStateException("getActivity is null");
        } else {
            return abActivity.startCustomActionMode(callback);
        }
    }


    /**
     * 设置menu的显示（在super.onViewCreated后调用才有效）
     *
     * @param enable
     */
    final protected void setMenuEnable(boolean enable) {
        BaseContentFragment parent = (BaseContentFragment) this.getParentFragment();
        if (parent == null) {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else if (this.getParentFragment() == null) {
                if (this.getActivity() instanceof BaseNavigationFragmentActivity) {
                    BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
                    bfActivity.setMenuEnable(enable);
                }
            }
        }
    }

    final protected void notifyMenuChange(int moduleId) {
        if (getActivity() == null) {
            throw new IllegalStateException("getActivity is null");
        } else {
            if (this.getActivity() instanceof BaseNavigationFragmentActivity) {
                BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
                bfActivity.setCurrentModuleId(moduleId);
            }
        }
    }

    final private void setActionBarUpIndicator() {
        BaseContentFragment parent = (BaseContentFragment) this.getParentFragment();
        if (parent == null) {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                ActionBarActivity bfActivity = (ActionBarActivity) this.getActivity();
                if (isCleanStack()) {
                    bfActivity.getCustomActionBar().displayHomeIndicator();
                } else {
                    bfActivity.getCustomActionBar().displayUpIndicator();
                }
            }
        } else {

        }

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setMenuVisibility(true);
        app = (CoreApplication) this.getActivity().getApplication();
        if (savedInstanceState == null) {

        } else {
            title = savedInstanceState.getCharSequence("title");
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("title",title);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.getParentFragment() == null) {
            getCustomActionBar().clearActionMenus();
        }
        setActionBarUpIndicator();
        setMenuEnable(isCleanStack());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
        if (abActivity == null) {
            throw new IllegalStateException("getActivity is null");
        } else {
            this.onMenuActionCreated(abActivity.getCustomActionBar().getActionMenu());
        }
    }

    /**
     * actionbar menu创建方法
     *
     * @param actionMenu
     * @return
     */
    protected boolean onMenuActionCreated(ActionMenu actionMenu) {

        return false;
    }

    /**
     * actionbar menu选择时间相应方法
     *
     * @param action
     * @return
     */
    protected boolean onMenuActionSelected(ActionMenuItem action) {

        return false;
    }
    /**
     * 设置content fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    final public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass, String tag, Bundle args) {
        replaceFragment(fragmentClass, tag, args);
    }

    /**
     * 设置content fragment,并更通知menuFragment更新变更了模块
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param moduleId
     */
    final public void setContentFragment(Class<? extends BaseContentFragment> fragmentClass, String tag, Bundle args, int moduleId) {
        setContentFragment(fragmentClass, tag, args);
        notifyMenuChange(moduleId);

    }

}
