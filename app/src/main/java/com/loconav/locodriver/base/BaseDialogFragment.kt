package com.loconav.locodriver.base

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.loconav.locodriver.R


abstract class BaseDialogFragment : DialogFragment() {

    abstract val layoutId:Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = this.activity?.layoutInflater?.inflate(
            layoutId,
            LinearLayout(this.activity),
            false
        )
        val builder = Dialog(dialog!!.context)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.setCanceledOnTouchOutside(false)
        builder.setContentView(dialog)
        builder.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return builder
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.let { dialog ->
            dialog.window?.let { window ->
                window.attributes.windowAnimations = R.style.DialogWindowAnimation
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    }


    protected fun isSafe(): Boolean {
        return !(this.isRemoving || this.activity == null || this.isDetached || !this.isAdded || this.view == null)
    }


    protected fun getDialogName(): String {
        return javaClass.simpleName
    }

    abstract fun getScreenName(): String?

}
