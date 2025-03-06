package com.jdauvergne.dinorun

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

class Dinosaur(context: Context, val dinoImageView: ImageView) {
    var isJumping = false

    private val deathSprite = ContextCompat.getDrawable(context, R.drawable.dino_death)
    private val jumpSprite = ContextCompat.getDrawable(context, R.drawable.dino_jump)
    private val runningSprite: Drawable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeDrawable(
            ImageDecoder.createSource(context.resources, R.drawable.dino_run)
        )
    } else {
        ContextCompat.getDrawable(context, R.drawable.dino_run1)
    }

    private var animComponent: ObjectAnimator? = null

    init {
        dinoImageView.setImageResource(R.drawable.dino_idle)
        offsetDinoView()
        resetAnimComponent()
    }

    private fun resetAnimComponent(val1: Float? = null, val2: Float? = null) {
        animComponent = if (val1 != null && val2 != null) {
            ObjectAnimator.ofFloat(dinoImageView, "y", val1, val2).apply {
                duration = 300
                addUpdateListener {
                    if (!MainActivity.isGameRunning) pause()
                }
            }
        } else {
            null
        }
    }

    private fun offsetDinoView() {
        (dinoImageView.layoutParams as ConstraintLayout.LayoutParams).apply {
            setMargins(Tools.screenWidth.toInt() / 8, topMargin, rightMargin, bottomMargin)
            dinoImageView.layoutParams = this
        }
    }

    suspend fun startSequence() {
        dinoImageView.setImageDrawable(deathSprite)
        delay(300)
        jump(125)
    }

    fun jump(height: Int = 400) {
        if(isJumping) return
        isJumping = true
        dinoImageView.setImageDrawable(jumpSprite)

        val baseDinoY = dinoImageView.y

        resetAnimComponent(baseDinoY, baseDinoY - height)
        animComponent?.start()

        animComponent?.doOnEnd {
            resetAnimComponent(baseDinoY - height, baseDinoY)
            animComponent?.start()

            animComponent?.doOnEnd {
                isJumping = false
                restartRunGIF()
            }
        }
    }

    private fun restartRunGIF() {
        dinoImageView.setImageDrawable(runningSprite)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            (if (runningSprite is AnimatedImageDrawable) runningSprite.start())
        }
    }

    fun deathSequence() {
        dinoImageView.setImageDrawable(deathSprite)
    }

}