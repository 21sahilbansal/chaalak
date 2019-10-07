package com.loconav.locodriver.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log
import android.util.Pair
import com.loconav.locodriver.application.LocoDriverApplication

class NetworkUtil {



    fun getActiveNetInfo(): NetworkInfo {
        return getnetInfoConn().first
    }


    fun getnetInfoConn(): Pair<NetworkInfo, Boolean> {
        try {
            val cm = LocoDriverApplication.instance.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var netInfo = cm.activeNetworkInfo

            if (netInfo != null && netInfo.isConnected) {
                Log.d(
                    "getnetInfoConn",
                    "Trying to connect using getActiveNetworkInfo"
                )
                return Pair(netInfo, true)
            }

            netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            if (netInfo != null && netInfo.isConnected) {
                Log.d(
                    "getnetinfoonn",
                    "Trying to connect using TYPE_MOBILE NetworkInfo"
                )
                return Pair(netInfo, true)
            } else {
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                if (netInfo != null && netInfo.isConnected) {
                    Log.d(
                        "getnetInfoConn",
                        "Trying to connect using TYPE_WIFI NetworkInfo"
                    )
                    return Pair(netInfo, true)
                }
            }
        } catch (e: Exception) {
            Log.e(
                "getnetInfoConn",
                "Got expection while trying to get NetworkInfo from ConnectivityManager", e
            )
            return Pair<NetworkInfo, Boolean>(null, true)
        }
        return Pair<NetworkInfo, Boolean>(null, false)
    }


    fun getNetworkType(info: NetworkInfo?): Short {
        var info = info
        val NETWORK_TYPE_GSM = 16
        var networkType = -1

        // Contains all the information about current connection
        if (null == info) {
            info = getActiveNetInfo()
        }

        if (info != null) {
            if (!info.isConnected) return -1
            // If device is connected via WiFi
            if (info.type == ConnectivityManager.TYPE_WIFI) {
                return 1 // return 1024 * 1024;
            } else {
                networkType = info.subtype
            }
        } else {
            return -1
        }


        // There are following types of mobile networks
        return when (networkType) {
            TelephonyManager.NETWORK_TYPE_LTE // ~ 10+ Mbps // API level 11
                -> 4
            TelephonyManager.NETWORK_TYPE_EVDO_0 // ~ 400-1000 kbps
                , TelephonyManager.NETWORK_TYPE_EVDO_A // ~ 600-1400 kbps
                , TelephonyManager.NETWORK_TYPE_HSDPA // ~ 2-14 Mbps
                , TelephonyManager.NETWORK_TYPE_HSPA // ~ 700-1700 kbps
                , TelephonyManager.NETWORK_TYPE_UMTS // ~ 400-7000 kbps
                , TelephonyManager.NETWORK_TYPE_EHRPD // ~ 1-2 Mbps // API level 11
                , TelephonyManager.NETWORK_TYPE_HSPAP // ~ 10-20 Mbps // API level 13
                , TelephonyManager.NETWORK_TYPE_EVDO_B // ~ 5 Mbps // API level 9
                , TelephonyManager.NETWORK_TYPE_HSUPA // ~ 1-23 Mbps
                -> 3
            TelephonyManager.NETWORK_TYPE_1xRTT // ~ 50-100 kbps
                , TelephonyManager.NETWORK_TYPE_CDMA // ~ 14-64 kbps
                , TelephonyManager.NETWORK_TYPE_EDGE // ~ 50-100 kbps
                , TelephonyManager.NETWORK_TYPE_GPRS // ~ 100 kbps
                , TelephonyManager.NETWORK_TYPE_IDEN // ~25 kbps // API level 8
                , NETWORK_TYPE_GSM
                -> 2
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> return 0
            else
                -> 0
        }
    }



}