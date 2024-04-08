package com.example.banner.model

import com.blankj.utilcode.util.NetworkUtils
import com.example.banner.utils.IpUtils

data class BannerModel(
    var img: Int,
    var id: String = "MTS-100SY-5098",
    var pass: String = "00001111(04:14)",
    var wifi: String = NetworkUtils.getIpAddressByWifi(),
    var lan: String = IpUtils.getLanIp()
) {

    fun showId() :String = "ID:$id"
    fun showPass(): String = "PASS:$pass"
    fun showWifi(): String = "Wifi Ip:$wifi"
    fun showLan(): String = "Lan Ip:$lan"

}
