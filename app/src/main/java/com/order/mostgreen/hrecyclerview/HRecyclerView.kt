package com.order.mostgreen.hrecyclerview

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.jvm.JvmOverloads
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.*
import java.util.ArrayList

class HRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(
    context, attrs, defStyleAttr) {
    //头部title布局
    private var mRightTitleLayout: LinearLayout? = null

    //手指按下时的位置
    private var mStartX = 0f

    //滑动时和按下时的差值
    private var mMoveOffsetX = 0

    //最大可滑动差值
    private var mFixX = 0

    //左边标题集合
    private lateinit var mLeftTextList: Array<String>

    //左边标题的宽度集合
    private lateinit var mLeftTextWidthList: IntArray

    //右边标题集合
    private var mRightTitleList = arrayOf<String>()

    //右边标题的宽度集合
    private var mRightTitleWidthList: IntArray? = null

    //展示数据时使用的RecycleView
    private var mRecyclerView: RecyclerView? = null

    //RecycleView的Adapter
    private var mAdapter: Any? = null

    //需要滑动的View集合
    private var mMoveViewList: ArrayList<View?>? = ArrayList<View?>()

    //右边可滑动的总宽度
    private var mRightTotalWidth = 0

    //右边单个view的宽度
    private val mRightItemWidth = 60

    //左边view的宽度
    private val mLeftViewWidth = 80

    //左边view的高度
    private val mLeftViewHeight = 40

    //触发拦截手势的最小值
    private val mTriggerMoveDis = 30
    private fun initView() {
        val linearLayout = LinearLayout(getContext())
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(createHeadLayout())
        linearLayout.addView(createMoveRecyclerView())
        addView(linearLayout, LayoutParams(LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT))
    }

    /**
     * 创建头部布局
     *
     * @return
     */
    private fun createHeadLayout(): View {
        val headLayout = LinearLayout(getContext())
        headLayout.gravity = Gravity.CENTER
        val leftLayout = LinearLayout(getContext())
        addListHeaderTextView(mLeftTextList[0], mLeftTextWidthList[0], leftLayout)
        leftLayout.gravity = Gravity.CENTER
        headLayout.addView(leftLayout, 0, ViewGroup.LayoutParams(dip2px(
            context, mLeftViewWidth.toFloat()), dip2px(context, mLeftViewHeight.toFloat())))
        mRightTitleLayout = LinearLayout(getContext())
        for (i in mRightTitleList.indices) {
            addListHeaderTextView(mRightTitleList[i],
                mRightTitleWidthList!![i],
                mRightTitleLayout!!)
        }
        headLayout.addView(mRightTitleLayout)
        return headLayout
    }

    /**
     * 创建数据展示布局
     *
     * @return
     */
    private fun createMoveRecyclerView(): View {
        val linearLayout = RelativeLayout(getContext())
        mRecyclerView = RecyclerView(getContext())
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView!!.layoutManager = layoutManager
        if (null != mAdapter) {
            if (mAdapter is CommonAdapter<*>) {
                mRecyclerView!!.adapter = mAdapter as CommonAdapter<*>?
                mMoveViewList = (mAdapter as CommonAdapter<*>).moveViewList
            }
        }
        linearLayout.addView(mRecyclerView, LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT))
        return linearLayout
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    fun setAdapter(adapter: Any?) {
        mAdapter = adapter
        initView()
    }

    /**
     * 设置头部title单个布局
     *
     * @param headerName
     * @param width
     * @param leftLayout
     * @return
     */
    private fun addListHeaderTextView(
        headerName: String,
        width: Int,
        leftLayout: LinearLayout
    ): TextView {
        val textView = TextView(getContext())
        textView.text = headerName
        textView.gravity = Gravity.CENTER
        leftLayout.addView(textView, width, dip2px(context, 50f))
        return textView
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> {
                mFixX = mMoveOffsetX
                //每次左右滑动都要更新CommonAdapter中的mFixX的值
                (mAdapter as CommonAdapter<*>?)!!.setFixX(mFixX)
            }
            MotionEvent.ACTION_DOWN -> mStartX = ev.x
            MotionEvent.ACTION_MOVE -> {
                val offsetX = Math.abs(ev.x - mStartX).toInt()
                return if (offsetX > mTriggerMoveDis) { //水平移动大于30触发拦截
                    true
                } else {
                    false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 右边可滑动的总宽度
     *
     * @return
     */
    private fun rightTitleTotalWidth(): Int {
        if (0 == mRightTotalWidth) {
            for (i in mRightTitleWidthList!!.indices) {
                mRightTotalWidth = mRightTotalWidth + mRightTitleWidthList!![i]
            }
        }
        return mRightTotalWidth
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_MOVE -> {
                val offsetX = Math.abs(event.x - mStartX).toInt()
                if (offsetX > 30) {
                    mMoveOffsetX = (mStartX - event.x + mFixX).toInt()
                    if (0 > mMoveOffsetX) {
                        mMoveOffsetX = 0
                    } else {
                        //当滑动大于最大宽度时，不在滑动（右边到头了）
                        if (mRightTitleLayout!!.width + mMoveOffsetX > rightTitleTotalWidth()) {
                            mMoveOffsetX = rightTitleTotalWidth() - mRightTitleLayout!!.width
                        }
                    }
                    //跟随手指向右滚动
                    mRightTitleLayout!!.scrollTo(mMoveOffsetX, 0)
                    if (null != mMoveViewList) {
                        var i = 0
                        while (i < mMoveViewList!!.size) {

                            //使每个item随着手指向右滚动
                            mMoveViewList!![i]!!.scrollTo(mMoveOffsetX, 0)
                            i++
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                mFixX = mMoveOffsetX //设置最大水平平移的宽度
                //每次左右滑动都要更新CommonAdapter中的mFixX的值
                (mAdapter as CommonAdapter<*>?)!!.setFixX(mFixX)
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 列表头部数据
     *
     * @param headerListData
     */
    fun setHeaderListData(headerListData: Array<String>) {
        mRightTitleList = headerListData
        mRightTitleWidthList = IntArray(headerListData.size)
        for (i in headerListData.indices) {
            mRightTitleWidthList!![i] = dip2px(context, mRightItemWidth.toFloat())
        }
        mLeftTextWidthList = intArrayOf(dip2px(context, mLeftViewWidth.toFloat()))
        mLeftTextList = arrayOf("名称")
    }

    companion object {
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}