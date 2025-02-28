package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.example.gamecrashtest.MainActivity.Companion.isGameRunning
import kotlinx.coroutines.*

class Dinosaur(private val context: Context, val dinoImageView: ImageView) {
    private var isJumping = false
    private val runningSprites = listOf(
        R.drawable.dino_run1,
        R.drawable.dino_run2
    )

    private val cachedSprites: List<Drawable> = runningSprites.map { res ->
        ContextCompat.getDrawable(context, res)!!
    }

    private var animUp: ObjectAnimator? = null
    private var animDown: ObjectAnimator? = null
    private var spriteJob: Job? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val handler = Handler(Looper.getMainLooper())

    init {
        dinoImageView.setImageResource(R.drawable.dino_idle)
    }

    suspend fun dinoStartingAnimation() {
        dinoImageView.setImageResource(R.drawable.dino_death)
        delay(300)
        if (!isJumping) {
            jump(125)
        }
    }

    fun touchScreenResponse() {
        if (!isJumping) jump()
    }

    private fun jump(height: Int = 400) {
        isJumping = true
        val baseDinoY = dinoImageView.y
        dinoImageView.setImageResource(R.drawable.dino_jump)

        animUp = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY, baseDinoY - height).apply {
            duration = 300
        }
        animDown = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY - height, baseDinoY).apply {
            duration = 300
        }

        animUp?.start()
        animUp?.doOnEnd {
            animDown?.start()
            animDown?.doOnEnd {
                isJumping = false
            }
        }
    }

    private fun cancelJump() {
        handler.post {
            animDown?.cancel()
            animUp?.cancel()
            isJumping = false
        }
    }

    fun startSpriteCycle() { //TODO POSE GROS PB POUR LE FRAMERATE ALED
        spriteJob?.cancel()
        spriteJob = coroutineScope.launch {
            var index = 0
            while (isGameRunning) {
                if (!isJumping) {
                    dinoImageView.setImageDrawable(cachedSprites[index])
                }
                index = (index + 1) % cachedSprites.size
                delay(75)
            }
        }
    }

    private fun stopSpriteCycle() {
        spriteJob?.cancel()
    }

    fun deathSequence() {
        stopSpriteCycle()
        killCoroutineScope()
        cancelJump()
        dinoImageView.setImageResource(R.drawable.dino_death)
    }

    private fun killCoroutineScope() {
        coroutineScope.cancel()
    }
}