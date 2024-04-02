package com.example.banner.adapter

import android.content.Context
import android.view.View
import com.example.banner.R
import com.example.banner.databinding.ItemBannerBinding
import com.example.banner.model.BannerModel
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

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
            bind.ivBg.setImageResource(data.img)
        }
    }

}