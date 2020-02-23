package com.loconav.locodriver.landing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle

import com.google.android.material.tabs.TabLayout

import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity

import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.loconav.locodriver.Constants.SharedPreferences.Companion.PHOTO_LINK

import com.loconav.locodriver.R
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.landing.ui.main.SectionsPagerAdapter
import com.loconav.locodriver.user.profile.ProfileActivity
import com.loconav.locodriver.util.LocationWorkManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_landing.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class LandingActivity : AppCompatActivity() {

    val LOCATION_WORKER_TAG="location_worker_tag"
    val picasso: Picasso by inject()
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    var workManager: WorkManager = WorkManager.getInstance()
    val locationGetterTask =
        PeriodicWorkRequestBuilder<LocationWorkManager>(15, TimeUnit.MINUTES)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        if (checkLocationPermission()) {
            workManager.enqueueUniquePeriodicWork(LOCATION_WORKER_TAG,ExistingPeriodicWorkPolicy.REPLACE,locationGetterTask.build())
        }
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val profileImageView = findViewById<CardView>(R.id.card_profile)
        if (sharedPreferenceUtil.getData(PHOTO_LINK, "").isEmpty()) {
            iv_profile.setImageResource(R.drawable.ic_user_placeholder)
        } else {
            picasso.load(sharedPreferenceUtil.getData(PHOTO_LINK, ""))
                .error(R.drawable.ic_user_placeholder).into(iv_profile)
        }
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        call_fab.setOnClickListener{
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"9650793733"))
            startActivity(phoneIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    workManager.enqueueUniquePeriodicWork(LOCATION_WORKER_TAG,ExistingPeriodicWorkPolicy.REPLACE,locationGetterTask.build())
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1000
    }
}