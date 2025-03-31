package com.jdauvergne.dinorun

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.jdauvergne.dinorun.display.MainActivity
import com.jdauvergne.dinorun.display.MainActivity.Companion.gameOver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Dinosaur(context: Context, val dinoImageView: ImageView): DinoServiceInterface {
    private var isJumping = false

    private val deathSprite = ContextCompat.getDrawable(context, R.drawable.dino_death)
    private val jumpSprite = ContextCompat.getDrawable(context, R.drawable.dino_jump)


    private val runningSprites = listOf(
        ContextCompat.getDrawable(context, R.drawable.dino_run1),
        ContextCompat.getDrawable(context, R.drawable.dino_run2)
    )

    private  var jumpUp: ObjectAnimator
    private  var fallDown: ObjectAnimator

    private var isPaused = false

    init {
        dinoImageView.setImageResource(R.drawable.dino_idle)
        offsetDinoView()

        fallDown = buildObjectAnimator(buildPropertyValues(0f, 0f))
        fallDown.apply {
            doOnEnd {
                isJumping = false
            }
        }

        jumpUp = buildObjectAnimator(buildPropertyValues(0f, 0f))
        jumpUp.apply {
            doOnEnd {
                if (MainActivity.isGameRunning()) fallDown.start()
            }
        }

    }
    private fun offsetDinoView() {
        (dinoImageView.layoutParams as ConstraintLayout.LayoutParams).apply {
            setMargins(Tools.screenWidth.toInt() / 8, topMargin, rightMargin, bottomMargin)
            dinoImageView.layoutParams = this
        }
    }

     fun start() {
        CoroutineScope(Dispatchers.Main).launch {
            dinoImageView.setImageDrawable(deathSprite)
            delay(300)
            jump(125f)
            startRunGIF()
        }
    }

    fun jump(height: Float = 400f) {
        if (isJumping || isPaused) return
        isJumping = true

        val baseDinoY = dinoImageView.y

        jumpUp.setValues(buildPropertyValues(baseDinoY, baseDinoY - height))
        fallDown.setValues(buildPropertyValues(baseDinoY - height, baseDinoY))

        dinoImageView.setImageDrawable(jumpSprite)
        jumpUp.start()
    }


    private fun buildObjectAnimator(propertyValuesHolder:PropertyValuesHolder): ObjectAnimator {
          val objectAnimator = ObjectAnimator.ofFloat(dinoImageView, "y", 0f, 0f).apply {
                    duration = 300
                    addUpdateListener {
                        if (!MainActivity.isGameRunning()) pause()
                    }
                }
        objectAnimator.setValues(propertyValuesHolder)
        return objectAnimator
    }

    private fun buildPropertyValues(val1: Float, val2: Float): PropertyValuesHolder {
        return  PropertyValuesHolder.ofFloat(
            "y", val1, val2
        )
    }

    private fun startRunGIF() {
        CoroutineScope(Dispatchers.Main).launch {
            var index = 0;
            while (MainActivity.isGameRunning()){
                if(!isPaused && !isJumping){
                    dinoImageView.setImageDrawable(runningSprites[index])
                    index = (++index)%2
                }
                delay(65)
            }
        }
    }

    fun death() {
        dinoImageView.post {
            dinoImageView.setImageDrawable(deathSprite)
        }
        gameOver()
    }

    override fun stop() {
        death()
    }

    override fun pause() {
        isPaused = true
    }

    override fun resume() {
        isPaused = false
    }

}