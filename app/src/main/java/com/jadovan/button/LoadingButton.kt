package com.jadovan.button

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.jadovan.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var buttonName = resources.getString(R.string.button_name)
    private val rect = Rect()
    private var changePercentage = 0f

    private val buttonColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    }

    private val animatedButtonColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
    }

    private val circleColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
    }

    private val textColor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
        color = ResourcesCompat.getColor(resources, R.color.white, null)
    }

    private val animator = ObjectAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (p) {
            buttonState -> invalidate()
        }
        when (old) {
            ButtonState.Completed -> animator.cancel()
            else -> animator.start()
        }
        when (new) {
            ButtonState.Loading -> animator.start()
            else -> animator.cancel()
        }
    }

    private var loadingButtonBackgroundColor = 0
    private var loadingButtonColor = 0
    private var loadingButtonTextColor = 0
    private var loadingButtonTextSize = 0.0f
    private var loadingButtonCircleColor = 0
    private var loadingButtonAnimatedText = ""

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadingButtonBackgroundColor =
                    getColor(R.styleable.LoadingButton_loadingButtonBackgroundColor, 0)
            loadingButtonColor = getColor(R.styleable.LoadingButton_loadingButtonColor, 0)
            loadingButtonTextColor =
                    getColor(R.styleable.LoadingButton_loadingButtonTextColor, 0)
            loadingButtonTextSize =
                    getDimension(R.styleable.LoadingButton_loadingButtonTextSize, 0.0f)
            loadingButtonCircleColor =
                    getColor(R.styleable.LoadingButton_loadingButtonCircleColor, 0)
            loadingButtonAnimatedText =
                    getString(R.styleable.LoadingButton_loadingButtonAnimatedText).toString()
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        animator.setFloatValues(0f, widthSize.toFloat())
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                isClickable = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                isClickable = true
            }
        })
        animator.addUpdateListener { animation ->
            changePercentage = animation?.animatedValue as Float
            invalidate()
        }

        when (buttonState) {
            ButtonState.Clicked -> {
                animator.duration = 300
                animator.start()
            }
            ButtonState.Completed -> {
                animator.end()
            }
            else -> ButtonState.Loading
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLoadingButton(canvas)
    }

    private fun drawLoadingButton(canvas: Canvas) {
        textColor.getTextBounds(buttonName, 0, buttonName.length, rect)

        when (buttonState) {
            ButtonState.Clicked -> {
                animatedButtonColor.color =
                        ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
                circleColor.color =
                        ResourcesCompat.getColor(resources, R.color.colorAccent, null)
                buttonName = loadingButtonAnimatedText
            }
            ButtonState.Completed -> {
                animatedButtonColor.color = buttonColor.color
                circleColor.color = buttonColor.color
                buttonName = resources.getString(R.string.button_name)
            }
            else -> ButtonState.Loading
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), buttonColor)
        canvas.drawRect(0f, 0f, changePercentage, height.toFloat(), animatedButtonColor)

        canvas.drawText(buttonName, (widthSize / 2).toFloat(),
                (heightSize / 1.5).toFloat(), textColor)

        canvas.translate(
                (widthSize / 1.9 + rect.width() / 1.9).toFloat(),
                (heightSize / 4 + rect.height() / 4).toFloat()
        )

        canvas.drawArc(0f, 0f, rect.height().toFloat(), rect.height().toFloat(),
                0f, (360 * changePercentage) / widthSize, true, circleColor)

    }

    fun changedButtonState(newButtonState: ButtonState) {
        buttonState = newButtonState
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}
