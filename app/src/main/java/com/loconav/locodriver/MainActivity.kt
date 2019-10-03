package com.loconav.locodriver


import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.room.CurrentCoordinateDao
import com.loconav.locodriver.location.AutoStartPermissionHelper
//import com.loconav.locodriver.location.LocationUpdatesService
//import com.loconav.locodriver.location.Utils
import com.loconav.locodriver.test.util.MapUtil
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class MainActivity() : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener,
    OnMapReadyCallback {


    // The BroadcastReceiver used to listen from broadcasts from the service.
//    private var myReceiver: MyReceiver? = null

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, "$exception handled !")
    }

    // A reference to the service used to get location updates.
//    private var mService: LocationUpdatesService? = null

    // Tracks the bound state of the service.
    private var mBound = false

    // UI elements.
    private var mRequestLocationUpdatesButton: Button? = null
    private var mRemoveLocationUpdatesButton: Button? = null

    private var googleMap: GoogleMap?=null

    private var pathDrawn = false

    // Monitors the state of the connection to the service.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            val binder = service as LocationUpdatesService.LocalBinder
//            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
//            mService = null
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        myReceiver = MyReceiver()


        setContentView(R.layout.activity_main)

        // Check that the user hasn't revoked permissions by going to Settings.
//        if (Utils.requestingLocationUpdates(this)) {
//            if (!checkPermissions()) {
//                requestPermissions()
//            }
//        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        AutoStartPermissionHelper.getInstance().getAutoStartPermission(baseContext)
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        mRequestLocationUpdatesButton =
            findViewById<View>(R.id.request_location_updates_button) as Button
        mRemoveLocationUpdatesButton =
            findViewById<View>(R.id.remove_location_updates_button) as Button

        mRequestLocationUpdatesButton!!.setOnClickListener {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
//                mService!!.requestLocationUpdates()
            }
        }

//        mRemoveLocationUpdatesButton!!.setOnClickListener { mService!!.removeLocationUpdates() }

        // Restore the state of the buttons when the activity (re)launches.
//        setButtonsState(Utils.requestingLocationUpdates(this))

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
//        bindService(
//            Intent(this, LocationUpdatesService::class.java), mServiceConnection,
//            Context.BIND_AUTO_CREATE
//        )
    }

    override fun onResume() {
        super.onResume()
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//            myReceiver!!,
//            IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
//        )

        try {
            googleMap?.run {
                if(!pathDrawn)
                    drawPath()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }


    }

    private fun drawPath() = GlobalScope.launch(Dispatchers.Main + handler){
        googleMap.clear()
        val mapUtil = MapUtil()
        val coordinates = async(Dispatchers.IO) {getCoordinatesFromDb()}
        mapUtil.moveCameraToPolyline(mapUtil.drawPolyLine(coordinates.await(), googleMap!!))
        mapUtil.moveCameraToPosition(coordinates.await().last(), 16, googleMap!!)
    }


    override fun onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
        super.onPause()
    }

    override fun onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection)
            mBound = false
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            Snackbar.make(
                findViewById(R.id.activity_main),
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
//                mService!!.requestLocationUpdates()
            } else {
                // Permission denied.
                setButtonsState(false)
                Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                        val uri = Uri.fromParts(
//                            "package",
//                            BuildConfig.APPLICATION_ID, null
//                        )
//                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
        }
    }

    override fun onMapReady(mMap: GoogleMap) {
        this.googleMap = mMap
        try {
            drawPath()
        }catch (e : Exception) {
            e.printStackTrace()
        }
    }


    fun getCoordinatesFromDb(): List<LatLng> {
        val latlngs = ArrayList<LatLng>()
        val db = AppDatabase(baseContext)
        val coordinates = db.currentCoordinateDao().getAll()
        coordinates.forEach{ currentCoordinate ->
            val latLng = LatLng(currentCoordinate.lat, currentCoordinate.lng)
            latlngs.add(latLng)
        }
        return latlngs
    }


    /**
     * Receiver for broadcasts sent by [LocationUpdatesService].
     */
//    private inner class MyReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val location =
//                intent.getParcelableExtra<Location>(LocationUpdatesService.EXTRA_LOCATION)
//            if (location != null) {
//                Toast.makeText(
//                    this@MainActivity, Utils.getLocationText(location, context),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        // Update the buttons state depending on whether location updates are being requested.
//        if (s == Utils.KEY_REQUESTING_LOCATION_UPDATES) {
//            setButtonsState(
//                sharedPreferences.getBoolean(
//                    Utils.KEY_REQUESTING_LOCATION_UPDATES,
//                    false
//                )
//            )
//        }
    }

    private fun setButtonsState(requestingLocationUpdates: Boolean) {
        if (requestingLocationUpdates) {
            mRequestLocationUpdatesButton!!.isEnabled = false
            mRemoveLocationUpdatesButton!!.isEnabled = true
        } else {
            mRequestLocationUpdatesButton!!.isEnabled = true
            mRemoveLocationUpdatesButton!!.isEnabled = false
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        // Used in checking for runtime permissions.
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }


    override fun onDestroy() {
        super.onDestroy()
    }


}
