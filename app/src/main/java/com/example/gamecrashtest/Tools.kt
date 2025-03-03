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

        var screenWidth: Float = 0f
            private set
        var screenHeight: Float = 0f
            private set

        //Required in MainActivity's onCreate()
        fun initScreenWidth(context: Context){
            screenWidth = context.resources.displayMetrics.widthPixels.toFloat()
        }
        fun initScreenHeight(context: Context){
            screenHeight = context.resources.displayMetrics.heightPixels.toFloat()
        }
    }
}