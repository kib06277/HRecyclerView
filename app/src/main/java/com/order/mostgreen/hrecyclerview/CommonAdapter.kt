package com.order.mostgreen.hrecyclerview

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.view.*
import java.util.ArrayList

abstract class CommonAdapter<T>(context: Context?, dataList: List<T>?, layoutId: Int) :
    RecyclerView.Adapter<CommonViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mDataList: List<T>?
    private val mLayoutId: Int
    private var mFixX = 0
    val moveViewList = ArrayList<View?>()
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val itemView = mLayoutInflater.inflate(mLayoutId, parent, false)
        val holder = CommonViewHolder(itemView)
        //获取可滑动的view布局
        val moveLayout = holder.getView<LinearLayout>(R.id.id_move_layout)
        //建议: 根据RecyclerView的复用机制，在重新刷新或者加载更多的时候可能会出现新创建的item的右边可滑动的布局没有滚动到对应的位置，所以建议此处对moveLayout进行初始化滚动到HRecyclerView中保存的相对滑动位置mFixX
        moveLayout!!.scrollTo(mFixX, 0) //新增mFixX属性并设置初始化到滑动的相对位置
        moveViewList.add(moveLayout)
        return holder
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        bindData(holder, mDataList!![position])
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    abstract fun bindData(holder: CommonViewHolder?, data: T)
    fun setFixX(fixX: Int) {
        mFixX = fixX
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mDataList = dataList
        mLayoutId = layoutId
    }
}