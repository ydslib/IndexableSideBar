package com.yds.indexablesidebar.widget
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.yds.indexablesidebar.utils.DisplayUtil.Companion.dip2px

class MyDividerDecoration(var context:Context): ItemDecoration() {
    var mDividerHeight = dip2px(context,1f) //线的高度

    var margin = dip2px(context,20f) //左右偏移量

    //画笔将自己做出来的分割线矩形画出颜色
    private val mPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#E2E3E7")
    }

    //在这里就已经把宽度的偏移给做好了
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //第一个ItemView不需要在上面绘制分割线
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = mDividerHeight.toInt() //指相对itemView顶部的偏移量
        }
    }

    //这里主要是绘制颜色的
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        //因为getItemOffsets是针对每一个ItemView，而onDraw方法是针对RecyclerView本身，所以需要循环遍历来设置
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(view)
            //第一个ItemView不需要绘制
            if (index == 0) {
                continue  //跳过本次循环体中尚未执行的语句，立即进行下一次的循环条件判断
            }
            val dividerTop:Float = (view.top - mDividerHeight).toFloat()
            val dividerLeft:Float = (parent.paddingLeft + margin).toFloat()
            val dividerBottom = view.top.toFloat()
            val dividerRight = (parent.width - parent.paddingRight - margin).toFloat()
            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint)
        }
    }



}