package com.example.banner.activity

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.PermissionUtils
import com.example.banner.R
import com.example.banner.adapter.BannerAdapter
import com.example.banner.databinding.ActivityMainBinding
import com.example.banner.model.BannerModel
import com.example.banner.utils.DeviceUtil
import com.example.banner.utils.eventbus.NetworkEvent
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

        //权限
        // 权限
        PermissionUtils.permission(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).callback(object : PermissionUtils.FullCallback {
            override fun onGranted(permissionsGranted: List<String>) {
                // 权限已授予
                // 在这里处理定位权限已授权后的逻辑
                getDevices()
                refreshWifi()
            }

            override fun onDenied(
                permissionsDeniedForever: List<String>,
                permissionsDenied: List<String>
            ) {
                // 权限被拒绝
                // 在这里处理定位权限被拒绝后的逻辑
                getDevices()
                refreshWifi()
            }
        }).request()

    }

    private fun getDevices() {
        val getCurrentWifiInfo = DeviceUtil.getCurrentWifiInfo(this)
        "DeviceUtil getCurrentWifiInfo id:${getCurrentWifiInfo.first}, pass:${getCurrentWifiInfo.second}".log()
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

    private fun refreshBannerIp() {
        NetworkUtils.getIPAddressAsync(true) {
            //刷新Banner数据的Ip
            mBanners.forEach { model ->
                model.refreshIp(it)
            }
            refreshBanner()
        }
    }

    private fun refreshWifi() {
        //刷新Banner数据的Ip
        mBanners.forEach { model ->
            model.refreshWifi()
        }
        refreshBanner()
    }

    private fun refreshBanner() {
        //数据更新banner
        val cur = binding.banner.currentItem
        binding.banner.refreshData(mBanners)
        binding.banner.setCurrentItem(cur, false)
    }

}