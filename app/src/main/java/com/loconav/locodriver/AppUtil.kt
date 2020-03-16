package com.loconav.locodriver

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.loconav.locodriver.Constants.SharedPreferences.Companion.DEVICE_ID
import com.loconav.locodriver.application.LocoDriverApplication
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.notification.htttpService.FCMHttpApiService
import com.loconav.locodriver.splash.SplashActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.net.NetworkInterface
import java.util.*


object AppUtils : KoinComponent {

    val sharedPreferenceUtil: SharedPreferenceUtil by inject()

    val isKitKatOrHigher: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    val versionCode: Int
        get() = BuildConfig.VERSION_CODE


    val appContext: Context by inject()
    val fcmHttpApiService : FCMHttpApiService by inject ()

    val db: AppDatabase by inject()


    val macAddr: String
        get() {
            try {
                val networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in networkInterfaces) {
                    if (!nif.name.equals("wlan0", ignoreCase = true)) continue

                    val macBytes = nif.hardwareAddress ?: return ""

                    val macAddress = StringBuilder()
                    for (b in macBytes) {
                        macAddress.append(String.format("%02X:", b))
                    }

                    if (macAddress.length > 0) {
                        macAddress.deleteCharAt(macAddress.length - 1)
                    }
                    return macAddress.toString()
                }
            } catch (ex: Exception) {
            }

            return "02:00:00:00:00:00"
        }


    fun getScreenWidth(context: Context): Float {
        val screenWidthInPx = getScreenWidthPx(context)
        return convertPixelsToDp(screenWidthInPx, context)
    }

    fun getScreenWidthPx(context: Context): Float {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        return display.width.toFloat()
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertDpToPixels(dp: Float, context: Context): Float {
        val resources = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    fun getVersionCode(): String {
        return BuildConfig.VERSION_CODE.toString()
    }


    fun clearAppData() {
        db.clearAllTables()
        sharedPreferenceUtil.deleteAllData()
    }


    fun logout() {
        GlobalScope.launch(IO) {
            fcmHttpApiService.deleteFCMDeviceId()
            clearAppData()
            launch(Dispatchers.Main) {
                relaunchApp()
            }
        }

    }


    fun relaunchApp() {
        val i = Intent(appContext, SplashActivity::class.java)
        i.flags =
            FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        appContext.startActivity(i)
    }

    fun getDeviceId(): String? {
        var uuid: String? = sharedPreferenceUtil.getData(DEVICE_ID, "")
        if (uuid!!.isEmpty()) {
            uuid = Settings.Secure.getString(
                LocoDriverApplication.instance.getContentResolver(),
                Settings.Secure.ANDROID_ID
            )
            if (uuid == null) {
                val wifiManager =
                    LocoDriverApplication.instance.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                uuid = wifiManager.connectionInfo.macAddress
            }
            if (uuid == null) {
                uuid = UUID.randomUUID().toString()
            }
            sharedPreferenceUtil.saveData(DEVICE_ID, uuid)
        }
        return uuid
    }

}