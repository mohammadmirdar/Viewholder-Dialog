package com.example.viewinstance

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemDialogView(
    context: Context,
    val rootView: ViewGroup,
    val mainRootView: ViewGroup,
    val fabContainer: View,
    val alertConfirmButton: MainActivity.Item,
    val fabX : Int,
    val fabY : Int
) : FrameLayout(context) {
    private var blurredDrawable: BitmapDrawable? = null
    lateinit var codeTimer: CountDownTimer
    var blurredView: View
    var dimmView: View

    private fun updateFabPosition() {
        val loc = IntArray(2)
        mainRootView.getLocationInWindow(loc)
        val fragmentX = loc[0]
        val fragmentY = loc[1]
        alertConfirmButton.translationX = (fabX - fragmentX).toFloat()
        alertConfirmButton.translationY = (fabY - fragmentY).toFloat()
        requestLayout()
    }

    init {
        blurredView = View(context)
        blurredView.setOnClickListener {
            dismiss()
        }
        addView(
            blurredView,
            LayoutHelper.createLinear(
                LayoutHelper.MATCH_PARENT,
                LayoutHelper.MATCH_PARENT
            )
        )

        dimmView = View(context)
        dimmView.setBackgroundColor(0x40000000)
        dimmView.alpha = 0f
        addView(
            dimmView,
            LayoutHelper.createLinear(
                LayoutHelper.MATCH_PARENT,
                LayoutHelper.MATCH_PARENT
            )
        )

        alertConfirmButton.visibility = View.VISIBLE
        addView(
            alertConfirmButton,
            LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT
            )
        )
        alertConfirmButton.setOnClickListener {
            dismiss()
        }

        updateFabPosition()

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    fun show() {
        CoroutineScope(Dispatchers.Main).launch {
            updateFabPosition()
            val anim = ValueAnimator.ofFloat(0f, 1f).setDuration(250)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
//                    fabContainer.visibility = View.INVISIBLE
                    val scaleFactor = 10f
                    val w = (mainRootView.getMeasuredWidth() / scaleFactor).toInt()
                    val h = (mainRootView.getMeasuredHeight() / scaleFactor).toInt()
                    val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    canvas.scale(1.0f / scaleFactor, 1.0f / scaleFactor)
                    mainRootView.draw(canvas)
                    blurredDrawable = BitmapDrawable(
                        resources,
                        bitmap
                    )
                    rootView.visibility = View.INVISIBLE
                    blurredView.background = blurredDrawable
                    blurredView.alpha = 0.0f

                    mainRootView.addView(
                        this@ItemDialogView,
                        LayoutHelper.createFrame(
                            LayoutHelper.MATCH_PARENT,
                            LayoutHelper.MATCH_PARENT,
                            Gravity.CENTER
                        )
                    )

                }

                override fun onAnimationEnd(animation: Animator) {

                }
            })
            anim.addUpdateListener { animation: ValueAnimator ->
                val `val` = animation.animatedValue as Float
                blurredView.alpha = `val`
                dimmView.alpha = `val`
                val scale = 0.5f + `val` * 0.5f
            }
            anim.interpolator = CubicBezierInterpolator.DEFAULT
            anim.start()
        }


    }


    fun dismiss() {
        val anim = ValueAnimator.ofFloat(1f, 0f).setDuration(250)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mainRootView.removeView(this@ItemDialogView)
                rootView.visibility = View.VISIBLE
                fabContainer.visibility = View.VISIBLE
            }
        })
        anim.addUpdateListener { animation: ValueAnimator ->
            val `val` = animation.animatedValue as Float
            blurredView.alpha = `val`
            dimmView.alpha = `val`
            val scale = 0.5f + `val` * 0.5f
        }
        anim.interpolator = CubicBezierInterpolator.DEFAULT
        anim.start()
    }

    enum class DialogType {
        NORMAL, WARNING
    }
}