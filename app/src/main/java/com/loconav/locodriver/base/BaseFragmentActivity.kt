package com.loconav.locodriver.base

import android.content.Context
import com.loconav.locodriver.R
import com.loconav.locodriver.util.LocaleHelper

abstract class BaseFragmentActivity : BaseActivity() {

    abstract val frameId: Int

    fun inflateAddFragment(baseFragment: BaseFragment, addToBackStack: Boolean, backStackTag: String) {
        val fragmentManager = supportFragmentManager
        if (addToBackStack)
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_laod, R.anim.fragment_unload).add(frameId, baseFragment).addToBackStack(backStackTag).commit()
        else
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_laod, R.anim.fragment_unload).add(frameId, baseFragment).commit()
    }


    fun inflateReplaceFragment(baseFragment: BaseFragment, addToBackStack: Boolean, backStackTag: String) {
        val fragmentManager = supportFragmentManager
        if (addToBackStack)
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_laod, R.anim.fragment_unload).replace(frameId, baseFragment).addToBackStack(backStackTag).commit()
        else
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_laod, R.anim.fragment_unload).replace(frameId, baseFragment).commit()
    }
}