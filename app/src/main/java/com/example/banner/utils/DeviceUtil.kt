package com.example.banner.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.banner.App

object DeviceUtil {

    fun getIdAndPass(onBlock: (id: String, pass: String) -> Unit) {

        val wifiManager = App.getContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        if (checkSelfPermission(
                App.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val configuredNetworks = wifiManager.configuredNetworks
            var currentConfig: WifiConfiguration? = null

            for (config in configuredNetworks) {
                if (config.networkId == wifiInfo.networkId) {
                    currentConfig = config
                    break
                }
            }

            if (currentConfig != null) {
                var ssid = currentConfig.SSID  // SSID
                var preSharedKey = currentConfig.preSharedKey  // preSharedKey

                // 去除引号
                ssid = ssid.removeSurrounding("\"")
                preSharedKey = preSharedKey.removeSurrounding("\"")

                "DeviceUtil wc.SSID:${ssid} wc.preSharedKey:${preSharedKey}".log()

                onBlock(ssid, preSharedKey)
            }
        } else {
            "DeviceUtil 无权限".log()
        }

    }

}