package com.loconav.locodriver.Trips

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
        initData(tripActionConfirmationDialog)
        setListeners(tripActionConfirmationDialog)
        return builder
    }

    fun initData(view:View?) {
        val subHeading=view?.findViewById<TextView>(R.id.tv_confimation_sub_heading)
//        subHeading?.text=String.format(getString(R.string.subheading_confirmation_dialog),TripStateGeneratorUtil.getCurrentState())
//        setListeners(view)
    }

    private fun setListeners(view:View?) {
        val declineConfirmation=view?.findViewById<TextView>(R.id.tv_confimation_decline)
        declineConfirmation?.setOnClickListener(closeDialogClickListener)
        val acceptingConfirmation =view?.findViewById<Button>(R.id.button_confirmation_yes)
//        acceptingConfirmation?.setOnClickListener(confirmationClickListener)
    }

    private val closeDialogClickListener = View.OnClickListener {
        dismiss()
    }
//    private val confirmationClickListener = View.OnClickListener {
//        TripStateGeneratorUtil.getCurrentState()?.let{
//            TripStateGeneratorUtil.getNextState(it)
//        }
//        TripStateGeneratorUtil.getCurrentState()
//    }

    override fun getScreenName(): String? {
        return null
    }
}
