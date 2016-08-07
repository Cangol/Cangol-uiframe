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
package mobi.cangol.mobile.navigation;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;


public abstract class AbstractNavigationFragmentActivityDelegate {

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    public abstract void onCreate(Bundle savedInstanceState);

    /**
     * 返回自定义的根布局
     *
     * @return
     */
    public abstract ViewGroup getRootView();

    /**
     * 返回menu布局
     *
     * @return
     */
    public abstract ViewGroup getMenuView();

    /**
     * 返回内容去布局
     *
     * @return
     */
    public abstract ViewGroup getContentView();

    /**
     * @param v
     */
    public abstract void setContentView(View v);

    /**
     * 获取menu布局的id
     *
     * @return
     */
    public abstract int getMenuFrameId();

    /**
     * onConfigurationChanged
     *
     * @param newConfig
     */
    public abstract void onConfigurationChanged(Configuration newConfig);

    /**
     * 显示menu
     *
     * @param show
     */
    public abstract void showMenu(boolean show);

    /**
     * 返回menu是否显示
     *
     * @return
     */
    public abstract boolean isShowMenu();

    /**
     * 设置menu有效
     *
     * @param enable
     */
    public abstract void setMenuEnable(boolean enable);

    /**
     * 处理onKeyUp事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    public abstract boolean onKeyUp(int keyCode, KeyEvent event);

    /**
     * 返回一个BaseNavigationFragmentActivity
     *
     * @return
     */
    public abstract BaseNavigationFragmentActivity getActivity();

    /**
     * attachToActivity
     *
     * @param activity
     */
    public abstract void attachToActivity(Activity activity);

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public abstract void setBackgroundColor(int color);

    /**
     * 设置背景
     *
     * @param resId
     */
    public abstract void setBackgroundResource(int resId);

}