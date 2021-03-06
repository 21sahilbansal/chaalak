package com.loconav.locodriver.expense

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.EDITABLE
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.LIST_POSITION
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import com.loconav.locodriver.expense.ImageSelectionEvent.Companion.DELETE_CONFIRMATION_DIALOG
import com.loconav.locodriver.expense.ImageSelectionEvent.Companion.REMOVE_IMAGE
import com.loconav.locodriver.expense.addExpense.DeleteImageConfirmationDialog
import com.loconav.locodriver.util.loadImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_document_image.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class DocumentImageFragment : BaseFragment(), KoinComponent {
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        val imageUri = arguments?.get(IMAGE_URI).toString()
        val editable = arguments?.getBoolean(EDITABLE)
        val position = arguments?.getInt(LIST_POSITION)
        document_image_iv.loadImage(R.drawable.ic_user_placeholder, imageUri)
        if (editable == false) {
            document_image_delete_iv.visibility = View.GONE
        }
        document_image_back_iv.setOnClickListener {
            activity?.finish()
        }
        document_image_delete_iv.setOnClickListener {
            position?.let {
                DeleteImageConfirmationDialog(position).show(
                    childFragmentManager,
                    Constants.FRAGMENT_TAG.DELETE_IMAGE_DIALOG
                )
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImageCaptureEventRecieved(imageSelectionEvent: ImageSelectionEvent) {
        when (imageSelectionEvent.message) {
            REMOVE_IMAGE ->{
                activity?.finish()
            }
        }
    }



    override fun getLayoutId(): Int {
        return R.layout.fragment_document_image
    }

    companion object {
        fun getInstance(uri: String, position: Int, editable: Boolean): DocumentImageFragment {
            val documentImageFragment = DocumentImageFragment()
            val bundle = Bundle()
            bundle.putString(IMAGE_URI, uri)
            bundle.putInt(LIST_POSITION, position)
            bundle.putBoolean(EDITABLE, editable)
            documentImageFragment.arguments = bundle
            return documentImageFragment
        }

        private const val IMAGE_URI = "image_uri"
    }
}