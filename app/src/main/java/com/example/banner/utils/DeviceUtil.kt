package com.example.banner.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.example.banner.App

object DeviceUtil {

    fun getIdAndPass(onBlock: (id: String, pass: String) -> Unit) {
        val mCm = App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifiManager = App.getContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wc: WifiConfiguration = mWifiManager.callerConfiguredNetworks.first()
        "DeviceUtil wc.SSID:${wc.SSID} wc.preSharedKey:${wc.preSharedKey}".log()
        onBlock(wc.SSID, wc.preSharedKey)
    }

}