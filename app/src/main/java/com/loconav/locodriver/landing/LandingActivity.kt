package com.loconav.locodriver.landing

import android.content.Intent
import android.os.Bundle

import com.google.android.material.tabs.TabLayout

import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity

import androidx.cardview.widget.CardView
import com.loconav.locodriver.Constants.SHARED_PREFERENCE.Companion.PHOTO_LINK

import com.loconav.locodriver.R
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.landing.ui.main.SectionsPagerAdapter
import com.loconav.locodriver.user.profile.ProfileActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_landing.*
import org.koin.android.ext.android.inject

class LandingActivity : AppCompatActivity() {

    val picasso: Picasso by inject()
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val profileImageView = findViewById<CardView>(R.id.card_profile)
        picasso.load(sharedPreferenceUtil.getData(PHOTO_LINK, "")).error(R.drawable.ic_profile).into(iv_profile)
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}