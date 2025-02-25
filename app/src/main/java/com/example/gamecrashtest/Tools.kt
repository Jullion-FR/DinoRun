package com.example.gamecrashtest

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup


class Tools {
    companion object {
        val Int.dpToPx: Int
            get() = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                Resources.getSystem().displayMetrics
            ).toInt()

        fun removeView(view: View) {
            val vg = (view.parent) as ViewGroup
            vg.removeView(view)
        }
        var screenWidth: Float = 0f
            private set
        //Required in MainView onCreate()
        fun initScreenWidth(context: Context){
            screenWidth = context.resources.displayMetrics.widthPixels.toFloat()
        }
    }
}