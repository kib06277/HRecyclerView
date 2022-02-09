package com.order.mostgreen.hrecyclerview

import androidx.recyclerview.widget.RecyclerView
import android.view.View.OnLongClickListener
import android.util.SparseArray
import android.view.*
import android.widget.*

class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnLongClickListener,
    View.OnClickListener {
    private val viewSparseArray: SparseArray<View?>
    private var commonClickListener: onItemCommonClickListener? = null

    /**
     * 根据 ID 来获取 View
     *
     * @param viewId viewID
     * @param <T>    泛型
     * @return 将结果强转为 View 或 View 的子类型
    </T> */
    fun <T : View?> getView(viewId: Int): T? {
        // 先从缓存中找，找打的话则直接返回
        // 如果找不到则 findViewById ，再把结果存入缓存中
        var view = viewSparseArray[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            viewSparseArray.put(viewId, view)
        }
        return view as T?
    }

    fun setText(viewId: Int, text: CharSequence?): CommonViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = text
        return this
    }

    fun setViewVisibility(viewId: Int, visibility: Int): CommonViewHolder {
        getView<View>(viewId)!!.visibility = visibility
        return this
    }

    fun setImageResource(viewId: Int, resourceId: Int): CommonViewHolder {
        val imageView = getView<ImageView>(viewId)!!
        imageView.setImageResource(resourceId)
        return this
    }

    interface onItemCommonClickListener {
        fun onItemClickListener(position: Int)
        fun onItemLongClickListener(position: Int)
    }

    fun setCommonClickListener(commonClickListener: onItemCommonClickListener?) {
        this.commonClickListener = commonClickListener
    }

    override fun onClick(v: View) {
        if (commonClickListener != null) {
            commonClickListener!!.onItemClickListener(adapterPosition)
        }
    }

    override fun onLongClick(v: View): Boolean {
        if (commonClickListener != null) {
            commonClickListener!!.onItemLongClickListener(adapterPosition)
        }
        return false
    }

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
        viewSparseArray = SparseArray()
    }
}