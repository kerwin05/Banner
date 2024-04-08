package com.example.banner.utils

import android.util.Log
import com.example.banner.BuildConfig
import timber.log.Timber

inline fun <T : Any> T.log(format: ((T) -> String) = Any::toString):T {
    Timber.d(format(this))
    return this
}

fun timberInit(){
    when(BuildConfig.BUILD_TYPE){
        "debug" -> Timber.plant(Timber.DebugTree())
        "release" -> Timber.plant(object:Timber.Tree(){
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                if(BuildConfig.DEBUG||priority == Log.VERBOSE||priority== Log.DEBUG)return
                super.log(priority, tag, message, t)
            }
        })
    }
}