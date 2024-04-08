package com.example.banner.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import com.example.banner.App
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Collections

object IpUtils {

    fun getWifiIpAddress(): String {
        val wifiManager = App.getContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress

        // 将 IP 地址转换为字符串形式
        val ipString = String.format(
            "%d.%d.%d.%d",
            ipAddress and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 24 and 0xff
        )

        return ipString
    }

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

    fun getLanIp(): String {
        val manager = App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni: NetworkInfo =
            manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) ?: return ""
        if (ni.isConnected) {
            try {
                val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                if (networkInterfaces != null) {
                    for (nif in Collections.list(networkInterfaces)) {
                        if (!nif.name.startsWith("eth")) continue
                        for (inetAddress in Collections.list(nif.inetAddresses)) {
                            if (inetAddress.isLoopbackAddress) continue
                            if (inetAddress is Inet4Address) {
                                return inetAddress.hostAddress
                            }
                        }
                    }
                } else {
                    // 处理获取网络接口失败的情况
                }
            } catch (e: SocketException) {
                e.printStackTrace()
            }
        }
        return ""
    }

}