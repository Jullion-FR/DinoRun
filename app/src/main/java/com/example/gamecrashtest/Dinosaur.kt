package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.widget.ImageView
import androidx.annotation.UiThread
import androidx.core.animation.doOnEnd

class Dinosaur(private var dinoImageView: ImageView) {

    var isJumping = false
        private set

    fun jump(height: Int = 300) {
        val baseDinoY = dinoImageView.y

        val animUp = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY, baseDinoY - height)
        val animDown = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY - height, baseDinoY)

        val duration: Long = 300
        animUp.duration = duration
        animDown.duration = duration

        isJumping = true
        animUp.start()
        animUp.doOnEnd {
            animDown.start()
            animDown.doOnEnd {
                isJumping = false
            }
        }
    }
}