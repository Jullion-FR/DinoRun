package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Dinosaur(val dinoImageView: ImageView) {
    private var isJumping = false
    var x: Float
        get() = dinoImageView.x
        set(value) {
            dinoImageView.x = value
        }
    private val runningSprites = listOf(
        R.drawable.dino_run1,
        R.drawable.dino_run2
    )

    init {
        dinoImageView.setImageResource(R.drawable.dino_idle)
    }
    suspend fun dinoStartingAnimation(){
        dinoImageView.setImageResource(R.drawable.dino_death)
        delay(300)
        if (!isJumping){
            jump(125)
        }
    }

    fun touchScreenResponse(){
        if (!isJumping) jump()
    }

    private fun jump(height: Int = 400) {
        isJumping = true
        val baseDinoY = dinoImageView.y

        dinoImageView.setImageResource(R.drawable.dino_jump)

        val animUp = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY, baseDinoY - height)
        val animDown = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY - height, baseDinoY)

        val duration: Long = 300
        animUp.duration = duration
        animDown.duration = duration

        animUp.start()
        animUp.doOnEnd {
            animDown.start()
            animDown.doOnEnd {
                isJumping = false
            }
        }
    }

    private fun spriteFlow() = flow {
        var index = 0
        while (true) {
            emit(runningSprites[index])
            index = (index + 1) % runningSprites.size
            delay(75)
        }
    }.flowOn(Dispatchers.Default)

    suspend fun startSpriteCycle() {
        spriteFlow().collect { sprite ->
            if (!isJumping) {
                dinoImageView.setImageResource(sprite)
            }
        }
    }
}