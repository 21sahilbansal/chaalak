package com.loconav.locodriver.expense.addExpense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseDialogFragment
import com.loconav.locodriver.expense.ImageSelectionEvent
import kotlinx.android.synthetic.main.dialog_delete_confirmation.view.*
import org.greenrobot.eventbus.EventBus

class DeleteImageConfirmationDialog(val position : Int) : BaseDialogFragment() {
    override val layoutId: Int = R.layout.dialog_delete_confirmation

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_delete_confirmation, container, false)
        view.tv_confimation_decline.setOnClickListener {
            dismiss()
        }
        view.button_confirmation_yes.setOnClickListener{
            EventBus.getDefault().post(ImageSelectionEvent(ImageSelectionEvent.REMOVE_IMAGE,position))
            dismiss()
        }
        return view
    }
}