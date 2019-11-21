package mobi.cangol.mobile.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater

abstract class BaseDialogFragment : BaseFragment(), DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    private var mStyle = 0
    private var mTheme = 0
    private var mCancelable = true
    private var mShowsDialog = true
    private var mBackStackId = -1
    private var mDialog: Dialog? = null
    private var mViewDestroyed: Boolean = false
    private var mDismissed: Boolean = false
    private var mShownByMe: Boolean = false

    fun setStyle(style: Int, theme: Int) {
        this.mStyle = style
        if (this.mStyle == 2 || this.mStyle == 3) {
            this.mTheme = 16973913
        }
        if (theme != 0) {
            this.mTheme = theme
        }

    }

    fun show(manager: FragmentManager, tag: String) {
        if (isStateSaved) return
        this.mDismissed = false
        this.mShownByMe = true
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commit()
    }

    fun show(transaction: FragmentTransaction, tag: String): Int {
        this.mDismissed = false
        this.mShownByMe = true
        transaction.add(this, tag)
        this.mViewDestroyed = false
        this.mBackStackId = transaction.commit()
        return this.mBackStackId
    }

    fun dismiss() {
        if (!isStateSaved)
            this.dismissInternal(false)
    }

    fun dismissAllowingStateLoss() {
        if (!isStateSaved) this.dismissInternal(true)
    }

    fun dismissInternal(allowStateLoss: Boolean) {
        if (!this.mDismissed) {
            this.mDismissed = true
            this.mShownByMe = false
            if (this.mDialog != null) {
                this.mDialog!!.dismiss()
                this.mDialog = null
            }

            this.mViewDestroyed = true
            if (this.mBackStackId >= 0) {
                this.fragmentManager!!.popBackStack(this.mBackStackId, 1)
                this.mBackStackId = -1
            } else {
                val ft = this.fragmentManager!!.beginTransaction()
                ft.remove(this)
                if (allowStateLoss) {
                    ft.commitAllowingStateLoss()
                } else {
                    ft.commit()
                }
            }
        }
    }

    fun getDialog(): Dialog {
        return this.mDialog!!
    }

    fun getTheme(): Int {
        return this.mTheme
    }

    fun isCancelable(): Boolean {
        return this.mCancelable
    }

    fun setCancelable(cancelable: Boolean) {
        this.mCancelable = cancelable
        if (this.mDialog != null) {
            this.mDialog?.setCancelable(cancelable)
        }
    }

    fun isShowsDialog(): Boolean {
        return this.mShowsDialog
    }

    fun setShowsDialog(showsDialog: Boolean) {
        this.mShowsDialog = showsDialog
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (!this.mShownByMe) {
            this.mDismissed = false
        }

    }

    override fun onDetach() {
        super.onDetach()
        if (!this.mShownByMe && !this.mDismissed) {
            this.mDismissed = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mShowsDialog = true
        if (savedInstanceState != null) {
            this.mStyle = savedInstanceState.getInt(SAVED_STYLE, 0)
            this.mTheme = savedInstanceState.getInt(SAVED_THEME, 0)
            this.mCancelable = savedInstanceState.getBoolean(SAVED_CANCELABLE, true)
            this.mShowsDialog = savedInstanceState.getBoolean(SAVED_SHOWS_DIALOG, this.mShowsDialog)
            this.mBackStackId = savedInstanceState.getInt(SAVED_BACK_STACK_ID, -1)
        }

    }

    @SuppressLint("RestrictedApi")
    override fun getLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        if (!this.mShowsDialog) {
            return super.getLayoutInflater(savedInstanceState)
        } else {
            this.mDialog = this.onCreateDialog(savedInstanceState)
            when (this.mStyle) {
                3 -> {
                    this.mDialog!!.window?.addFlags(24)
                    this.mDialog!!.requestWindowFeature(1)
                    return if (this.mDialog != null) this.mDialog!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater else this.activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                }
                1, 2 -> {
                    this.mDialog!!.requestWindowFeature(1)
                    return if (this.mDialog != null) this.mDialog!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater else this.activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                }
                else -> return if (this.mDialog != null) this.mDialog!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater else this.activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
        }
    }

    fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(this.activity, this.mTheme)
    }

    override fun onCancel(dialog: DialogInterface) {
        //do somethings
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (!this.mViewDestroyed) {
            this.dismissInternal(true)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (this.mShowsDialog) {
            val view = this.view
            if (view != null) {
                check(view.parent == null) { "DialogFragment can not be attached to a container view" }
                this.mDialog!!.setContentView(view)
            }

            this.mDialog!!.ownerActivity = this.activity
            this.mDialog!!.setCancelable(this.mCancelable)
            this.mDialog!!.setOnCancelListener(this)
            this.mDialog!!.setOnDismissListener(this)
            if (savedInstanceState != null) {
                val dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG)
                if (dialogState != null) {
                    this.mDialog!!.onRestoreInstanceState(dialogState)
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        if (this.mDialog != null) {
            this.mViewDestroyed = false
            this.mDialog!!.show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this.mDialog != null) {
            outState.putBundle(SAVED_DIALOG_STATE_TAG, this.mDialog!!.onSaveInstanceState())
        }

        if (this.mStyle != 0) {
            outState.putInt(SAVED_STYLE, this.mStyle)
        }

        if (this.mTheme != 0) {
            outState.putInt(SAVED_THEME, this.mTheme)
        }

        if (!this.mCancelable) {
            outState.putBoolean(SAVED_CANCELABLE, this.mCancelable)
        }

        if (!this.mShowsDialog) {
            outState.putBoolean(SAVED_SHOWS_DIALOG, this.mShowsDialog)
        }

        if (this.mBackStackId != -1) {
            outState.putInt(SAVED_BACK_STACK_ID, this.mBackStackId)
        }

    }

    override fun onStop() {
        super.onStop()
        if (this.mDialog != null) {
            this.mDialog!!.hide()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this.mDialog != null) {
            this.mViewDestroyed = true
            this.mDialog!!.dismiss()
            this.mDialog = null
        }

    }

    companion object {

        val STYLE_NORMAL = 0
        val STYLE_NO_TITLE = 1
        val STYLE_NO_FRAME = 2
        val STYLE_NO_INPUT = 3
        private val SAVED_DIALOG_STATE_TAG = "android:savedDialogState"
        private val SAVED_STYLE = "android:style"
        private val SAVED_THEME = "android:theme"
        private val SAVED_CANCELABLE = "android:cancelable"
        private val SAVED_SHOWS_DIALOG = "android:showsDialog"
        private val SAVED_BACK_STACK_ID = "android:backStackId"
    }
}