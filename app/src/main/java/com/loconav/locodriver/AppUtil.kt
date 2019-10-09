package com.loconav.locodriver

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.net.NetworkInterface
import java.util.*
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.loconav.locodriver.db.room.AppDatabase
import com.loconav.locodriver.db.sharedPF.SharedPreferenceUtil
import com.loconav.locodriver.splash.SplashActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


object AppUtils : KoinComponent {

    val isKitKatOrHigher: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    val versionCode: Int
        get() = BuildConfig.VERSION_CODE


    val appContext : Context by inject()

    val sharedPreferenceUtil : SharedPreferenceUtil by inject()

    val db  : AppDatabase by inject()


    val macAddr: String
        get() {
            try {
                val all = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.name.equals("wlan0", ignoreCase = true)) continue

                    val macBytes = nif.hardwareAddress ?: return ""

                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02X:", b))
                    }

                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            } catch (ex: Exception) {}

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

    fun getVersionCode() : String {
        return BuildConfig.VERSION_CODE.toString()
    }


    fun clearAppData() {
        db.clearAllTables()
        sharedPreferenceUtil.deleteAllData()
    }


    fun logout() {
        GlobalScope.launch(IO){
            clearAppData()
            launch(Dispatchers.Main){
                relaunchApp()
            }
        }

    }


    fun relaunchApp(){
        val i = Intent(appContext, SplashActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        appContext.startActivity(i)
    }

}