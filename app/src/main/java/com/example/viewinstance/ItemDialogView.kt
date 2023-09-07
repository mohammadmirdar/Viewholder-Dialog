package com.example.viewinstance

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ItemDialogView"

class ItemDialogView(
    context: Context,
    val rootView: ViewGroup,
    val mainRootView: ViewGroup,
    val fabContainer: View,
    val alertConfirmButton: MainActivity.Item,
    val fabX: Int,
    val fabY: Int
) : FrameLayout(context) {
    private var subMenuHeight: Int = 128.dp()
    private var blurredDrawable: BitmapDrawable? = null
    private var subMenuScrollView: ScrollView
    private var menuContainer: LinearLayout
    private var copyText: TextView
    private var replyText: TextView
    private var editText: TextView
    private var forwardText: TextView
    var blurredView: View
    var dimmView: View

    private fun updateFabPosition() {
        val loc = IntArray(2)
        mainRootView.getLocationInWindow(loc)
        val fragmentX = loc[0]
        val fragmentY = loc[1]
        alertConfirmButton.translationX = (fabX - fragmentX).toFloat()
        alertConfirmButton.translationY = (fabY - fragmentY).toFloat()

        if (alertConfirmButton.bottom > mainRootView.bottom - subMenuHeight) {
            val objectAnimator = ObjectAnimator.ofFloat(
                alertConfirmButton,
                "translationY",
                alertConfirmButton.bottom.toFloat(),
                (mainRootView.bottom - subMenuHeight - alertConfirmButton.height - 12.dp()).toFloat()
            )
             objectAnimator.setDuration(200).start()

            objectAnimator.addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    subMenuScrollView.translationX = alertConfirmButton.translationX + 4.dp()
                    subMenuScrollView.translationY =
                        alertConfirmButton.translationY + alertConfirmButton.height + 8.dp()
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }

            })
        }

        subMenuScrollView.translationX = alertConfirmButton.translationX + 4.dp()
        subMenuScrollView.translationY =
            alertConfirmButton.translationY + alertConfirmButton.height + 8.dp()
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
        dimmView.setBackgroundColor(Color.GRAY)
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

        subMenuScrollView = ScrollView(context).apply {
            background =
                ResourcesCompat.getDrawable(context.resources, R.drawable.rounded_drawable, null)
            visibility = VISIBLE
        }

        menuContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        subMenuScrollView.addView(
            menuContainer,
            LayoutHelper.createFrame(60.dp(), LayoutHelper.WRAP_CONTENT)
        )

        copyText = TextView(context).apply {
            text = "Copy"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }
        menuContainer.addView(
            copyText,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 32)
        )

        editText = TextView(context).apply {
            text = "Edit"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }

        menuContainer.addView(
            editText,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 32)
        )

        forwardText = TextView(context).apply {
            text = "Forward"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }

        menuContainer.addView(
            forwardText,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 32)
        )

        replyText = TextView(context).apply {
            text = "Reply"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }
        menuContainer.addView(
            replyText,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 32)
        )

        addView(
            subMenuScrollView,
            LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT)
        )

        updateFabPosition()
    }


    fun show() {
        updateFabPosition()
        val anim = ValueAnimator.ofFloat(0f, 1f).setDuration(250)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
//                    fabContainer.visibility = View.INVISIBLE
                val scaleFactor = 10f
                val w = (mainRootView.measuredWidth / scaleFactor).toInt()
                val h = (mainRootView.measuredHeight / scaleFactor).toInt()
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