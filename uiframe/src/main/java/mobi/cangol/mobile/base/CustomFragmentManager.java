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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.EmptyStackException;
import java.util.Stack;

import mobi.cangol.mobile.logging.Log;

public class CustomFragmentManager {
    private static final String STATE_TAG = "CustomFragmentManager";
    private Stack<BaseFragment> stack = new Stack<BaseFragment>();
    private Stack<String> tagStack = new Stack<String>();
    private Object lock = new Object();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int containerId;
    private FragmentActivity fActivity;
    private final Runnable execPendingTransactions = new Runnable() {
        @Override
        public void run() {
            if (fragmentTransaction != null && fActivity != null) {
                try{
                    fragmentTransaction.commitAllowingStateLoss();
                    fragmentManager.executePendingTransactions();
                    fragmentTransaction = null;
                }catch (IllegalStateException e){
                    Log.e(STATE_TAG, "IllegalStateException" + e.getMessage());
                }
            }
        }
    };
    private Handler handler;
    private int enterAnimation;
    private int exitAnimation;
    private int popStackEnterAnimation;
    private int popStackExitAnimation;
    private boolean firstUseAnim = false;

    private CustomFragmentManager(FragmentActivity fActivity, int containerId, FragmentManager fragmentManager) {
        this.fActivity = fActivity;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        handler = new Handler();
    }

    public static CustomFragmentManager forContainer(FragmentActivity activity, int containerId,
                                                     FragmentManager fragmentManager) {
        return new CustomFragmentManager(activity, containerId, fragmentManager);
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
        for (String tag : tagStack) {
            Log.i(STATE_TAG, "tag =" + tag);
            stackTags[i++] = tag;
        }

        outState.putStringArray(STATE_TAG, stackTags);
    }

    public void restoreState(Bundle state) {
        String[] stackTags = state.getStringArray(STATE_TAG);
        for (String tag : stackTags) {
            BaseFragment f = (BaseFragment) fragmentManager.findFragmentByTag(tag);
            stack.add(f);
            tagStack.add(tag);
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
        if (clazz.isAssignableFrom(BaseDialogFragment.class))
            throw new IllegalStateException("DialogFragment can not be attached to a container view");
//        if (stack.size() > 0) {
//            BaseFragment first = stack.firstElement();
//            if (first != null && tag.equals(tagStack.firstElement())) {
//                Log.i(STATE_TAG,"firstElement TAG="+tag);
//                if (customFragmentTransaction == null || !customFragmentTransaction.fillCustomAnimations(beginTransaction())) {
//                    if (enterAnimation > 0 && exitAnimation > 0 && popStackEnterAnimation > 0 && popStackExitAnimation > 0) {
//                        beginTransaction().setCustomAnimations(enterAnimation, exitAnimation, popStackEnterAnimation, popStackExitAnimation);
//                    } else if (enterAnimation > 0 && exitAnimation > 0) {
//                        beginTransaction().setCustomAnimations(enterAnimation, exitAnimation);
//                    }else{
//                        beginTransaction();
//                    }
//                }
//                Log.i(STATE_TAG,"while pop");
//                while (stack.size() > 1) {
//                    synchronized (lock) {
//                        Log.i(STATE_TAG,"pop "+tagStack.peek());
//                        stack.pop();
//                        tagStack.pop();
//                    }
//                    fragmentManager.popBackStack();
//                }
//                return;
//            }
//
//            BaseFragment last = stack.peek();
//            if (last != null && clazz.isInstance(last)) {
////				if (last.isCleanStack()){
////					//return;//导致 fragmentTransaction 为null
////				}else
//                if (last.isSingleton()) {
//                    if (tag.equals(tagStack.peek())) {
//                        return;
//                    } else {
//                        synchronized (lock) {
//                            stack.pop();
//                            tagStack.pop();
//                        }
//                        fragmentManager.popBackStack();
//                    }
//                } else {
//                    //
//                }
//            }
//        }
        BaseFragment fragment = (BaseFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            Log.i(STATE_TAG,"fragment=null newInstance");
            fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
            if (fragment.isCleanStack()) {
                Log.i(STATE_TAG,"fragment isCleanStack=true,while pop all");
                while (stack.size() > 0) {
                    synchronized (lock) {
                        stack.pop();
                        tagStack.pop();
                    }
                    fragmentManager.popBackStack();
                }
            }else{
                Log.i(STATE_TAG,"fragment isCleanStack=false");
                if(fragment.isSingleton()&&tagStack.contains(tag)){
                    Log.i(STATE_TAG,"fragment isSingleton=true,while pop all");
                    while (!tag.equals(tagStack.peek())) {
                        synchronized (lock) {
                            stack.pop();
                            tagStack.pop();
                        }
                        fragmentManager.popBackStack();
                    }
                    synchronized (lock) {
                        stack.pop();
                        tagStack.pop();
                    }
                    fragmentManager.popBackStack();
                    Log.i(STATE_TAG,"fragment newInstance");
                    fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
                }
            }
        }else {
            Log.i(STATE_TAG,"fragment is exist");
            if (fragment.isCleanStack()) {
                Log.i(STATE_TAG,"fragment isCleanStack=true,while pop all");
                while (stack.size() > 1) {
                    synchronized (lock) {
                        stack.pop();
                        tagStack.pop();
                    }
                    fragmentManager.popBackStack();
                }
                fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
            }else{
                Log.i(STATE_TAG,"fragment isCleanStack=false");
                if(!fragment.isSingleton()){
                    Log.i(STATE_TAG,"fragment isSingleton=false,newInstance");
                    fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
                }else{
                    Log.i(STATE_TAG,"fragment isSingleton=true,while pop all");
                    while (!tag.equals(tagStack.peek())) {
                        synchronized (lock) {
                            stack.pop();
                            tagStack.pop();
                        }
                        fragmentManager.popBackStack();
                    }
                    synchronized (lock) {
                        stack.pop();
                        tagStack.pop();
                    }
                    fragmentManager.popBackStack();
                    fragment = (BaseFragment) Fragment.instantiate(fActivity, clazz.getName(), args);
                }
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
                }else{
                    beginTransaction();
                }
            }
        } else {
            //
        }

        attachFragment(fragment, tag);
        synchronized (lock) {
            stack.add(fragment);
            tagStack.add(tag);
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
        return stack.peek();
    }

    public int size() {
        return stack.size();
    }

    public boolean popBackStack() {
        if (stack.size() > 1) {
            fragmentManager.popBackStackImmediate();
            synchronized (lock) {
                BaseFragment baseFragment = stack.pop();
                tagStack.pop();
                if (baseFragment.getTargetFragment() != null) {
                    baseFragment.notifyResult();
                }
            }
            return true;
        }
        return false;
    }
    public boolean popBackStackAll() {
        if(stack.size() > 1){
            while (stack.size() > 1) {
                synchronized (lock) {
                    stack.pop();
                    tagStack.pop();
                }
                fragmentManager.popBackStack();
            }
        }
        return true;
    }

    public void commit() {
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

}