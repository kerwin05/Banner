package com.example.banner.activity

import android.os.Bundle
import com.example.banner.R
import com.example.banner.adapter.BannerAdapter
import com.example.banner.databinding.ActivityMainBinding
import com.example.banner.viewmodel.MainVM
import com.zhpan.bannerview.transform.toPx

class MainActivity : BaseDataBindingActivity<ActivityMainBinding>() {

    override fun dataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main)
    }

    private lateinit var viewModel: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainVM()
        viewModel.mAdapter = BannerAdapter(this)
        getBinding().vm = viewModel
    }

    override fun onResume() {
        super.onResume()
        initBanner()
    }

    private fun initBanner() {
        getBinding().banner.apply {
            setCanLoop(true)
            setAutoPlay(true)
            setAdapter(viewModel.mAdapter)
            setIndicatorSliderRadius(5.toPx())
            setIndicatorMargin(0, 0, 0, 20.toPx())
            setIndicatorSliderColor(
                resources.getColor(R.color.purple_200, null),
                resources.getColor(R.color.black, null)
            )
        }.create(viewModel.mBanners)
    }

}