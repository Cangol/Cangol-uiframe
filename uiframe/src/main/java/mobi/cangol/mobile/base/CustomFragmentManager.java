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

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import mobi.cangol.mobile.logging.Log;

public class CustomFragmentManager {
    private static final String STATE_TAG = "CustomFragmentManager";
    private FragmentStack stack = null;
    private Object lock = new Object();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int containerId;
    private FragmentActivity fActivity;
    private final Runnable execPendingTransactions = new Runnable() {
        @Override
        public void run() {
            try {
                if (fragmentTransaction != null && fActivity != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!fActivity.isFinishing() && !fActivity.isDestroyed()) {
                            try {
                                fragmentTransaction.commitAllowingStateLoss();
                                fragmentManager.executePendingTransactions();
                                fragmentTransaction = null;
                            } catch (IllegalStateException e) {
                                Log.e(STATE_TAG, "execPendingTransactions", e);
                            }
                        }
                    } else {
                        if (!fActivity.isFinishing()) {
                            try {
                                fragmentTransaction.commitAllowingStateLoss();
                                fragmentManager.executePendingTransactions();
                                fragmentTransaction = null;
                            } catch (IllegalStateException e) {
                                Log.e(STATE_TAG, "execPendingTransactions", e);
                            }
                        }
                    }
                }
            }catch (OutOfMemoryError error){
                if (!fActivity.isFinishing())
                    fActivity.finish();
            }catch (Exception e){
                if (!fActivity.isFinishing())
                    fActivity.finish();
            }
        }
    };
    private InternalHandler handler;
    private int enterAnimation;
    private int exitAnimation;
    private int popStackEnterAnimation;
    private int popStackExitAnimation;
    private boolean firstUseAnim = false;

    private CustomFragmentManager(FragmentActivity fActivity, int containerId, FragmentManager fragmentManager) {
        this.fActivity = fActivity;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.handler = new InternalHandler(fActivity);
        this.stack = new FragmentStack();
    }

    public static CustomFragmentManager forContainer(FragmentActivity activity, int containerId,
                                                     FragmentManager fragmentManager) {
        return new CustomFragmentManager(activity, containerId, fragmentManager);
    }

    public void destroy() {
        this.stack.clear();
        this.handler.removeCallbacks(execPendingTransactions);
        this.fActivity=null;
        this.fragmentManager=null;
    }

    protected static final  class InternalHandler extends Handler {
        public InternalHandler(FragmentActivity activity) {
            super(Looper.getMainLooper());
        }
    }

    public int getContainerId() {
        return containerId;
    }

    public void setDefaultAnimation(int enter, int exit, int popEnter, int popExit) {
        enterAnimation = enter;
        exitAnimation = exit;
        popStackEnterAnimation = popEnter;
        popStackExitAnimation = popExit;
    }

    public void saveState(Bundle outState) {
        executePendingTransactions();
        final int stackSize = stack.size();
        String[] stackTags = new String[stackSize];

        int i = 0;
        for (String tag : stack.getTag()) {
            Log.i(STATE_TAG, "tag =" + tag);
            stackTags[i++] = tag;
        }

        outState.putStringArray(STATE_TAG, stackTags);
    }

    public void restoreState(Bundle state) {
        String[] stackTags = state.getStringArray(STATE_TAG);
        if(stackTags!=null){
            for (String tag : stackTags) {
                BaseFragment f = (BaseFragment) fragmentManager.findFragmentByTag(tag);
                stack.addFragment(f);
                stack.addTag(tag);
            }
        }
    }

    private FragmentTransaction beginTransaction() {
        if (fragmentTransaction == null) fragmentTransaction = fragmentManager.beginTransaction();
        handler.removeCallbacks(execPendingTransactions);
        return fragmentTransaction;
    }

    public void replace(Class<? extends BaseFragment> clazz, String tag, Bundle args) {
        if (clazz.isAssignableFrom(BaseDialogFragment.class)) {
            throw new IllegalStateException("DialogFragment can not be attached to a container view");
        } else {
            this.replace(clazz, tag, args, null);
        }
    }

    public void replace(Class<? extends BaseFragment> clazz, String tag, Bundle args, CustomFragmentTransaction customFragmentTransaction) {
        Log.v(STATE_TAG, "replace clazz="+clazz+",tag="+tag+",args="+args);
        if (fragmentManager.isDestroyed() || isStateSaved()) return;
        if (clazz.isAssignableFrom(BaseDialogFragment.class))
            throw new IllegalStateException("DialogFragment can not be attached to a container view");
        BaseFragment fragment = (BaseFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            Log.i(STATE_TAG, "fragment=null newInstance");
            fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
            if (fragment.isCleanStack()) {
                Log.i(STATE_TAG, "fragment isCleanStack=true,while pop all");
                while (stack.size() > 0) {
                    BaseFragment temp;
                    synchronized (lock) {
                        temp=stack.popFragment();
                        stack.popTag();
                    }
                    Log.v(STATE_TAG, "FragmentStackSize="+stack.size()+" backStackEntryCount="+fragmentManager.getBackStackEntryCount());
                    if(stack.size() > 0||!temp.isCleanStack())
                        fragmentManager.popBackStack();
                    else{
                        BaseFragment top = temp;
                        Log.v(STATE_TAG, "detach="+top);
                        if(top!=null)beginTransaction().detach(top);
                    }
                }
            } else {
                Log.i(STATE_TAG, "fragment isCleanStack=false");
                if (fragment.isSingleton() && stack.containsTag(tag)) {
                    Log.i(STATE_TAG, "fragment isSingleton=true,while pop all");
                    while (stack.size() > 0&&!tag.equals(stack.peekTag())) {
                        synchronized (lock) {
                            stack.popFragment();
                            stack.popTag();
                        }
                        fragmentManager.popBackStack();
                    }
                    synchronized (lock) {
                        stack.popFragment();
                        stack.popTag();
                    }
                    fragmentManager.popBackStack();
                    Log.i(STATE_TAG, "fragment newInstance");
                    fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
                }
            }
        } else {
            Log.i(STATE_TAG, "fragment is exist");
            if (fragment.isCleanStack()) {
                Log.i(STATE_TAG, "stack size="+stack.size());
                if (stack.size() == 1) {
                    if (stack.peekTag().equals(tag)) {
                        fragment.onNewBundle(args);
                        return;
                    } else {
                        BaseFragment temp;
                        synchronized (lock) {
                            temp=stack.popFragment();
                            stack.popTag();
                        }
                        BaseFragment top = temp;Log.v(STATE_TAG, "detach="+top);
                        if(top!=null)beginTransaction().detach(top);
                        fragment.onNewBundle(args);
                    }
                } else {
                    Log.i(STATE_TAG, "fragment isCleanStack=true,while pop all");
                    while (stack.size() > 0) {
                        BaseFragment temp;
                        synchronized (lock) {
                            temp=stack.popFragment();
                            stack.popTag();
                        }
                        Log.v(STATE_TAG, "FragmentStackSize="+stack.size()+" backStackEntryCount="+fragmentManager.getBackStackEntryCount());
                        if(stack.size() > 0&&!temp.isCleanStack())
                            fragmentManager.popBackStack();
                        else{
                            BaseFragment top = temp;
                            Log.v(STATE_TAG, "detach="+top);
                            if(top!=null)beginTransaction().detach(top);
                            fragment.onNewBundle(args);
                            if (top!=null&& TextUtils.equals(top.getClass().getName(),tag)) {
                                Log.v(STATE_TAG, "isCleanStack=true,same tag,new instance"+tag);
                                fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
                            }
                        }
                    }
                }
            } else {
                Log.i(STATE_TAG, "fragment isCleanStack=false");
                if (!fragment.isSingleton()) {
                    Log.i(STATE_TAG, "fragment isSingleton=false,newInstance");
                } else {
                    Log.i(STATE_TAG, "fragment isSingleton=true,while pop all");
                    while (stack.size() > 0&&!tag.equals(stack.peekTag())) {
                        synchronized (lock) {
                            stack.popFragment();
                            stack.popTag();
                        }
                        fragmentManager.popBackStack();
                    }
                    synchronized (lock) {
                        stack.popFragment();
                        stack.popTag();
                    }
                    fragmentManager.popBackStack();
                }
                fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
            }
        }
        if (customFragmentTransaction != null)
            customFragmentTransaction.fillTargetFragment(fragment);

        if (firstUseAnim || stack.size() > 0) {
            //保证第一个填充不适用动画
            if (customFragmentTransaction == null || !customFragmentTransaction.fillCustomAnimations(beginTransaction())) {
                if (enterAnimation > 0 && exitAnimation > 0 && popStackEnterAnimation > 0 && popStackExitAnimation > 0) {
                    beginTransaction().setCustomAnimations(enterAnimation, exitAnimation, popStackEnterAnimation, popStackExitAnimation);
                } else if (enterAnimation > 0 && exitAnimation > 0) {
                    beginTransaction().setCustomAnimations(enterAnimation, exitAnimation);
                } else {
                    beginTransaction();
                }
            }
        }
        attachFragment(fragment, tag);
        synchronized (lock) {
            stack.addFragment(fragment);
            stack.addTag(tag);
        }
    }

    public void setFirstUseAnim(boolean firstUseAnim) {
        this.firstUseAnim = firstUseAnim;
    }

    private void attachFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            if (fragment.isDetached()) {
                Log.i(STATE_TAG, "attachFragment tag=" + tag);
                beginTransaction().attach(fragment);
                if (stack.size() > 0) {
                    beginTransaction().addToBackStack(tag);
                }
            } else if (!fragment.isAdded()) {
                Log.i(STATE_TAG, "replaceFragment tag=" + tag);
                beginTransaction().replace(containerId, fragment, tag);
                if (stack.size() > 0) {
                    beginTransaction().addToBackStack(tag);
                }
            } else {
                Log.i(STATE_TAG, "fragment state illegal " + fragment);
            }
        } else {
            Log.i(STATE_TAG, "fragment is null");
        }
    }

    public BaseFragment peek() {
        return stack.peekFragment();
    }

    public int size() {
        return stack.size();
    }


    public boolean popBackStack() {
        if (fragmentManager.isDestroyed() || isStateSaved()) return false;
        if (stack.size() > 1) {
            fragmentManager.popBackStack();
            synchronized (lock) {
                BaseFragment baseFragment = stack.popFragment();
                stack.popTag();
                if (baseFragment != null && baseFragment.getTargetFragment() != null) {
                    baseFragment.notifyResult();
                }
            }
            return true;
        }
        return false;
    }

    public boolean popBackStack(boolean cleanFirst) {
        if (fragmentManager.isDestroyed() || isStateSaved()) return false;
        if (cleanFirst||stack.size() > 1) {
            fragmentManager.popBackStack();
            synchronized (lock) {
                BaseFragment baseFragment = stack.popFragment();
                stack.popTag();
                if (baseFragment != null && baseFragment.getTargetFragment() != null) {
                    baseFragment.notifyResult();
                }
            }
            return true;
        }
        return false;
    }
    public boolean popBackStack(String tag, int flag) {
        if (fragmentManager.isDestroyed() || isStateSaved()) return false;
        if (stack.size() > 1) {
            fragmentManager.popBackStack(tag, flag);
            synchronized (lock) {
                stack.popFragment(tag, flag);
            }
            return true;
        }
        return false;
    }

    public boolean popBackStackImmediate(String tag, int flag) {
        if (fragmentManager.isDestroyed() || isStateSaved()) return false;
        if (stack.size() > 1) {
            fragmentManager.popBackStackImmediate(tag, flag);
            synchronized (lock) {
                stack.popFragment(tag, flag);
            }
            return true;
        }
        return false;
    }

    public boolean popBackStackImmediate() {
        if (fragmentManager.isDestroyed() || isStateSaved()) return false;
        if (stack.size() > 1) {
            fragmentManager.popBackStackImmediate();
            synchronized (lock) {
                BaseFragment baseFragment = stack.popFragment();
                stack.popTag();
                if (baseFragment != null && baseFragment.getTargetFragment() != null) {
                    baseFragment.notifyResult();
                }
            }
            return true;
        }
        return false;
    }

    public boolean popBackStackAll() {
        if (fragmentManager.isDestroyed() || isStateSaved()) return false;
        if (stack.size() > 1) {
            while (stack.size() > 1) {
                synchronized (lock) {
                    stack.popFragment();
                    stack.popTag();
                }
                fragmentManager.popBackStack();
            }
            return true;
        }
        return false;
    }

    public void commit() {
        if (fragmentManager.isDestroyed() || isStateSaved()) return;
        if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
            handler.removeCallbacks(execPendingTransactions);
            handler.post(execPendingTransactions);
        } else {
            Log.i(STATE_TAG, "fragmentTransaction is null or empty");
        }
    }

    public boolean executePendingTransactions() {
        if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
            handler.removeCallbacks(execPendingTransactions);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentTransaction = null;
            return fragmentManager.executePendingTransactions();
        }
        return false;
    }

    public boolean isStateSaved() {
        return fragmentManager.isStateSaved();
    }
}
