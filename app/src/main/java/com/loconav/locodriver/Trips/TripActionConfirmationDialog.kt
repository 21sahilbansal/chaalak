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
import kotlinx.android.synthetic.main.trip_activity_confirmation_dialog.view.*

class TripActionConfirmationDialog : BaseDialogFragment() {

    override val layoutId: Int = R.layout.trip_activity_confirmation_dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initData()
        setListeners()
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initData() {
        view?.tv_confimation_sub_heading?.text = String.format(
            getString(R.string.subheading_confirmation_dialog),""
//            TripStateGeneratorUtil.getCurrentState()
        )
    }

    private fun setListeners() {
        view?.tv_confimation_decline?.setOnClickListener(closeDialogClickListener)
        view?.button_confirmation_yes?.setOnClickListener(confirmationClickListener)
    }

    private val closeDialogClickListener = View.OnClickListener {
        dismiss()
    }
    private val confirmationClickListener = View.OnClickListener {
        //        TripStateGeneratorUtil.getCurrentState()?.let{
//            TripStateGeneratorUtil.getNextState(it)
//        }
//        TripStateGeneratorUtil.getCurrentState()
    }

    override fun getScreenName(): String? {
        return null
    }
}
