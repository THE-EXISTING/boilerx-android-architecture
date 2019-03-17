package com.existing.boilerx.common.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.existing.boilerx.common.R
import com.existing.boilerx.common.base.extension.calculateFontSize
import com.thekhaeng.pushdownanim.PushDownAnim


@Suppress("UNUSED_PARAMETER")
class AppButtonView
@JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = R.attr.buttonStyle)
    : AppCompatButton(context, attrs, defStyleAttr) {

    companion object {
        private val CLICK_DURATION = 100L
    }

    private var pushDownScale: Float = 0.0f
    private var enablePushDown: Boolean = true


    init {
        initWithAttrs(attrs, defStyleAttr, 0)
        init(context, attrs)
    }


    private fun initWithAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.AppButtonView, defStyleRes, 0)
        pushDownScale = attrArray.getFloat(R.styleable.AppButtonView_pushDownScale, 0.97f)
        enablePushDown = attrArray.getBoolean(R.styleable.AppButtonView_enablePushDown, true)
        attrArray.recycle()

    }


    private fun init(context: Context, attrs: AttributeSet?) {
        this.calculateFontSize()
        if (enablePushDown) {
            PushDownAnim
                    .setPushDownAnimTo(this)
                    .setScale(PushDownAnim.MODE_STATIC_DP, 2f)
        }
    }


}