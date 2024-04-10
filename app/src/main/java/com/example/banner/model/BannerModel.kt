package com.example.banner.model

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.example.banner.App
import com.example.banner.utils.IpUtils

data class BannerModel(
    var img: Int,
    var id: String = "-------------",
    var pass: String = "-----------",
    var wifi: String = "",
    var lan: String = ""
) {

    fun showId() :String = "ID:$id"
    fun showPass(): String = "PASS:$pass"
    fun showWifi(): String = "Wifi Ip:$wifi"
    fun showLan(): String = "Lan Ip:$lan"

    fun refreshIp(lan: String = "") {
        wifi = IpUtils.getLocalIpAddress()
        this.lan = lan

        try {
            val wifiManager: WifiManager = App.getContext().getSystemService(Context.WIFI_SERVICE) as WifiManager// 获取WifiManager实例
            val getWifiApConfigurationMethod = wifiManager.javaClass.getMethod("getWifiApConfiguration")
            val wifiApConfiguration: WifiConfiguration = getWifiApConfigurationMethod.invoke(wifiManager) as WifiConfiguration
            id = wifiApConfiguration.SSID
            pass = wifiApConfiguration.preSharedKey
        } catch (e: Exception) {

        }

    }

    fun refreshWifi(id: String, pass: String) {
//        this.id = id
//        this.pass = pass
    }

}
