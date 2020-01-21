package com.loconav.locodriver.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log
import android.util.Pair
import com.loconav.locodriver.application.LocoDriverApplication

object NetworkUtil {

    val isUserOnline: Boolean
        get() = netInfoFromConnectivityManager.second

    /**
     * Fetches the network connection using connectivity manager
     *
     * @return  * -1 in case of no network  * 0 in case of unknown network  * 1 in case
     * of wifi  * 2 in case of 2g  * 3 in case of 3g  * 4 in case of
     * 4g
     */
    val networkType: Short
        get() = getNetworkType(null)

    /**
     * Returns active network info
     */
    val activeNetInfo: NetworkInfo
        get() = netInfoFromConnectivityManager.first

    /**
     * Now we might say network is there even if we don't have a NetworkInfo object that is why we
     * returning NetworkInfo and NeworkAvailable states seprately. this is basically
     * done to tackle some exception scenarios where getActiveNetworkInfo unexpectedly throws an
     * error.
     *
     * @return Pair<NetworkInfo></NetworkInfo>, Boolean>.first ==> NeworkInfo object of current available network ;
     * Pair<NetworkInfo></NetworkInfo>, Boolean>.second ==> boolean indicating wheather network is
     * available or not
     */
    val netInfoFromConnectivityManager: Pair<NetworkInfo, Boolean>
        get() {
            try {
                val cm = LocoDriverApplication.instance
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var netInfo = cm.activeNetworkInfo

                if (netInfo != null && netInfo.isConnected) {
                    Log.d(
                        "getNetInfoFromConn",
                        "Trying to connect using getActiveNetworkInfo"
                    )
                    return Pair(netInfo, true)
                }

                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

                if (netInfo != null && netInfo.isConnected) {
                    Log.d(
                        "getNetInfoFromConn",
                        "Trying to connect using TYPE_MOBILE NetworkInfo"
                    )
                    return Pair(netInfo, true)
                } else {
                    netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    if (netInfo != null && netInfo.isConnected) {
                        Log.d(
                            "getNetInfoFromConn",
                            "Trying to connect using TYPE_WIFI NetworkInfo"
                        )
                        return Pair(netInfo, true)
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "getNetInfoFromConn",
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
            info = activeNetInfo
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
        when (networkType) {
            TelephonyManager.NETWORK_TYPE_LTE // ~ 10+ Mbps // API level 11
            -> return NetworkType._4G.enumValue
            TelephonyManager.NETWORK_TYPE_EVDO_0 // ~ 400-1000 kbps
                , TelephonyManager.NETWORK_TYPE_EVDO_A // ~ 600-1400 kbps
                , TelephonyManager.NETWORK_TYPE_HSDPA // ~ 2-14 Mbps
                , TelephonyManager.NETWORK_TYPE_HSPA // ~ 700-1700 kbps
                , TelephonyManager.NETWORK_TYPE_UMTS // ~ 400-7000 kbps
                , TelephonyManager.NETWORK_TYPE_EHRPD // ~ 1-2 Mbps // API level 11
                , TelephonyManager.NETWORK_TYPE_HSPAP // ~ 10-20 Mbps // API level 13
                , TelephonyManager.NETWORK_TYPE_EVDO_B // ~ 5 Mbps // API level 9
                , TelephonyManager.NETWORK_TYPE_HSUPA // ~ 1-23 Mbps
            -> return NetworkType._3G.enumValue
            TelephonyManager.NETWORK_TYPE_1xRTT // ~ 50-100 kbps
                , TelephonyManager.NETWORK_TYPE_CDMA // ~ 14-64 kbps
                , TelephonyManager.NETWORK_TYPE_EDGE // ~ 50-100 kbps
                , TelephonyManager.NETWORK_TYPE_GPRS // ~ 100 kbps
                , TelephonyManager.NETWORK_TYPE_IDEN // ~25 kbps // API level 8
                , NETWORK_TYPE_GSM -> return NetworkType._2G.enumValue
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> return NetworkType._UNKNOWN.enumValue
            else -> return NetworkType._UNKNOWN.enumValue
        }
    }


    enum class NetworkType(val enumValue:Short) {
        _4G(4),
        _3G(3),
        _2G(2),
        _UNKNOWN(0)
    }

}
