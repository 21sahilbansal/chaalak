package com.loconav.locodriver.expense

import android.os.Bundle
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.DOCUMENT_IMAGE
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.EDITABLE
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.LIST_POSITION
import com.loconav.locodriver.Constants.ExpenseConstants.Companion.SOURCE
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseFragmentActivity

class DocumentImageActivity : BaseFragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.document_image_activity)
        intent?.extras?.let {
            if (it.getString(SOURCE).equals(DOCUMENT_IMAGE)) {
                val uri = intent.data
                val position = it.getInt(LIST_POSITION)
                val editable = it.getBoolean(EDITABLE)
                if (uri == null || position == null || editable == null) return
                inflateAddFragment(
                    DocumentImageFragment.getInstance(uri.toString(), position, editable), false,
                    Constants.FRAGMENT_TAG.DOCUMENT_IMAGE_FRAGMENT
                )
            }
        }
    }

    override val frameId: Int
        get() = R.id.fragment_container

}