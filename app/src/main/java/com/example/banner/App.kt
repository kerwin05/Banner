package com.example.banner

import android.app.Application

class App: Application() {

    companion object {
        lateinit var mContext: App
        fun getContext(): App {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

}