package com.example.banner.utils

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.example.banner.App
import java.net.NetworkInterface

object IpUtils {

    fun getLocalIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                if (networkInterface.isUp && !networkInterface.isLoopback) {
                    val addresses = networkInterface.inetAddresses
                    while (addresses.hasMoreElements()) {
                        val address = addresses.nextElement()
                        if (!address.isLoopbackAddress && address.hostAddress.indexOf(':') < 0) {
                            return address.hostAddress
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getIdAndPass(): Pair<String, String> {
        try {
            val wifiManager: WifiManager = App.getContext()
                .getSystemService(Context.WIFI_SERVICE) as WifiManager// 获取WifiManager实例
            val getWifiApConfigurationMethod =
                wifiManager.javaClass.getMethod("getWifiApConfiguration")
            val wifiApConfiguration: WifiConfiguration =
                getWifiApConfigurationMethod.invoke(wifiManager) as WifiConfiguration
            return Pair(
                "ID:${wifiApConfiguration.SSID}",
                "PASS:${wifiApConfiguration.preSharedKey}"
            )
        } catch (e: Exception) {
        }
        return Pair("", "")
    }

}