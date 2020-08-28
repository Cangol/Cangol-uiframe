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

import androidx.fragment.app.FragmentTransaction;


public class CustomFragmentTransaction {
    private BaseFragment mSource;
    private int mRequestCode;
    private int mEnterAnimation;
    private int mExitAnimation;
    private int mPopStackEnterAnimation;
    private int mPopStackExitAnimation;

    public CustomFragmentTransaction setTargetFragment(BaseFragment source, int requestCode) {
        this.mSource = source;
        this.mRequestCode = requestCode;
        return this;
    }

    public CustomFragmentTransaction setCustomAnimations(int enter, int exit) {
        this.mEnterAnimation = enter;
        this.mExitAnimation = exit;
        return this;
    }

    public CustomFragmentTransaction setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
        this.mEnterAnimation = enter;
        this.mExitAnimation = exit;
        this.mPopStackEnterAnimation = popEnter;
        this.mPopStackExitAnimation = popExit;
        return this;
    }

    public void fillTargetFragment(BaseFragment target) {
        if (mSource != null && mRequestCode != -1)
            target.setTargetFragment(mSource, mRequestCode);
    }


    public boolean fillCustomAnimations(FragmentTransaction fragmentTransaction) {
        if (mEnterAnimation > 0 && mExitAnimation > 0 && mPopStackEnterAnimation > 0 && mPopStackExitAnimation > 0) {
            fragmentTransaction.setCustomAnimations(mEnterAnimation, mExitAnimation, mPopStackEnterAnimation, mPopStackExitAnimation);
            return true;
        } else if (mEnterAnimation > 0 && mExitAnimation > 0) {
            fragmentTransaction.setCustomAnimations(mEnterAnimation, mExitAnimation);
            return true;
        } else {
            return false;
        }
    }
}
