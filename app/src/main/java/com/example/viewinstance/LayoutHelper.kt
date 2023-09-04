package com.example.viewinstance

import android.graphics.Paint
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.ViewCompat

class LayoutHelper {
    companion object {
        const val MATCH_PARENT = -1
        const val WRAP_CONTENT = -2
        private fun getSize(size: Int): Int {
            return (if (size < 0) size else size.dp())
        }

        fun createFrame(
            width: Int,
            height: Int,
            gravity: Int,
            leftMargin: Int = 0,
            topMargin: Int = 0,
            rightMargin: Int = 0,
            bottomMargin: Int = 0
        ): FrameLayout.LayoutParams {
            val layoutParams =
                FrameLayout.LayoutParams(getSize(width), getSize(height), gravity)
            layoutParams.setMargins(
                leftMargin.dp(),
                topMargin.dp(),
                rightMargin.dp(),
                bottomMargin.dp()
            )
            return layoutParams
        }

        fun createFrame(width: Int, height: Int, gravity: Int): FrameLayout.LayoutParams {
            return FrameLayout.LayoutParams(
                getSize(width),
                getSize(height),
                gravity
            )
        }


        fun createFrame(width: Int, height: Int): FrameLayout.LayoutParams {
            return FrameLayout.LayoutParams(
                getSize(width),
                getSize(height)
            )
        }


        private fun createRelative(
            width: Float,
            height: Float,
            leftMargin: Int,
            topMargin: Int,
            rightMargin: Int,
            bottomMargin: Int,
            alignParent: Int,
            alignRelative: Int,
            anchorRelative: Int
        ): RelativeLayout.LayoutParams {
            val layoutParams = RelativeLayout.LayoutParams(
                getSize(width.toInt()), getSize(
                    height.toInt()
                )
            )
            if (alignParent >= 0) {
                layoutParams.addRule(alignParent)
            }
            if (alignRelative >= 0 && anchorRelative >= 0) {
                layoutParams.addRule(alignRelative, anchorRelative)
            }
            layoutParams.leftMargin = leftMargin.dp()
            layoutParams.topMargin = topMargin.dp()
            layoutParams.rightMargin = rightMargin.dp()
            layoutParams.bottomMargin = bottomMargin.dp()
            return layoutParams
        }

        fun createRelative(
            width: Int,
            height: Int,
            leftMargin: Int,
            topMargin: Int,
            rightMargin: Int,
            bottomMargin: Int
        ): RelativeLayout.LayoutParams {
            return createRelative(
                width.toFloat(),
                height.toFloat(),
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin,
                -1,
                -1,
                -1
            )
        }

        fun createRelative(
            width: Int,
            height: Int,
            leftMargin: Int,
            topMargin: Int,
            rightMargin: Int,
            bottomMargin: Int,
            alignParent: Int
        ): RelativeLayout.LayoutParams {
            return createRelative(
                width.toFloat(),
                height.toFloat(),
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin,
                alignParent,
                -1,
                -1
            )
        }

        fun createRelative(
            width: Float,
            height: Float,
            leftMargin: Int,
            topMargin: Int,
            rightMargin: Int,
            bottomMargin: Int,
            alignRelative: Int,
            anchorRelative: Int
        ): RelativeLayout.LayoutParams {
            return createRelative(
                width,
                height,
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin,
                -1,
                alignRelative,
                anchorRelative
            )
        }

        fun createRelative(
            width: Int,
            height: Int,
            alignParent: Int,
            alignRelative: Int,
            anchorRelative: Int
        ): RelativeLayout.LayoutParams {
            return createRelative(
                width.toFloat(),
                height.toFloat(),
                0,
                0,
                0,
                0,
                alignParent,
                alignRelative,
                anchorRelative
            )
        }

        fun createRelative(width: Int, height: Int): RelativeLayout.LayoutParams {
            return createRelative(width.toFloat(), height.toFloat(), 0, 0, 0, 0, -1, -1, -1)
        }

        fun createRelative(
            width: Int,
            height: Int,
            alignParent: Int
        ): RelativeLayout.LayoutParams {
            return createRelative(
                width.toFloat(),
                height.toFloat(),
                0,
                0,
                0,
                0,
                alignParent,
                -1,
                -1
            )
        }

        fun createRelative(
            width: Int,
            height: Int,
            alignRelative: Int,
            anchorRelative: Int
        ): RelativeLayout.LayoutParams {
            return createRelative(
                width.toFloat(),
                height.toFloat(),
                0,
                0,
                0,
                0,
                -1,
                alignRelative,
                anchorRelative
            )
        }

        fun createLinear(
            width: Int,
            height: Int,
            weight: Float,
            gravity: Int,
            leftMargin: Int = 0,
            topMargin: Int = 0,
            rightMargin: Int = 0,
            bottomMargin: Int = 0
        ): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(getSize(width), getSize(height), weight)
            layoutParams.setMargins(
                leftMargin.dp(),
                topMargin.dp(),
                rightMargin.dp(),
                bottomMargin.dp()
            )
            layoutParams.gravity = gravity
            return layoutParams
        }

