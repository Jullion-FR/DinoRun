package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.delay

class Dinosaur(private val dinoImageView: ImageView) {
    private var isJumping = false

    private val runningSprites = listOf(
        R.drawable.dino_run1,
        R.drawable.dino_run2
    )

    init {
        dinoImageView.setImageResource(R.drawable.dino_idle)
    }

    suspend fun startGameAnimation(){
        dinoImageView.setImageResource(R.drawable.dino_death)
        delay(300)
        jump(100)
        cycleSprites()
    }

    fun touchScreenResponse(){
        if (!isJumping) {
            isJumping = true
            jump()
        }
    }

    private fun jump(height: Int = 300) {
        dinoImageView.setImageResource(R.drawable.dino_jump)

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

     private suspend fun cycleSprites(){
        var index = 0
        while (true) {
            if(!isJumping) {
                dinoImageView.setImageResource(runningSprites[index])
                index = (index + 1) % runningSprites.size
            }
            delay(75)
        }
    }
}