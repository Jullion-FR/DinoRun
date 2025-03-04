package com.example.gamecrashtest

import android.content.Context


class Tools {
    companion object {
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

        fun calculateSpeedPerFrame(distance: Float, durationMs: Long, frameRate: Float = 60f): Float {
            //Calculate a px/ms ratio for a movement
            return (distance / durationMs) * (1000 / frameRate)
        }
    }
}