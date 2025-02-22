package com.example.gamecrashtest

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup


class Tools {
    companion object{
        fun Context.dpToPx(dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                this.resources.displayMetrics
            ).toInt()
        }
        fun removeView(view: View) {
            val vg = (view.parent) as ViewGroup
            vg.removeView(view)
        }
    }
}