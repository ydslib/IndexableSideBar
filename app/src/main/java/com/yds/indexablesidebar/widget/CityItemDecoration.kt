package cn.codemao.rokrok.login.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yds.indexablesidebar.CountryAdapter
import com.yds.indexablesidebar.bean.CountryBean
import com.yds.indexablesidebar.utils.DisplayUtil.Companion.dip2px
import com.yds.indexablesidebar.utils.DisplayUtil.Companion.sp2px
import kotlin.math.max

/**
 *@author hujl
 *      Email: hujlin@163.com
 *      Date : 2020-06-15 17:19
 */
class CityItemDecoration(val context: Context) : RecyclerView.ItemDecoration() {

    private var mGroupHeight = dip2px(context, 40f)
    private var mLeftMargin = dip2px(context, 20f)
    private var fontAssetsPath: String = roundFontAssetsPath

    private val mGroupPaint = Paint().apply {
        this.color = Color.parseColor("#F8F8F8")

    }
    private val mTextPaint = Paint().apply {
        textSize = sp2px(context, 14f)
        color = Color.parseColor("#3F3C3C")
        isAntiAlias = true
    }
    companion object {
        private const val defaultFontAssetsPath = "font/Nunito-SemiBold.TTF"
        private const val roundFontAssetsPath="font/Nunito-Bold.TTF"
        private const val homeFontAssetsPath="font/Nunito-ExtraBold.TTF"
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val adapter = parent.adapter as CountryAdapter
        val data = adapter.data
        val itemCount = state.itemCount
        val childCount = parent.childCount
        val left = parent.left + parent.paddingStart
        val right = parent.right - parent.paddingEnd
        var preGroupName: String
        var currentGroupName = ""
        for (i in (0 until childCount)) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            preGroupName = currentGroupName
            currentGroupName = data[position].character
            if (currentGroupName.isEmpty() || currentGroupName == preGroupName) continue
            val viewBottom = view.bottom
            var top = max(mGroupHeight, view.top)
            if (position + 1 < itemCount) {
                val nextGroupName = data[position + 1].character
                if (currentGroupName != nextGroupName && viewBottom < top) {
                    top = viewBottom
                }
            }
            c.drawRect(left.toFloat(), (top - mGroupHeight).toFloat(), right.toFloat(), top.toFloat(), mGroupPaint)
            val fm = mTextPaint.fontMetrics
            val baseLine = top - (mGroupHeight - (fm.bottom - fm.top)) / 2 - fm.bottom
            c.drawText(currentGroupName, (left + mLeftMargin).toFloat(), baseLine, mTextPaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter as CountryAdapter
        val data = adapter.data
        val pos = parent.getChildAdapterPosition(view)
        if (pos == 0 || isFirstInGroup(data, pos)) {
            outRect.top = mGroupHeight
        }
    }

    private fun isFirstInGroup(data: List<CountryBean>, pos: Int): Boolean {
        if (pos == 0) return true
        return !TextUtils.equals(data[pos - 1].character, data[pos].character)
    }


}