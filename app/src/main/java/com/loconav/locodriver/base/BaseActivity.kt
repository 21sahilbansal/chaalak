package com.loconav.locodriver.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.loconav.locodriver.util.LocaleHelper

abstract class BaseActivity  : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}
