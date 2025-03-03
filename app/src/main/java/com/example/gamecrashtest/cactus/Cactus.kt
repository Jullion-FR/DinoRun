package com.example.gamecrashtest.cactus

import android.animation.ObjectAnimator
import android.provider.CalendarContract.Colors
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.gamecrashtest.Dinosaur
import com.example.gamecrashtest.MainActivity
import com.example.gamecrashtest.MainActivity.Companion.isGameRunning
import com.example.gamecrashtest.R
import com.example.gamecrashtest.Tools
import com.example.gamecrashtest.Tools.Companion.dpToPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class Cactus(
    private val parentLayout: ConstraintLayout,
    val size: CactusSizesEnum,
    private val cactusImageView: ImageView
) {
    companion object {
        var speed: Long = 1375
    }

    var spriteOffset = 0f
    var x: Float
        get() = cactusImageView.x
        set(value) {
            cactusImageView.x = value
        }
    private var movementAnimator: ObjectAnimator? = null

    init {
        setupCactus()
    }

    private fun setupCactus() {
        val width = size.width
        val height = size.height
        println("$width, $height")

        val params = ConstraintLayout.LayoutParams(
            width.toInt(),
            height.toInt()
        ).apply {
                bottomToTop = R.id.groundView
                bottomMargin = -(14).dpToPx
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
            duration = speed
            addUpdateListener {
                if (!MainActivity.isGameRunning) {
                    pause()
                }
            }
            start()
            doOnEnd {
                dropSelfFromParent()
            }
        }
    }

    fun spawn(xPos: Float = Tools.screenWidth){
        // Ajout au layout parent
        addSelfToParent()
        x = xPos
    }

    private fun addSelfToParent() {
        parentLayout.addView(cactusImageView)
    }

    private fun dropSelfFromParent() {
        parentLayout.removeView(cactusImageView)
        cactusImageView.setImageDrawable(null)
    }

    private fun collisionFlow(dinosaur: Dinosaur) = flow {
        val dino = dinosaur.dinoImageView
        val errorMargin = 30f

        while (MainActivity.isGameRunning) {
            val dinoWidth = dino.width.toFloat()
            val dinoHeight = dino.height.toFloat()
            val dinoBaseY = dino.y + dinoHeight - errorMargin
            val cactusTopY = cactusImageView.y

            val minX = dino.x - errorMargin
            val maxX = dino.x + (dinoWidth * 3 / 4) - errorMargin/2

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
                if (collided){
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