package com.example.viewinstance

import kotlin.math.ceil

fun Float.dp() : Int{
    return if (this == 0F) {
        0
    } else ceil((1 * this).toDouble()).toInt()
}
