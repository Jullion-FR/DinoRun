package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Dinosaur(private val dinoImageView: ImageView) {
    private var isJumping = false
    //private var baseDinoY:Float

    private val runningSprites = listOf(
        R.drawable.dino_run1,
        R.drawable.dino_run2
    )

    init {
        dinoImageView.setImageResource(R.drawable.dino_idle)
    }
    suspend fun dinoStartingAnimation(){
        dinoImageView.setImageResource(R.drawable.dino_death)
        if (!isJumping){
            delay(300)
            jump(125)
        }
    }

    fun touchScreenResponse(){
        if (!isJumping) jump()
    }

    private fun jump(height: Int = 300) {
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
    }.flowOn(Dispatchers.Default) // Exécuter sur un thread différent de la UI

    suspend fun startSpriteCycle() {
        spriteFlow().collect { sprite ->
            if (!isJumping) {
                dinoImageView.setImageResource(sprite)
            }
        }
    }
}