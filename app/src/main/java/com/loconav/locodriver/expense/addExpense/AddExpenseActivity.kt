package com.loconav.locodriver.expense.addExpense

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.FRAGMENT_TAG.Companion.DOCUMENT_IMAGE_FRAGMENT
import com.loconav.locodriver.Constants.PermisionsConstant.Companion.CAMERA_REQUEST_CODE
import com.loconav.locodriver.Constants.PermisionsConstant.Companion.GALLERY_REQUEST_CODE
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity
import com.loconav.locodriver.expense.ImageSelectionEvent
import com.loconav.locodriver.expense.ImageUtil
import org.greenrobot.eventbus.EventBus
import java.security.Permission
import java.security.Permissions

class AddExpenseActivity : BaseFragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        inflateReplaceFragment(
            AddExpenseFragment.getInstance(),
            false,
            Constants.FRAGMENT_TAG.ADD_EXPENSE_FRAGEMENT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    EventBus.getDefault().post(
                        ImageSelectionEvent(
                            ImageSelectionEvent.IMAGE_SELECTED,
                            intent?.data
                        )
                    )

                }
                CAMERA_REQUEST_CODE -> {

                    val bitmap = intent?.extras?.get("data") as Bitmap
                    val uri = ImageUtil.getImageUri(applicationContext, bitmap)
                    EventBus.getDefault().post(
                        ImageSelectionEvent(
                            ImageSelectionEvent.IMAGE_SELECTED,
                            uri
                        )
                    )

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, intent)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    this.startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                }
            }
            CAMERA_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val cameraIntent = Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                    )
                    this.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                }
            }
        }
    }

    override val frameId: Int
        get() = R.id.fragment_container
}