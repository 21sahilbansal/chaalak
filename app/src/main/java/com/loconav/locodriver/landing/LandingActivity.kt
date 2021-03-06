package com.loconav.locodriver.landing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.tabs.TabLayout
import com.loconav.locodriver.Constants
import com.loconav.locodriver.Constants.SharedPreferences.Companion.PHOTO_LINK
import com.loconav.locodriver.R
import com.loconav.locodriver.base.BaseActivity
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.landing.ui.main.LandingTabPagerAdapter
import com.loconav.locodriver.user.profile.ProfileActivity
import com.loconav.locodriver.util.LocationWorkManager
import com.loconav.locodriver.util.loadImage
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.fragment_trips.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class LandingActivity : BaseActivity() {

    private val LOCATION_WORKER_TAG = "location_worker_tag"
    val sharedPreferenceUtil: SharedPreferenceUtil by inject()
    var workManager: WorkManager = WorkManager.getInstance()
    private val locationGetterTask =
        PeriodicWorkRequestBuilder<LocationWorkManager>(15, TimeUnit.MINUTES)
    private var viewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        if (checkLocationPermission()) {
            workManager.enqueueUniquePeriodicWork(
                LOCATION_WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                locationGetterTask.build()
            )
        }
        val sectionsPagerAdapter = LandingTabPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager?.adapter = sectionsPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val tabCount = tabs.tabCount
        for (tabIndex in 0 until tabCount) {
            tabs.getTabAt(tabIndex)?.customView = sectionsPagerAdapter.getTabView(tabIndex)
        }
        setTabListener(tabs)

        val profileImageView = findViewById<CardView>(R.id.card_profile)
        if (sharedPreferenceUtil.getData(PHOTO_LINK, "").isEmpty()) {
            iv_profile.setImageResource(R.drawable.ic_user_placeholder)
        } else {
            iv_profile.loadImage(
                R.drawable.ic_user_placeholder,
                sharedPreferenceUtil.getData(PHOTO_LINK, "")
            )
        }
        profileImageView.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
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
                    workManager.enqueueUniquePeriodicWork(
                        LOCATION_WORKER_TAG,
                        ExistingPeriodicWorkPolicy.REPLACE,
                        locationGetterTask.build()
                    )
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

    private fun setTabListener(tab: TabLayout) {
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val title = tab?.customView?.findViewById<TextView>(R.id.tab_title)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    title?.setTextAppearance(R.style.TopNav)
                } else {
                    title?.setTextAppearance(title.context, R.style.TopNav)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val title = tab?.customView?.findViewById<TextView>(R.id.tab_title)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    title?.setTextAppearance(R.style.TopNavActive)
                } else {
                    title?.setTextAppearance(title.context, R.style.TopNavActive)
                }
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.let {
            var notificationType: String? =
                it.getString(Constants.NotificationConstants.NOTIFICATION_TYPE)
            when (notificationType) {
                Constants.NotificationConstants.NOTIFICATION_TYPE_IS_EXPENSE -> {
                    viewPager?.setCurrentItem(1,true)
                }
                Constants.NotificationConstants.NOTIFICATION_TYPE_IS_TRIP -> {
                    viewPager?.setCurrentItem(0,true)
                }
                Constants.NotificationConstants.NOTIFICATION_TYPE_IS_LOCATION -> {
                   //TODO : Add loction update method to send location coordinates to server
                }
                else ->{
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        intent?.extras?.let {
            var notificationType: String? =
                it.getString(Constants.NotificationConstants.NOTIFICATION_TYPE)
            when (notificationType) {
                Constants.NotificationConstants.NOTIFICATION_TYPE_IS_EXPENSE -> {
                    viewPager?.setCurrentItem(1,true)
                    //TODO: Add constants for tab
                }
                Constants.NotificationConstants.NOTIFICATION_TYPE_IS_TRIP -> {
                    viewPager?.setCurrentItem(0,true)
                }
                Constants.NotificationConstants.NOTIFICATION_TYPE_IS_LOCATION -> {
                    //TODO : Add loction update method to send location coordinates to server
                }
                else ->{
                }
            }
        }
    }
    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1000
    }
}