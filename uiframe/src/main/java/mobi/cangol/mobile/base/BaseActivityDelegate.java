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
import android.os.Handler;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import android.widget.EditText;

import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.session.Session;

public interface BaseActivityDelegate {

    /**
     * 查找view
     */
    void findViews();

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */

    void initViews(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * 显示toast
     *
     * @param resId
     */
    void showToast(int resId);

    /**
     * 显示toast
     *
     * @param str
     */
    void showToast(String str);

    /**
     * 显示toast
     *
     * @param resId
     * @param duration
     */
    void showToast(int resId, int duration);

    /**
     * 显示toast
     *
     * @param str
     * @param duration
     */
    void showToast(String str, int duration);

    /**
     * 获取AppService
     *
     * @param name
     * @return
     */
    AppService getAppService(String name);

    /**
     * 获取Session
     *
     * @return
     */
    Session getSession();

    /**
     * 设置全屏
     *
     * @param fullscreen
     */
    void setFullScreen(boolean fullscreen);
    /**
     * 是否是全屏
     *
     */
    boolean isFullScreen();
    /**
     * back按钮回调
     */
    void onBack();


    /**
     * 显示软键盘
     *
     * @param editText
     */
    void showSoftInput(EditText editText);

    /**
     * 隐藏软键盘
     *
     */
     void hideSoftInput();

    /**
     * 隐藏软键盘
     *
     */
    void hideSoftInput(EditText editText);

    /**
     * 获取主线程的Handler
     */
    Handler getUiHandler();

    /**
     * 获取共享线程的Handler
     */
    Handler getThreadHandler();

    @ColorInt
    int getThemeAttrColor(@AttrRes int colorAttr);

}
