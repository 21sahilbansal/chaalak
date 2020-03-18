package com.loconav.locodriver.util

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.telephony.TelephonyManager
import com.loconav.locodriver.Constants
import java.util.regex.Pattern

class PhoneUtil {

    companion object {

        /**
         * it fetches battery of android device.
         */
        fun getBatteryPercentage(context: Context): Int {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, iFilter)

            val level = batteryStatus?.getIntExtra(
                BatteryManager.EXTRA_LEVEL,
                -1
            ) ?: -1
            val scale = if (batteryStatus != null) batteryStatus.getIntExtra(
                BatteryManager.EXTRA_SCALE,
                -1
            ) else -1
            val batteryPct = level / scale.toFloat()
            return (batteryPct * 100).toInt()
        }


        /**
         * fetches sim carrier info.
         */
        fun getServiceProviderName(context: Context): String{
            val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return manager.networkOperatorName
        }


        /**
         * it detects network type.
         */
        fun getNetworkClass(context: Context): String {
            val mTelephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            when (mTelephonyManager.networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return "2G"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return "3G"
                TelephonyManager.NETWORK_TYPE_LTE -> return "4G"
                TelephonyManager.NETWORK_TYPE_NR -> return "5G"
                else -> return "Unknown"
            }
        }

        fun isPhoneNumberValid(inputString:String):Boolean {
            val pattern = Pattern.compile(Constants.RegexConstants.VALID_PHONE_NUMBER_REGEX)
            val matcher = pattern.matcher(inputString)
            return matcher.matches()
        }
    }
}