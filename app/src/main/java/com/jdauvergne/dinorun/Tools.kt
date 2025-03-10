package com.jdauvergne.dinorun

import android.content.Context


class Tools {
    companion object {
        var screenWidth: Float = 0f
            private set
        var screenHeight: Float = 0f
            private set
        var globalMaxLeftX: Float = 0f
            private set

        //Required in MainActivity's onCreate()
        fun initScreenWidth(context: Context) {
            screenWidth = context.resources.displayMetrics.widthPixels.toFloat()
            globalMaxLeftX = -screenWidth
        }

        fun initScreenHeight(context: Context) {
            screenHeight = context.resources.displayMetrics.heightPixels.toFloat()
        }
    }
}