        fun createLinear(
            width: Int,
            height: Int,
            weight: Float,
            leftMargin: Int = 0,
            topMargin: Int = 0,
            rightMargin: Int = 0,
            bottomMargin: Int = 0
        ): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(getSize(width), getSize(height), weight)
            layoutParams.setMargins(
                leftMargin.dp(),
                topMargin.dp(),
                rightMargin.dp(),
                bottomMargin.dp()
            )
            return layoutParams
        }

        fun createLinear(
            width: Int,
            height: Int,
            gravity: Int,
            leftMargin: Int = 0,
            topMargin: Int = 0,
            rightMargin: Int = 0,
            bottomMargin: Int = 0
        ): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(getSize(width), getSize(height))
            layoutParams.setMargins(
                leftMargin.dp(),
                topMargin.dp(),
                rightMargin.dp(),
                bottomMargin.dp()
            )
            layoutParams.gravity = gravity
            return layoutParams
        }

        fun createLinear(
            width: Int,
            height: Int,
            leftMargin: Float = 0f,
            topMargin: Float = 0f,
            rightMargin: Float = 0f,
            bottomMargin: Float = 0f
        ): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(getSize(width), getSize(height))
            layoutParams.setMargins(
                leftMargin.dp(),
                topMargin.dp(),
                rightMargin.dp(),
                bottomMargin.dp()
            )
            return layoutParams
        }

        fun createLinear(
            width: Int,
            height: Int,
            weight: Float,
            gravity: Int
        ): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(getSize(width), getSize(height), weight)
            layoutParams.gravity = gravity
            return layoutParams
        }

        fun createLinear(width: Int, height: Int, gravity: Int): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(getSize(width), getSize(height))
            layoutParams.gravity = gravity
            return layoutParams
        }

        fun createLinear(width: Int, height: Int, weight: Float): LinearLayout.LayoutParams {
            return LinearLayout.LayoutParams(getSize(width), getSize(height), weight)
        }

        fun createLinear(width: Int, height: Int): LinearLayout.LayoutParams {
            return LinearLayout.LayoutParams(getSize(width), getSize(height))
        }


        fun createLinearRelatively(
            width: Float,
            height: Float,
            gravity: Int,
            startMargin: Float = 0f,
            topMargin: Float = 0f,
            endMargin: Float = 0f,
            bottomMargin: Float = 0f
        ): LinearLayout.LayoutParams {
            val linearLayoutParams = LinearLayout.LayoutParams(
                getSize(width.toInt()),
                getSize(height.toInt()),
                getAbsoluteGravity(gravity).toFloat()
            )
            linearLayoutParams.leftMargin = (startMargin).dp()
            linearLayoutParams.topMargin = topMargin.dp()
            linearLayoutParams.rightMargin = (endMargin).dp()
            linearLayoutParams.bottomMargin = bottomMargin.dp()

            return linearLayoutParams
        }

        fun manageSpec(size: Int, mode: Int): Int {
            return View.MeasureSpec.makeMeasureSpec(size, mode)
        }

        fun getTextHeight(textView: TextView): Int {
            val metrics: Paint.FontMetrics = textView.paint.fontMetrics
            return (metrics.bottom - metrics.top).toInt()
        }

        fun getTextWidth(textView: TextView): Int {
            return textView.paint.measureText(textView.text.toString()).toInt()
        }

        fun createScroll(width: Int, height: Int, gravity: Int): FrameLayout.LayoutParams? {
            return FrameLayout.LayoutParams(getSize(width), getSize(height), gravity)
        }

        fun getAbsoluteGravity(gravity: Int): Int {
            return Gravity.getAbsoluteGravity(
                gravity,
                ViewCompat.LAYOUT_DIRECTION_LTR
            )
        }
    }
}