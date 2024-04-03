package com.example.banner.adapter

import android.content.Context
import android.view.View
import com.example.banner.R
import com.example.banner.databinding.ItemBannerBinding
import com.example.banner.model.BannerModel
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BannerAdapter(private val context: Context) :
    BaseBannerAdapter<BannerModel, BannerAdapter.BannerViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_banner
    }

    override fun createViewHolder(itemView: View, viewType: Int): BannerViewHolder {
        return BannerViewHolder(context, itemView)
    }

    override fun onBind(
        holder: BannerViewHolder,
        data: BannerModel,
        position: Int,
        pageSize: Int
    ) {
        holder.bindData(data, position, pageSize)
    }

    inner class BannerViewHolder(val mContext: Context, itemView: View) :
        BaseViewHolder<BannerModel>(itemView) {

        val bind: ItemBannerBinding =
            ItemBannerBinding.bind(itemView.findViewById(R.id.root))

        override fun bindData(
            data: BannerModel,
            position: Int,
            pageSize: Int
        ) {
            bind.tvTime.text = getCurTime()
            bind.ivBg.setImageResource(data.img)
            bind.tvId.text = data.showId()
            bind.tvPass.text = data.showPass()
            bind.tvWifi.text = data.showWifi()
            bind.tvLan.text = data.showLan()
        }
    }

    fun getCurTime(): String {
        val currentTime = Date()
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(currentTime)
    }

}