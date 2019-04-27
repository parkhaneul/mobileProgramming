package kr.ac.ajou.daygram

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView






class RoundImageView : ImageView{
    var radius : Float = 100.0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas){
        this.clipToOutline = true
    }
}