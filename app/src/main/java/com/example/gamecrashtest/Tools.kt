package com.example.gamecrashtest

import android.content.Context
import android.util.TypedValue

class Tools {
    companion object{
        fun Context.dpToPx(dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                this.resources.displayMetrics
            ).toInt()
        }
    }
}