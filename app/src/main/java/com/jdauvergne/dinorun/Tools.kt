package com.jdauvergne.dinorun

import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController


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

         fun hideSystemUI(window: Window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.decorView.post {
                    window.insetsController?.let { controller ->
                        controller.hide(WindowInsets.Type.systemBars())
                        controller.systemBarsBehavior =
                            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        )
            }
        }
    }
}