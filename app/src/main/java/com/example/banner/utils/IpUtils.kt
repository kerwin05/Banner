package com.example.banner.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import com.example.banner.App
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Collections

object IpUtils {

    fun getWifiIpAddress(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return ""
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return ""
            if (!networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return ""
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return ""
            if (networkInfo.type != ConnectivityManager.TYPE_WIFI) {
                return ""
            }
        }

        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            if (networkInterfaces != null) {
                while (networkInterfaces.hasMoreElements()) {
                    val networkInterface = networkInterfaces.nextElement()
                    val addresses = networkInterface.inetAddresses
                    while (addresses.hasMoreElements()) {
                        val address = addresses.nextElement()
                        if (!address.isLoopbackAddress && address is InetAddress && address.hostAddress.indexOf(':') < 0) {
                            return address.hostAddress
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("IP Address", "Failed to get Wi-Fi IP address", e)
        }

        return ""
    }

    fun getLanIpAddress(): String {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            if (networkInterfaces != null) {
                while (networkInterfaces.hasMoreElements()) {
                    val networkInterface = networkInterfaces.nextElement()
                    val addresses = networkInterface.inetAddresses
                    while (addresses.hasMoreElements()) {
                        val address = addresses.nextElement()
                        if (!address.isLoopbackAddress && address is InetAddress && address.hostAddress.indexOf(':') < 0) {
                            return address.hostAddress
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("IP Address", "Failed to get LAN IP address", e)
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