package mobi.cangol.mobile.base

import android.support.v4.app.FragmentTransaction

class CustomFragmentTransaction {
    private var mSource: BaseFragment? = null
    private var mRequestCode: Int = 0
    private var mEnterAnimation: Int = 0
    private var mExitAnimation: Int = 0
    private var mPopStackEnterAnimation: Int = 0
    private var mPopStackExitAnimation: Int = 0

    fun setTargetFragment(source: BaseFragment, requestCode: Int): CustomFragmentTransaction {
        this.mSource = source
        this.mRequestCode = requestCode
        return this
    }

    fun setCustomAnimations(enter: Int, exit: Int): CustomFragmentTransaction {
        this.mEnterAnimation = enter
        this.mExitAnimation = exit
        return this
    }

    fun setCustomAnimations(enter: Int, exit: Int, popEnter: Int, popExit: Int): CustomFragmentTransaction {
        this.mEnterAnimation = enter
        this.mExitAnimation = exit
        this.mPopStackEnterAnimation = popEnter
        this.mPopStackExitAnimation = popExit
        return this
    }

    fun fillTargetFragment(target: BaseFragment) {
        if (mSource != null && mRequestCode != -1)
            target.setTargetFragment(mSource, mRequestCode)
    }


    fun fillCustomAnimations(fragmentTransaction: FragmentTransaction): Boolean {
        return if (mEnterAnimation > 0 && mExitAnimation > 0 && mPopStackEnterAnimation > 0 && mPopStackExitAnimation > 0) {
            fragmentTransaction.setCustomAnimations(mEnterAnimation, mExitAnimation, mPopStackEnterAnimation, mPopStackExitAnimation)
            true
        } else if (mEnterAnimation > 0 && mExitAnimation > 0) {
            fragmentTransaction.setCustomAnimations(mEnterAnimation, mExitAnimation)
            true
        } else {
            false
        }
    }
}