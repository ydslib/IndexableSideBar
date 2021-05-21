package com.yds.indexable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 *@author hujl
 *      Email: hujlin@163.com
 *      Date : 2020-06-16 11:16
 */
class SlideBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    var characters = mutableListOf("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
    /**默认宽度*/
    private val defaultWidth = dip2px(context, 40f)
    //没一个字母占的高度
    private val defaultItemHeight = dip2px(context, 25f)
    private var itemHeight = defaultItemHeight

    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        textSize = sp2px(context, 12f)
        color = Color.parseColor("#4D8FFF")
    }

    private val fontMetrics = textPaint.fontMetrics
    // 计算文字高度 
    private val fontHeight = fontMetrics.bottom - fontMetrics.top

    fun setData(characterList: List<String>) {
        this.characters.clear()
        this.characters.addAll(characterList)
        requestLayout()
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w: Int
        val h: Int
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        w = if (widthMode == MeasureSpec.AT_MOST) {
            defaultWidth + paddingStart + paddingEnd
        } else {
            widthSize
        }
        h = if (heightMode == MeasureSpec.AT_MOST) {
            itemHeight = defaultItemHeight
            defaultItemHeight * characters.size + paddingTop + paddingBottom
        } else {
            itemHeight = (heightSize - paddingTop - paddingEnd) / if (characters.size==0) 1 else characters.size
            heightSize
        }
        setMeasuredDimension(w, h)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        characters.forEachIndexed { index, s ->
            val x = (width - textPaint.measureText(s)) / 2
            val y = itemHeight * (index + 1) + paddingTop
            // 计算文字baseline 
            val textBaseY = y - (itemHeight - fontHeight) / 2 - fontMetrics.bottom
            canvas?.drawText(s, x, textBaseY, textPaint)
        }
    }

    var currentIndex = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true
        val dy = (event.y - paddingTop)
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (dy < 0) return true
                val index = (dy / itemHeight).toInt()
                if (index >= 0 && index < characters.size) {
                    if (currentIndex != index) {
                        currentIndex = index
                        onIndexChangedListener?.onIndexChanged?.invoke(characters[index])
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                currentIndex = -1
                onIndexChangedListener?.onUp?.invoke()
            }
        }

        return true
    }

    var onIndexChangedListener: OnIndexChangedListener? = null

    class OnIndexChangedListener(var onUp: (() -> Unit)? = null,
                                 var onIndexChanged: ((String) -> Unit)? = null)

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f)
    }

    fun hideSoftInput(view: View): Boolean {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}