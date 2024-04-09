package com.example.banner

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.example.banner.utils.receiver.NetworkChangeReceiver
import com.example.banner.utils.timberInit

class App: Application() {

    companion object {
        lateinit var mContext: App
        fun getContext(): App {
            return mContext
        }
    }

    private val networkChangeReceiver = NetworkChangeReceiver()

    override fun onCreate() {
        super.onCreate()
        mContext = this

        timberInit()

        //网络环境监听
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)

    }

}