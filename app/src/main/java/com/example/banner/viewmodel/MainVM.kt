package com.example.banner.viewmodel

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.example.banner.App
import com.example.banner.R
import com.example.banner.adapter.BannerAdapter
import com.example.banner.model.BannerModel
import com.example.banner.utils.IpUtils
import com.example.banner.utils.eventbus.NetworkEvent
import com.example.banner.utils.eventbus.WifiEvent
import com.example.banner.utils.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainVM : ViewModel()  {

    val mId = MutableLiveData("")
    val mPass = MutableLiveData("")
    val mWifi = MutableLiveData("")
    val mLan = MutableLiveData("")
    val mTime = MutableLiveData("")

    fun showId() :String = "ID:${mId.value}"
    fun showPass(): String = "PASS:${mPass.value}"
    fun showWifi(): String = "Wifi Ip:${mWifi.value}"
    fun showLan(): String = "Lan Ip:${mLan.value}"

    lateinit var mAdapter: BannerAdapter
    val mBanners = ArrayList<BannerModel>()

    init {
        EventBus.getDefault().register(this)
        initData()
        showTime()
    }

    private fun initShowData() {
        NetworkUtils.getIPAddressAsync(true) {
            //刷新Banner数据的Ip
            mWifi.value = IpUtils.getLocalIpAddress()
            mLan.value = it
        }
        try {

            val wifiManager: WifiManager = App.getContext().getSystemService(Context.WIFI_SERVICE) as WifiManager// 获取WifiManager实例
            val getWifiApConfigurationMethod = wifiManager.javaClass.getMethod("getWifiApConfiguration")
            val wifiApConfiguration: WifiConfiguration = getWifiApConfigurationMethod.invoke(wifiManager) as WifiConfiguration
            mId.value = wifiApConfiguration.SSID
            mPass.value = wifiApConfiguration.preSharedKey

            "wifiApConfiguration id:${mId.value}, pass:${mPass.value}".log()
        } catch (e: Exception) {
            "wifiApConfiguration error".log()
        }
    }

    //初始化数据，后续可以将图片更改为从文件拿
    private fun initData() {
        mBanners.add(
            BannerModel(
                R.drawable.image1
            )
        )
        mBanners.add(
            BannerModel(
                R.drawable.image2
            )
        )
        mBanners.add(
            BannerModel(
                R.drawable.image3
            )
        )
        mBanners.add(
            BannerModel(
                R.drawable.image4
            )
        )
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
        super.onCleared()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun rcvMsg(event: NetworkEvent) {
        //EvenBus收到网络改变的通知，刷新Banner显示的Ip
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun rcvMsg(event: WifiEvent) {
        //EvenBus收到网络改变的通知，刷新Banner显示的Wifi
        NetworkUtils.getIPAddressAsync(true) {
            //刷新Banner数据的Ip
            mWifi.value = IpUtils.getLocalIpAddress()
            mLan.value = it
        }
    }

    private fun showTime() {
        viewModelScope.launch {
            while (true) {
                mTime.value = getCurTime()
                delay(1000)
            }
        }
    }

    private fun getCurTime(): String {
        val currentTime = Date()
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(currentTime)
    }

}