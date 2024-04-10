package com.example.banner.activity

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.blankj.utilcode.util.NetworkUtils
import com.example.banner.App
import com.example.banner.R
import com.example.banner.adapter.BannerAdapter
import com.example.banner.databinding.ActivityMainBinding
import com.example.banner.model.BannerModel
import com.example.banner.utils.eventbus.NetworkEvent
import com.example.banner.utils.eventbus.WifiEvent
import com.example.banner.utils.log
import com.zhpan.bannerview.transform.toPx
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: BannerAdapter

    private val mBanners = ArrayList<BannerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)

        initData()

    }

    override fun onResume() {
        super.onResume()
        initBanner()

        try {
            val wifiManager: WifiManager = App.getContext().getSystemService(Context.WIFI_SERVICE) as WifiManager// 获取WifiManager实例
            val getWifiApConfigurationMethod = wifiManager.javaClass.getMethod("getWifiApConfiguration")
            val wifiApConfiguration: WifiConfiguration = getWifiApConfigurationMethod.invoke(wifiManager) as WifiConfiguration
            val id = wifiApConfiguration.SSID
            val pass = wifiApConfiguration.preSharedKey
            "wifiApConfiguration id:$id, pass:$pass".log()
        } catch (e: Exception) {
            "wifiApConfiguration error".log()
        }

    }

    private fun initBanner() {
        mAdapter = BannerAdapter(this)
        binding.banner.apply {
            setCanLoop(true)
            setAutoPlay(true)
            setAdapter(mAdapter)
            setIndicatorSliderRadius(5.toPx())
            setIndicatorMargin(0, 0, 0, 20.toPx())
            setIndicatorSliderColor(
                resources.getColor(R.color.purple_200, null),
                resources.getColor(R.color.black, null)
            )
        }.create(mBanners)

        binding.tv.isVisible = false
//        binding.tv.isVisible = true
        binding.tv.setOnClickListener {

        }

    }

    //初始化数据，后续可以将图片更改为从文件拿
    private fun initData() {
        mBanners.add(
            BannerModel(
                R.drawable.image1
            ).apply { refreshIp() }
        )
        mBanners.add(
            BannerModel(
                R.drawable.image2
            ).apply { refreshIp() }
        )
        mBanners.add(
            BannerModel(
                R.drawable.image3
            ).apply { refreshIp() }
        )
        mBanners.add(
            BannerModel(
                R.drawable.image4
            ).apply { refreshIp() }
        )
        refreshBannerIp()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun rcvMsg(event: NetworkEvent) {
        //EvenBus收到网络改变的通知，刷新Banner显示的Ip
        refreshBannerIp()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun rcvMsg(event: WifiEvent) {
        //EvenBus收到网络改变的通知，刷新Banner显示的Wifi
        NetworkUtils.getIPAddressAsync(true) {
            //刷新Banner数据的Ip
            mBanners.forEach { model ->
                model.refreshWifi(event.id, event.pass)
            }
            refreshBanner()
        }
    }

    private fun refreshBannerIp() {
        NetworkUtils.getIPAddressAsync(true) {
            //刷新Banner数据的Ip
            mBanners.forEach { model ->
                model.refreshIp(it)
            }
            refreshBanner()
        }
    }

    private fun refreshBanner() {
        //数据更新banner
        val cur = binding.banner.currentItem
        binding.banner.refreshData(mBanners)
        binding.banner.setCurrentItem(cur, false)
    }

}