package com.jdauvergne.dinorun.cactus

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.lifecycle.LifecycleCoroutineScope
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.MainActivity.Companion.gameSpeed
import com.jdauvergne.dinorun.MainActivity.Companion.isGameRunning
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.Tools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class Cactus(
    private val parentLayout: ConstraintLayout,
    val size: CactusSizesEnum,
    private val cactusImageView: ImageView,
) {
    var spriteOffset = 0f
    var x: Float
        get() = cactusImageView.x
        set(value) {
            cactusImageView.x = value
        }
    private var movementAnimator: ObjectAnimator? = null


    private fun setupCactus(anchorView: View?) {
        val width = size.width
        val height = size.height

        val params = ConstraintLayout.LayoutParams(
            width,
            height
        )
        params.apply {
            bottomToTop = R.id.groundView
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            if (anchorView != null) {
                bottomMargin = -(anchorView.height * 1.5 / 8).toInt()
            }
        }
        cactusImageView.layoutParams = params
    }


    fun startMoving(startX: Float, targetX: Float) {
        movementAnimator = ObjectAnimator.ofFloat(
            cactusImageView,
            "x",
            startX,
            targetX
        ).apply {
            interpolator = LinearInterpolator()
            duration = gameSpeed
            addUpdateListener {
                if (!isGameRunning) {
                    pause()
                }
            }
            start()
            doOnEnd {
                dropSelfFromParent()
            }
        }
    }

    fun spawn(
        anchorView: View? = null,
        xPos: Float = Tools.screenWidth
    ) {
        setupCactus(anchorView)
        addSelfToParent()
        x = xPos
    }

    private fun addSelfToParent() {
        parentLayout.addView(cactusImageView)
        cactusImageView.requestLayout()
    }

    private fun dropSelfFromParent() {
        parentLayout.removeView(cactusImageView)
        cactusImageView.setImageDrawable(null)
    }

    private fun collisionFlow(dinosaur: Dinosaur) = flow {
        val dino = dinosaur.dinoImageView
        val dinoWidth = dino.width.toFloat()
        val dinoHeight = dino.height.toFloat()

        val errorMarginX = dinoWidth / 24
        val errorMarginY = dinoHeight / 24

        while (isGameRunning) {

            val minX = dino.x - errorMarginX
            val maxX = dino.x + (dinoWidth * 3 / 4) - errorMarginX

            val dinoBaseY = dino.y + dinoHeight - errorMarginY
            val cactusTopY = cactusImageView.y
            val effectiveDinoBaseY = dinoBaseY - (dinoHeight / 4)

            if (x in minX..maxX && effectiveDinoBaseY > cactusTopY) {
                emit(true)
                break
            }
            delay(16)
        }
    }.flowOn(Dispatchers.Default)

    fun startCollisionCheck(lifecycleScope: LifecycleCoroutineScope, dinosaur: Dinosaur) {
        lifecycleScope.launch {
            collisionFlow(dinosaur).collect { collided ->
                if (collided) {
                    isGameRunning = false
                    lifecycleScope.launch {
                        println("Ouch, it's a cactus")
                        dinosaur.deathSequence()
                    }
                }
            }
        }
    }
}