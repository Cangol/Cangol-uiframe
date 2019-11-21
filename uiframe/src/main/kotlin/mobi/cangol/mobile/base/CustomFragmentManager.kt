package mobi.cangol.mobile.base

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import mobi.cangol.mobile.logging.Log


class CustomFragmentManager private constructor(private var fActivity: FragmentActivity, val containerId: Int, private var fragmentManager: FragmentManager?) {
    private var stack: FragmentStack = FragmentStack()
    private val lock = Any()
    private var fragmentTransaction: FragmentTransaction? = null
    private val execPendingTransactions = Runnable {
        if (fragmentTransaction != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!fActivity.isFinishing && !fActivity.isDestroyed) {
                    try {
                        fragmentTransaction!!.commitAllowingStateLoss()
                        fragmentManager!!.executePendingTransactions()
                        fragmentTransaction = null
                    } catch (e: IllegalStateException) {
                        Log.e(STATE_TAG, "execPendingTransactions", e)
                    }

                }
            } else {
                if (!fActivity.isFinishing) {
                    try {
                        fragmentTransaction!!.commitAllowingStateLoss()
                        fragmentManager!!.executePendingTransactions()
                        fragmentTransaction = null
                    } catch (e: IllegalStateException) {
                        Log.e(STATE_TAG, "execPendingTransactions", e)
                    }

                }
            }
        }
    }
    private val handler=Handler(Looper.getMainLooper())
    private var enterAnimation: Int = 0
    private var exitAnimation: Int = 0
    private var popStackEnterAnimation: Int = 0
    private var popStackExitAnimation: Int = 0
    private var firstUseAnim = false



    fun destroy() {
        this.stack.clear()
        this.handler.removeCallbacks(execPendingTransactions)
        this.fragmentManager = null
    }

    fun setDefaultAnimation(enter: Int, exit: Int, popEnter: Int, popExit: Int) {
        enterAnimation = enter
        exitAnimation = exit
        popStackEnterAnimation = popEnter
        popStackExitAnimation = popExit
    }

    fun saveState(outState: Bundle) {
        executePendingTransactions()
        val stackSize = stack.size()
        val stackTags = arrayOfNulls<String>(stackSize)

        var i = 0
        for (tag in stack.getTags()) {
            Log.i(STATE_TAG, "tag =$tag")
            stackTags[i++] = tag
        }

        outState.putStringArray(STATE_TAG, stackTags)
    }

    fun restoreState(state: Bundle) {
        val stackTags = state.getStringArray(STATE_TAG)
        if (stackTags != null) {
            for (tag in stackTags) {
                val f = fragmentManager!!.findFragmentByTag(tag) as BaseFragment
                stack.addFragment(f)
                stack.addTag(tag)
            }
        }
    }

    private fun beginTransaction(): FragmentTransaction {
        if (fragmentTransaction == null) fragmentTransaction = fragmentManager!!.beginTransaction()
        handler.removeCallbacks(execPendingTransactions)
        return fragmentTransaction!!
    }

    fun replace(clazz: Class<out BaseFragment>, tag: String, args: Bundle?) {
        check(!clazz.isAssignableFrom(BaseDialogFragment::class.java)) { "DialogFragment can not be attached to a container view" }
        this.replace(clazz, tag, args, null)
    }

    fun replace(clazz: Class<out BaseFragment>, tag: String, args: Bundle?, customFragmentTransaction: CustomFragmentTransaction?) {
        if (fragmentManager!!.isDestroyed || isStateSaved()) return
        check(!clazz.isAssignableFrom(BaseDialogFragment::class.java)) { "DialogFragment can not be attached to a container view" }
        var fragment: BaseFragment? = fragmentManager!!.findFragmentByTag(tag) as BaseFragment?
        if (fragment == null) {
            Log.i(STATE_TAG, "fragment=null newInstance")
            fragment = Fragment.instantiate(fActivity, clazz.name, args)as BaseFragment?
            if (fragment!!.isCleanStack()) {
                Log.i(STATE_TAG, "fragment isCleanStack=true,while pop all")
                while (stack.size() > 0) {
                    synchronized(lock) {
                        stack.popFragment()
                        stack.popTag()
                    }
                    fragmentManager!!.popBackStack()
                }
            } else {
                Log.i(STATE_TAG, "fragment isCleanStack=false")
                if (fragment.isSingleton() && stack.containsTag(tag)) {
                    Log.i(STATE_TAG, "fragment isSingleton=true,while pop all")
                    while (tag != stack.peekTag()) {
                        synchronized(lock) {
                            stack.popFragment()
                            stack.popTag()
                        }
                        fragmentManager!!.popBackStack()
                    }
                    synchronized(lock) {
                        stack.popFragment()
                        stack.popTag()
                    }
                    fragmentManager!!.popBackStack()
                    Log.i(STATE_TAG, "fragment newInstance")
                    fragment = Fragment.instantiate(fActivity, clazz.name, args)as BaseFragment?
                }
            }
        } else {
            Log.i(STATE_TAG, "fragment is exist")
            if (fragment.isCleanStack()) {
                if (stack.size() == 1) {
                    if (stack.peekTag() == tag) {
                        return
                    } else {
                        synchronized(lock) {
                            stack.popFragment()
                            stack.popTag()
                        }
                        fragmentManager!!.popBackStack()
                        fragment = Fragment.instantiate(fActivity, clazz.name, args)as BaseFragment?
                    }
                } else {
                    Log.i(STATE_TAG, "fragment isCleanStack=true,while pop all")
                    while (stack.size() > 0) {
                        synchronized(lock) {
                            stack.popFragment()
                            stack.popTag()
                        }
                        fragmentManager!!.popBackStack()
                    }
                    fragment = Fragment.instantiate(fActivity, clazz.name, args)as BaseFragment?
                }
            } else {
                Log.i(STATE_TAG, "fragment isCleanStack=false")
                if (!fragment.isSingleton()) {
                    Log.i(STATE_TAG, "fragment isSingleton=false,newInstance")
                    fragment = Fragment.instantiate(fActivity, clazz.name, args)as BaseFragment?
                } else {
                    Log.i(STATE_TAG, "fragment isSingleton=true,while pop all")
                    while (tag != stack.peekTag()) {
                        synchronized(lock) {
                            stack.popFragment()
                            stack.popTag()
                        }
                        fragmentManager!!.popBackStack()
                    }
                    synchronized(lock) {
                        stack.popFragment()
                        stack.popTag()
                    }
                    fragmentManager!!.popBackStack()
                    fragment = Fragment.instantiate(fActivity, clazz.name, args) as BaseFragment?
                }
            }
        }
        customFragmentTransaction?.fillTargetFragment(fragment!!)

        if (firstUseAnim || stack.size() > 0) {
            //保证第一个填充不适用动画
            if (customFragmentTransaction == null || !customFragmentTransaction.fillCustomAnimations(beginTransaction())) {
                if (enterAnimation > 0 && exitAnimation > 0 && popStackEnterAnimation > 0 && popStackExitAnimation > 0) {
                    beginTransaction().setCustomAnimations(enterAnimation, exitAnimation, popStackEnterAnimation, popStackExitAnimation)
                } else if (enterAnimation > 0 && exitAnimation > 0) {
                    beginTransaction().setCustomAnimations(enterAnimation, exitAnimation)
                } else {
                    beginTransaction()
                }
            }
        }

        attachFragment(fragment, tag)
        synchronized(lock) {
            stack.addFragment(fragment!!)
            stack.addTag(tag)
        }
    }

    fun setFirstUseAnim(firstUseAnim: Boolean) {
        this.firstUseAnim = firstUseAnim
    }

    private fun attachFragment(fragment: Fragment?, tag: String?) {
        if (fragment != null) {
            if (fragment.isDetached) {
                Log.i(STATE_TAG, "attachFragment tag=$tag")
                beginTransaction().attach(fragment)
                if (stack.size() > 0) {
                    beginTransaction().addToBackStack(tag)
                }
            } else if (!fragment.isAdded) {
                Log.i(STATE_TAG, "replaceFragment tag=$tag")
                beginTransaction().replace(containerId, fragment, tag)
                if (stack.size() > 0) {
                    beginTransaction().addToBackStack(tag)
                }
            } else {
                Log.i(STATE_TAG, "fragment state illegal " + fragment)
            }
        } else {
            Log.i(STATE_TAG, "fragment is null")
        }
    }

    fun peek(): BaseFragment? {
        return stack.peekFragment()
    }

    fun size(): Int {
        return stack.size()
    }


    fun popBackStack(): Boolean {
        if (fragmentManager!!.isDestroyed || isStateSaved()) return false
        if (stack.size() > 1) {
            fragmentManager!!.popBackStack()
            synchronized(lock) {
                val baseFragment = stack.popFragment()
                stack.popTag()
                if (baseFragment != null && baseFragment.targetFragment != null) {
                    baseFragment.notifyResult()
                }
            }
            return true
        }
        return false
    }

    fun popBackStack(tag: String, flag: Int): Boolean {
        if (fragmentManager!!.isDestroyed || isStateSaved()) return false
        if (stack.size() > 1) {
            fragmentManager!!.popBackStack(tag, flag)
            synchronized(lock) {
                stack.popFragment(tag, flag)
            }
            return true
        }
        return false
    }

    fun popBackStackImmediate(tag: String, flag: Int): Boolean {
        if (fragmentManager!!.isDestroyed || isStateSaved()) return false
        if (stack.size() > 1) {
            fragmentManager!!.popBackStackImmediate(tag, flag)
            synchronized(lock) {
                stack.popFragment(tag, flag)
            }
            return true
        }
        return false
    }

    fun popBackStackImmediate(): Boolean {
        if (fragmentManager!!.isDestroyed || isStateSaved()) return false
        if (stack.size() > 1) {
            fragmentManager!!.popBackStackImmediate()
            synchronized(lock) {
                val baseFragment = stack.popFragment()
                stack.popTag()
                if (baseFragment != null && baseFragment.targetFragment != null) {
                    baseFragment.notifyResult()
                }
            }
            return true
        }
        return false
    }

    fun popBackStackAll(): Boolean {
        if (fragmentManager!!.isDestroyed || isStateSaved()) return false
        if (stack.size() > 1) {
            while (stack.size() > 1) {
                synchronized(lock) {
                    stack.popFragment()
                    stack.popTag()
                }
                fragmentManager!!.popBackStack()
            }
            return true
        }
        return false
    }

    fun commit() {
        if (fragmentManager!!.isDestroyed || isStateSaved()) return
        if (fragmentTransaction != null && !fragmentTransaction!!.isEmpty) {
            handler.removeCallbacks(execPendingTransactions)
            handler.post(execPendingTransactions)
        } else {
            Log.i(STATE_TAG, "fragmentTransaction is null or empty")
        }
    }

    private fun executePendingTransactions(): Boolean {
        if (fragmentTransaction != null && !fragmentTransaction!!.isEmpty) {
            handler.removeCallbacks(execPendingTransactions)
            fragmentTransaction!!.commitAllowingStateLoss()
            fragmentTransaction = null
            return fragmentManager!!.executePendingTransactions()
        }
        return false
    }
    fun isStateSaved(): Boolean {
        return fragmentManager!!.isStateSaved
    }
    companion object {
        private const val STATE_TAG = "CustomFragmentManager"

        fun forContainer(activity: FragmentActivity, containerId: Int,
                         fragmentManager: FragmentManager): CustomFragmentManager {
            return CustomFragmentManager(activity, containerId, fragmentManager)
        }
    }
}