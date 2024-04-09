package com.example.banner.utils

import android.Manifest
import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.util.Log
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.banner.App
import java.lang.reflect.Field
import java.lang.reflect.Method

object DeviceUtil {

    fun getIdAndPass(onBlock: (id: String, pass: String) -> Unit) {

        PermissionUtils.permission(*arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)).callback(object : PermissionUtils.FullCallback {
            override fun onGranted(granted: MutableList<String>) {
                val wifiManager = App.getContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                val configuredNetworks = wifiManager.configuredNetworks
                var currentConfig: WifiConfiguration? = null

                for (config in configuredNetworks) {
                    if (config.networkId == wifiInfo.networkId) {
                        currentConfig = config
                        break
                    }
                }

                if (currentConfig != null) {
                    var ssid = currentConfig.SSID  // SSID
                    var preSharedKey = currentConfig.preSharedKey  // preSharedKey

                    // 去除引号
                    ssid = ssid.removeSurrounding("\"")
                    preSharedKey = preSharedKey.removeSurrounding("\"")

                    "DeviceUtil wc.SSID:${ssid} wc.preSharedKey:${preSharedKey}".log()

                    onBlock(ssid, preSharedKey)
                }
            }

            override fun onDenied(deniedForever: MutableList<String>, denied: MutableList<String>) {
                "DeviceUtil 无权限".log()
                ToastUtils.showShort("DeviceUtil 无权限")
                onBlock("", "")
            }

        })

    }

    private const val TAG = "DeviceUtil"

    fun getApSSIDAndPwd(context: Context, onBlock: (id: String, pass: String) -> Unit) {
        val mWifiConfig = getWifiConfiguration(context)
        var ssid: String? = null
        var pwd: String? = null

        if (mWifiConfig != null) {
            val fields: Array<Field> = mWifiConfig.javaClass.declaredFields
            for (field in fields) {
                try {
                    if (field.name == "SSID") {
                        ssid = field.get(mWifiConfig).toString()
                        Log.e(TAG, "AP SSID = $ssid")
                    } else if (field.name == "preSharedKey") {
                        pwd = field.get(mWifiConfig).toString()
                        Log.e(TAG, "AP pwd = $pwd")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "getApSSIDAndPwd()-->error:$e")
                }
            }
        }

        if (ssid == null) {
            ssid = "Unknown"
        }
        if (pwd == null) {
            pwd = "Unknown"
        }

        onBlock(ssid, pwd)
    }

    private fun getWifiConfiguration(context: Context): WifiConfiguration? {
        var mWifiConfig: WifiConfiguration? = null
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val method: Method = wifiManager.javaClass.getMethod("getWifiApConfiguration")
            method.isAccessible = true
            mWifiConfig = method.invoke(wifiManager) as WifiConfiguration
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mWifiConfig
    }

    fun getCurrentWifiInfo(context: Context): Pair<String?, String?> {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        val ssid = wifiInfo.ssid.replace("\"", "")
        val preSharedKey = ""
        return Pair(ssid, preSharedKey)
    }

}