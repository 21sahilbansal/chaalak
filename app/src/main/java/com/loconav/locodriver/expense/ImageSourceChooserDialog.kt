package com.loconav.locodriver.expense

import android.Manifest
import android.Manifest.permission.*
import android.app.Dialog
import android.os.Bundle
import com.loconav.locodriver.base.BaseDialogFragment
import kotlinx.android.synthetic.main.image_chooser_dialog.*
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.loconav.locodriver.Constants.PermisionsConstant.Companion.CAMERA_REQUEST_CODE
import com.loconav.locodriver.Constants.PermisionsConstant.Companion.GALLERY_REQUEST_CODE
import com.loconav.locodriver.base.BaseActivity
import com.loconav.locodriver.language.DialogLanguageAdapter
import kotlinx.android.synthetic.main.image_chooser_dialog.view.*
import android.content.ContentValues
import com.loconav.locodriver.R


class ImageSourceChooserDialog : BaseDialogFragment() {

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.image_chooser_dialog, container, false)
        view.tv_gallery_selection.setOnClickListener {
            if (!checkPermission(READ_EXTERNAL_STORAGE)) {
                requestPermssion(
                    arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
                    GALLERY_REQUEST_CODE
                )
            } else {
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                activity?.startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
            }
            dismiss()
        }

        view.tv_camera_selection.setOnClickListener {
            if (!checkPermission(CAMERA)) {
                requestPermssion(arrayOf(CAMERA), CAMERA_REQUEST_CODE)
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activity?.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }
            dismiss()
        }

        return view
    }

    private fun checkPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermssion(permission: Array<String>, requestCode: Int) {
        super.getActivity()?.let {
            ActivityCompat.requestPermissions(
                it as BaseActivity,
                permission,
                requestCode
            )
        }
    }

    override val layoutId: Int = R.layout.image_chooser_dialog
}