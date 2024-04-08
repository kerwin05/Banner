package com.example.banner.model

import com.blankj.utilcode.util.NetworkUtils
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

    fun refreshIp() {
        wifi = IpUtils.getLocalIpAddress()
        NetworkUtils.getIPAddressAsync(true) {
            lan = it
        }
    }

}
