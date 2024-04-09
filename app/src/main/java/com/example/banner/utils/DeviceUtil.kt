package com.example.banner.utils

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager

object DeviceUtil {

    fun getCurrentWifiInfo(context: Context): Pair<String?, String?> {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        val ssid = wifiInfo.ssid.replace("\"", "")
        val preSharedKey = ""
        return Pair(ssid, preSharedKey)
    }

    fun getHotspotConfiguration(context: Context): WifiConfiguration? {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val configuredNetworks: List<WifiConfiguration> = wifiManager.configuredNetworks
        for (config in configuredNetworks) {
            if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK) ||
                config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA2_PSK)
            ) {
                return config
            }
        }
        return null
    }

}