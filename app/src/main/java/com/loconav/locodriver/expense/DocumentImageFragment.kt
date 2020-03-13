package com.loconav.locodriver.expense

import android.os.Bundle
import android.text.Editable
import android.view.View
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_document_image.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class DocumentImageFragment:BaseFragment(),KoinComponent {
    private val picasso:Picasso by inject()
    override fun onViewInflated(view: View, savedInstanceState: Bundle?) {
        val imageuri=arguments?.get(IMAGE_URI).toString()
        val editable=arguments?.getBoolean(IMAGE_EDITABLE)
        picasso.load(imageuri).error(R.drawable.ic_user_placeholder).into(document_image_iv)
        if(editable==false){
            document_image_delete_iv.visibility = View.GONE
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_document_image
    }

    companion object {
        fun getInstance(uri:String,position : Int, editable: Boolean): DocumentImageFragment {
            val documentImageFragment=DocumentImageFragment()
            val bundle = Bundle()
            bundle.putString(IMAGE_URI, uri)
            bundle.putInt(IMAGE_POSITION,position)
            bundle.putBoolean(IMAGE_EDITABLE,editable)
            documentImageFragment.arguments = bundle
            return documentImageFragment
        }

        private const val IMAGE_URI = "image_uri"
        private const val IMAGE_POSITION = "image_position"
        private const val IMAGE_EDITABLE = "image_editable"
    }
}