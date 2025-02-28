package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import com.example.gamecrashtest.MainActivity.Companion.isGameRunning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Dinosaur(val dinoImageView: ImageView) {
    private var isJumping = false
    private val runningSprites = listOf(
        R.drawable.dino_run1,
        R.drawable.dino_run2
    )
    private var animUp: ObjectAnimator? = null
    private var animDown: ObjectAnimator? = null


    init {
        dinoImageView.setImageResource(R.drawable.dino_idle)
        dinoImageView.setBackgroundColor(Color.parseColor("#90EE90"))
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

        animUp = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY, baseDinoY - height)
        animDown = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY - height, baseDinoY)

        val duration: Long = 300
        animUp?.duration = duration
        animDown?.duration = duration

        animUp?.start()
        animUp?.doOnEnd {
            animDown?.start()
            animDown?.doOnEnd {
                isJumping = false
            }
        }
    }

    private fun cancelJump() {
        Handler(Looper.getMainLooper()).post {
            animDown?.cancel()
            animUp?.cancel()
            isJumping = false
        }
    }

        private fun spriteFlow() = flow {
        var index = 0
        while (isGameRunning) {
            emit(runningSprites[index])
            index = (index + 1) % runningSprites.size
            if(isGameRunning)
                delay(75)
        }
    }.flowOn(Dispatchers.Default)

    suspend fun startSpriteCycle() {
        spriteFlow().collect { sprite ->
            if (!isJumping && isGameRunning) {
                dinoImageView.setImageResource(sprite)
            }
            if (!isGameRunning) return@collect
        }
    }

    fun deathSequence(){
        cancelJump()
        dinoImageView.setImageResource(R.drawable.dino_death)
    }
}