package com.loconav.locodriver.Trips

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseDialogFragment
import kotlinx.android.synthetic.main.trip_activity_confirmation_dialog.*

class TripActionConfirmationDialog : BaseDialogFragment() {

    var tripActionConfirmationDialog: View? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        tripActionConfirmationDialog = activity?.layoutInflater?.inflate(
            R.layout.trip_activity_confirmation_dialog,
            LinearLayout(activity),
            false
        )
        val builder = Dialog(tripActionConfirmationDialog!!.context)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.setCanceledOnTouchOutside(false)
        builder.setContentView(tripActionConfirmationDialog!!)
        builder.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setListeners()
        return builder
    }

    fun initData() {
        // add string for driver cta for confirmation
    }

    private fun setListeners() {
        tv_confimation_decline?.setOnClickListener(closeDialogClickListener)
        button_confirmation_yes?.setOnClickListener(confirmationClickListener)
    }

    private val closeDialogClickListener = View.OnClickListener {
        dismiss()
    }
    private val confirmationClickListener = View.OnClickListener {
        //Fire event for confirmation to be listened by Fragment
    }

    override fun getScreenName(): String? {
        return null
    }
}
