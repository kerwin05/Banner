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

    fun setPass() {
        val psw = (1000 + Math.random() * (9999 - 1000 + 1)).toInt()
        val mPsw = "0000$psw"

        try {
            val wifiManager: WifiManager = App.getContext()
                .getSystemService(Context.WIFI_SERVICE) as WifiManager// 获取WifiManager实例

            val wifiConfigurationClass = WifiConfiguration::class.java
            val setWifiApConfigurationMethod = WifiManager::class.java.getDeclaredMethod(
                "setWifiApConfiguration",
                wifiConfigurationClass
            )
            val refreshSoftApPasswordMethod = WifiManager::class.java.getDeclaredMethod(
                "refreshSoftApPassword",
                wifiConfigurationClass
            )

            val mId = getIdAndPass().first.replace("ID:","")
            // 创建WifiConfiguration对象（根据您的需求进行设置）
            val wifiConfiguration = WifiConfiguration()
            // 设置WifiConfiguration的相关属性
            wifiConfiguration.SSID = mId
            wifiConfiguration.preSharedKey = mPsw
            // 调用setWifiApConfiguration方法
            setWifiApConfigurationMethod.invoke(wifiManager, wifiConfiguration)
            // 调用refreshSoftApPassword方法
            refreshSoftApPasswordMethod.invoke(wifiManager, wifiConfiguration)
        } catch (e: Exception) {

        }

    }

}