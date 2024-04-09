package com.example.banner.utils

import android.content.Context
import android.net.wifi.WifiManager

object DeviceUtil {

    fun getCurrentWifiInfo(context: Context): Pair<String?, String?> {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        val ssid = wifiInfo.ssid.replace("\"", "")
        val preSharedKey = ""
        return Pair(ssid, preSharedKey)
    }

}