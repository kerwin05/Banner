package com.example.banner.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.banner.R
import com.example.banner.adapter.BannerAdapter
import com.example.banner.databinding.ActivityMainBinding
import com.example.banner.model.BannerModel
import com.zhpan.bannerview.transform.toPx

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: BannerAdapter

    private val mBanners = ArrayList<BannerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

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

}