package com.example.viewinstance

import android.graphics.PointF
import android.view.animation.Interpolator
import kotlin.math.abs

class CubicBezierInterpolator(start: PointF, end: PointF) : Interpolator {
    private var start: PointF
    private var end: PointF
    private var a = PointF()
    private var b = PointF()
    private var c = PointF()

    constructor(startX: Float, startY: Float, endX: Float, endY: Float) : this(PointF(startX, startY), PointF(endX, endY))

    companion object {
        val DEFAULT = CubicBezierInterpolator(0.25f, 0.1f, 0.25f, 1f)
        val EASE_OUT = CubicBezierInterpolator(0f, 0f, .58f, 1f)
        val EASE_OUT_QUINT = CubicBezierInterpolator(.23f, 1f, .32f, 1f)
        val EASE_IN = CubicBezierInterpolator(.42f, 0f, 1f, 1f)
        val EASE_BOTH = CubicBezierInterpolator(.42f, 0f, .58f, 1f)
    }

    init {
        require(!(start.x < 0 || start.x > 1)) { "startX value must be in the range [0, 1]" }
        require(!(end.x < 0 || end.x > 1)) { "endX value must be in the range [0, 1]" }
        this.start = start
        this.end = end
    }

    override fun getInterpolation(time: Float): Float {
        return getBezierCoordinateY(getXForTime(time))
    }

    private fun getBezierCoordinateY(time: Float): Float {
        c.y = 3 * start.y
        b.y = 3 * (end.y - start.y) - c.y
        a.y = 1 - c.y - b.y
        return time * (c.y + time * (b.y + time * a.y))
    }

    private fun getXForTime(time: Float): Float {
        var x = time
        var z: Float
        for (i in 1..13) {
            z = getBezierCoordinateX(x) - time
            if (abs(z) < 1e-3) {
                break
            }
            x -= z / getXDerivative(x)
        }
        return x
    }

    private fun getXDerivative(t: Float): Float {
        return c.x + t * (2 * b.x + 3 * a.x * t)
    }

    private fun getBezierCoordinateX(time: Float): Float {
        c.x = 3 * start.x
        b.x = 3 * (end.x - start.x) - c.x
        a.x = 1 - c.x - b.x
        return time * (c.x + time * (b.x + time * a.x))
    }
}