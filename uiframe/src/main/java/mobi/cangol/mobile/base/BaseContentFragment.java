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
import androidx.fragment.app.Fragment;
import android.view.View;

import mobi.cangol.mobile.actionbar.ActionBar;
import mobi.cangol.mobile.actionbar.ActionBarActivity;
import mobi.cangol.mobile.actionbar.ActionMenu;
import mobi.cangol.mobile.actionbar.ActionMenuItem;
import mobi.cangol.mobile.actionbar.ActionMode;
import mobi.cangol.mobile.logging.Log;

public abstract class BaseContentFragment extends BaseFragment {

    public static final String GET_ACTIVITY_IS_NULL = "get activity is null";
    public static final String GET_ACTIVITY_IS_NOT = "get activity is not ActionBarActivity";
    private CharSequence title;

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
        if (getActivity() == null) {
            throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
        } else if(getActivity() instanceof ActionBarActivity ){
            return  ((ActionBarActivity) this.getActivity()).getCustomActionBar();
        }else{
            throw new IllegalStateException(GET_ACTIVITY_IS_NOT);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.setTitle("");
    }
    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    public void setStatusBarColor(int color) {
        if (getActivity() == null) {
            throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
        } else if(getActivity() instanceof ActionBarActivity ){
            ((ActionBarActivity)getActivity()).setStatusBarTintColor(color);
        }else{
            throw new IllegalStateException(GET_ACTIVITY_IS_NOT);
        }
    }

    /**
     * 设置导航栏颜色
     *
     * @param color
     */
    public void setNavigationBarTintColor(int color) {
        if (getActivity() == null) {
            throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
        } else if(getActivity() instanceof ActionBarActivity ){
            ((ActionBarActivity)getActivity()).setNavigationBarTintColor(color);
        }else{
            throw new IllegalStateException(GET_ACTIVITY_IS_NOT);
        }
    }

    /**
     * 开始progress模式
     */
    public void enableRefresh(boolean enable) {
        if (getActivity() == null) {
            throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
        } else if(getActivity() instanceof ActionBarActivity ){
            ((ActionBarActivity)getActivity()).getCustomActionBar().enableRefresh(enable);
        }else{
            throw new IllegalStateException(GET_ACTIVITY_IS_NOT);
        }
    }

    /**
     * 停止progress模式
     */
    public void refreshing(boolean refreshing) {
        if (getActivity() == null) {
            throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
        } else if(getActivity() instanceof ActionBarActivity ){
            ((ActionBarActivity)getActivity()).getCustomActionBar().refreshing(refreshing);
        }else{
            throw new IllegalStateException(GET_ACTIVITY_IS_NOT);
        }
    }

    /**
     * 开启自定义actionbar模式
     *
     * @param callback
     * @return
     */
    public ActionMode startCustomActionMode(ActionMode.Callback callback) {
        if (getActivity() == null) {
            throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
        } else if(getActivity() instanceof ActionBarActivity ){
            return ((ActionBarActivity)getActivity()).startCustomActionMode(callback);
        }else{
            throw new IllegalStateException(GET_ACTIVITY_IS_NOT);
        }
    }


    /**
     * 设置menu的显示（在super.onViewCreated后调用才有效）
     *
     * @param enable
     */
    protected final  void setMenuEnable(boolean enable) {
        BaseContentFragment parent = (BaseContentFragment) this.getParentFragment();
        if (parent == null) {
            if (getActivity()!=null&&this.getParentFragment() == null) {
                if (this.getActivity() instanceof BaseNavigationFragmentActivity) {
                    BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
                    bfActivity.setMenuEnable(enable);
                } else {
                    Log.e("getActivity is not BaseNavigationFragmentActivity ");
                }
            }
        }
    }

    protected final  void notifyMenuChange(int moduleId) {
        if (getActivity() == null) {
            throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
        } else if(getActivity() instanceof BaseNavigationFragmentActivity ){
            BaseNavigationFragmentActivity bfActivity = (BaseNavigationFragmentActivity) this.getActivity();
            bfActivity.setCurrentModuleId(moduleId);
        }
    }

    private final  void setActionBarUpIndicator() {
        BaseContentFragment parent = (BaseContentFragment) this.getParentFragment();
        if (parent == null) {
            if (getActivity() == null) {
                throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
            } else if(getActivity() instanceof ActionBarActivity ){
                ActionBarActivity bfActivity = (ActionBarActivity) this.getActivity();
                if (isCleanStack()) {
                    bfActivity.getCustomActionBar().displayHomeIndicator();
                } else {
                    bfActivity.getCustomActionBar().displayUpIndicator();
                }
            }else{
                throw new IllegalStateException(GET_ACTIVITY_IS_NOT);
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setMenuVisibility(true);
        if (savedInstanceState != null) {
            title = savedInstanceState.getCharSequence("title");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("title", title);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.getParentFragment() == null) {
            getCustomActionBar().clearActionMenus();
        }
        setActionBarUpIndicator();
        if (this.getActivity() instanceof BaseNavigationFragmentActivity) {
            setMenuEnable(isCleanStack());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarActivity abActivity = (ActionBarActivity) this.getActivity();
        if (abActivity == null) {
            throw new IllegalStateException(GET_ACTIVITY_IS_NULL);
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
    public boolean onMenuActionCreated(ActionMenu actionMenu) {

        return false;
    }

    /**
     * actionbar menu选择时间相应方法
     *
     * @param action
     * @return
     */
    public boolean onMenuActionSelected(ActionMenuItem action) {
        if(this.getChildFragmentManager()!=null&&
                this.getChildFragmentManager().getFragments()!=null&&
                !this.getChildFragmentManager().getFragments().isEmpty()){
            int size=getChildFragmentManager().getFragments().size();
            for (int i = size-1; i >=0; i--) {
                Fragment fragment=getChildFragmentManager().getFragments().get(i);
                if(fragment instanceof BaseContentFragment){
                    if(((BaseContentFragment) fragment).isEnable()
                            &&fragment.isVisible()){
                        return ((BaseContentFragment) fragment).onMenuActionSelected(action);
                    }
                }
            }
        }
        return false;
    }
    /**
     * 设置顶级content fragment
     *
     * @param fragmentClass
     * @param args
     */
    public final  void setContentFragment(Class<? extends BaseContentFragment> fragmentClass,Bundle args) {
        this.setContentFragment(fragmentClass,fragmentClass.getName(),args);
    }
    /**
     * 设置顶级content fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    public final  void setContentFragment(Class<? extends BaseContentFragment> fragmentClass, String tag, Bundle args) {
        if (this.getActivity() instanceof CustomFragmentActivityDelegate) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.replaceFragment(fragmentClass, tag, args);
        }else{
            replaceFragment(fragmentClass, tag, args);
        }
    }

    /**
     * 设置顶级content fragment,并更通知menuFragment更新变更了模块
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param moduleId
     */
    public final  void setContentFragment(Class<? extends BaseContentFragment> fragmentClass, String tag, Bundle args, int moduleId) {
        if (this.getActivity() instanceof CustomFragmentActivityDelegate) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.replaceFragment(fragmentClass, tag, args,moduleId);
        }else{
            replaceFragment(fragmentClass, tag, args);
        }
        notifyMenuChange(moduleId);
    }

    /**
     * 获取父类 BaseContentFragment
     * @return
     */
    public final  BaseContentFragment getParentContentFragment() {
        return (BaseContentFragment) getParentFragment();
    }

}
