package com.example.banner.model

data class BannerModel(
    var img: Int,
    var id: String = "MTS-100SY-5098",
    var pass: String = "PASS:00001111(04:14)",
    var wifi: String = "192.168.43.1",
    var lan: String = "192.168.3.61/N/A"
) {

    fun showId() :String = "ID:$id"
    fun showPass(): String = "PASS:$pass"
    fun showWifi(): String = "Wifi Ip:$wifi"
    fun showLan(): String = "Lan Ip:$lan"

}
