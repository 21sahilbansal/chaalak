package com.loconav.locodriver.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.loconav.locodriver.R


abstract class BaseDialogFragment : DialogFragment() {

    interface OnDialogCompletionListener {
        fun onComplete()
    }

    protected var onCompletionListener = null
        set(value) {
            field = value
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!
            .attributes.windowAnimations = R.style.DialogWindowAnimation
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected fun isSafe(): Boolean {
        return !(this.isRemoving || this.activity == null || this.isDetached || !this.isAdded || this.view == null)
    }


    protected fun getDialogName(): String {
        return javaClass.simpleName
    }

    abstract fun getScreenName(): String?

}
