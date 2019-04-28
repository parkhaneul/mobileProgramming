package kr.ac.ajou.daygram

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.ImageView

class RoundImageView : ImageView{
    var radius : Float = 100.0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas){
        //var drawable : Drawable = context.getDrawable(R.drawable.round_box)
        //this.background = drawable;
        //this.clipToOutline = true
    }
}