package com.loconav.locodriver.util

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.loadImage(placeHolder : Int , imageUrl: String){
    Picasso.get().load(imageUrl).error(placeHolder).into(this)
}