package com.example.banner.utils

import android.Manifest
import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.banner.App

object DeviceUtil {

    fun getIdAndPass(onBlock: (id: String, pass: String) -> Unit) {

        PermissionUtils.permission(*arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)).callback(object : PermissionUtils.FullCallback {
            override fun onGranted(granted: MutableList<String>) {
                val wifiManager = App.getContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
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
            }

            override fun onDenied(deniedForever: MutableList<String>, denied: MutableList<String>) {
                "DeviceUtil 无权限".log()
                ToastUtils.showShort("DeviceUtil 无权限")
            }

        })

    }

}