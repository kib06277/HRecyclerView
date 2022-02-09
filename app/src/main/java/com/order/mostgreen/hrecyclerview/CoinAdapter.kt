package com.order.mostgreen.hrecyclerview

import android.content.Context
import com.order.mostgreen.hrecyclerview.CommonViewHolder.onItemCommonClickListener
class CoinAdapter(context: Context?, dataList: List<CoinInfo>?, layoutId: Int, val commonClickListener: onItemCommonClickListener) : CommonAdapter<CoinInfo?>(context, dataList, layoutId) {
    override fun bindData(holder: CommonViewHolder?, data: CoinInfo?) {
        if (data != null) {
            holder!!.setText(R.id.id_name, data.name)
                .setText(R.id.id_tv_price_last, data.priceLast)
                .setText(R.id.id_tv_rise_rate24, data.riseRate24)
                .setText(R.id.id_tv_vol24, data.vol24)
                .setText(R.id.id_tv_close, data.close)
                .setText(R.id.id_tv_open, data.open)
                .setText(R.id.id_tv_bid, data.bid)
                .setText(R.id.id_tv_ask, data.ask)
                .setText(R.id.id_tv_percent, data.amountPercent)
                .setCommonClickListener(commonClickListener)
        }
    }
}