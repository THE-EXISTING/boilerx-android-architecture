package com.existing.boilerx.common.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.widget.AppCompatTextView
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import com.existing.boilerx.common.base.extension.calculateFontSize
import com.existing.boilerx.common.R
import com.existing.boilerx.core.base.view.ViewSavedState

@Suppress("UNUSED_PARAMETER")
open class AppTextView
@JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = android.R.attr.textViewStyle)
    : AppCompatTextView(context, attrs, defStyleAttr), LoaderController.LoaderView {

    private var enableColor = -1
    private var loaderController: LoaderController? = null
    private var isLoading: Boolean = false

    init {
        initWithAttrs(attrs, defStyleAttr, 0)
        init(context, attrs)
    }

    private var htmlString: String = ""

    @SuppressLint("CustomViewStyleable")
    private fun initWithAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        this.enableColor = currentTextColor
        var attrArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewLoading, defStyleRes, 0)
        isLoading = attrArray.getBoolean(R.styleable.TextViewLoading_loading, false)
        attrArray.recycle()

        attrArray = context.obtainStyledAttributes(attrs, R.styleable.AppTextView, defStyleRes, 0)
        htmlString = attrArray.getString(R.styleable.AppTextView_html) ?: ""
        attrArray.recycle()

    }

    private fun init(context: Context, attrs: AttributeSet?) {
        loaderController = LoaderController(this, isLoading)
        this.calculateFontSize()

        if (htmlString.isNotEmpty()) {
            setHtml(htmlString)
        }
    }

    fun setHtml(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT))
        } else {
            setText(Html.fromHtml(text))
        }
    }

    override
    fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            setTextColor(enableColor)
        } else {
            val typedValue = TypedValue()
            val theme = context.theme
            theme.resolveAttribute(R.attr.colorTextDisabled, typedValue, true)
            setTextColor(typedValue.data)
        }
    }

    override
    fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.enableColor = this.enableColor
        return ss
    }

    override
    fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        this.enableColor = state.enableColor
    }

    override
    fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController?.onSizeChanged()
    }

    override
    fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController?.onDraw(canvas, compoundPaddingLeft.toFloat(),
                compoundPaddingTop.toFloat(),
                compoundPaddingRight.toFloat(),
                compoundPaddingBottom.toFloat())
    }

    override
    fun setText(text: CharSequence, type: TextView.BufferType) {
        super.setText(text, type)
        loaderController?.stopLoading()
    }

    override
    fun setRectColor(rectPaint: Paint) {
        val typeface = typeface
        if (typeface != null && typeface.style == Typeface.BOLD) {
            rectPaint.color = LoaderController.COLOR_DARKER_GREY
        } else {
            rectPaint.color = LoaderController.COLOR_DEFAULT_GREY
        }
    }

    override
    fun valueSet(): Boolean {
        return !TextUtils.isEmpty(text)
    }

    override
    fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController?.removeAnimatorUpdateListener()
    }

    fun setFontStyle(fontStyle: Int) {
        setTypeface(typeface, fontStyle)
    }


    fun resetLoading() {
        if (!TextUtils.isEmpty(text)) {
            super.setText("")
            loaderController?.startLoading()
        }

    }


    internal open class SavedState : ViewSavedState {
        var enableColor: Int = 0

        internal constructor(superState: Parcelable) : super(superState)

        internal constructor(`in`: Parcel) : super(`in`) {
            this.enableColor = `in`.readInt()
        }

        override
        fun writeToParcel(out: Parcel, flags: Int) = with(out) {
            super.writeToParcel(out, flags)
            out.writeInt(enableColor)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override
                fun createFromParcel(`in`: Parcel): SavedState? = SavedState(`in`)

                override
                fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }
}