package com.loconav.locodriver.expense

import com.loconav.locodriver.base.PubSubEvent

class ImageSelectionEvent(message: String, `object`: Any? = null) :
    PubSubEvent(message = message, `object` = `object`) {
    constructor(message: String) : this(message, null)

    companion object {
        const val IMAGE_SELECTED = "image_selected"
        const val DISABLE_ADD_IMAGE = "disable_add_image"
        const val ENABLE_ADD_IMAGE = "enable_add_image"
        const val REMOVE_IMAGE = "remove_image"
    }
}