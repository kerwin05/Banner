package com.example.banner.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.blankj.utilcode.util.NetworkUtils
import com.example.banner.R
import com.example.banner.adapter.BannerAdapter
import com.example.banner.databinding.ActivityMainBinding
import com.example.banner.model.BannerModel
import com.example.banner.utils.eventbus.NetworkEvent
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
        refreshBannerIp()
    }

    private fun refreshBannerIp() {
        NetworkUtils.getIPAddressAsync(true) {
            mBanners.forEach {  model ->
                model.refreshIp(it)
            }
            val cur = binding.banner.currentItem
            binding.banner.refreshData(mBanners)
            binding.banner.setCurrentItem(cur, false)
        }
    }

}