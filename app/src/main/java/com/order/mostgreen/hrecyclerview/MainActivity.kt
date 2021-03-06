package com.order.mostgreen.hrecyclerview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.order.mostgreen.hrecyclerview.CommonViewHolder.onItemCommonClickListener

class MainActivity : AppCompatActivity() {
    private var mDataModels: ArrayList<CoinInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val hRecyclerView = findViewById<View>(R.id.id_hrecyclerview) as HRecyclerView
        mDataModels = ArrayList()
        for (i in 0..9999) {
            val coinInfo = CoinInfo()
            coinInfo.name = "USDT"
            coinInfo.priceLast = "20.0"
            coinInfo.riseRate24 = "0.2"
            coinInfo.vol24 = "10020"
            coinInfo.close = "22.2"
            coinInfo.open = "40.0"
            coinInfo.bid = "33.2"
            coinInfo.ask = "19.0"
            coinInfo.amountPercent = "33.3%"
            mDataModels!!.add(coinInfo)
        }
        hRecyclerView.setHeaderListData(resources.getStringArray(R.array.right_title_name))

        val adapter = CoinAdapter(this, mDataModels, R.layout.item_layout, object : onItemCommonClickListener {
                override fun onItemClickListener(position: Int) {
                    Toast.makeText(this@MainActivity, "position--->$position", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onItemLongClickListener(position: Int) {}
            })
        hRecyclerView.setAdapter(adapter)
    }
}