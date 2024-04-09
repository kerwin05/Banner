package com.example.banner.model

import com.blankj.utilcode.util.NetworkUtils
import com.example.banner.App
import com.example.banner.utils.DeviceUtil
import com.example.banner.utils.IpUtils

data class BannerModel(
    var img: Int,
    var id: String = NetworkUtils.getSSID(),
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
    }

    fun refreshWifi() {
        id = NetworkUtils.getSSID()
        pass = DeviceUtil.getCurrentWifiInfo(App.getContext()).first.toString()
    }

}
