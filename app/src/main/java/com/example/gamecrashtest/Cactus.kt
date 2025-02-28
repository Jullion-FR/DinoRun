package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import com.example.gamecrashtest.Tools.Companion.dpToPx
import kotlinx.coroutines.delay

class Cactus(
    private val parentLayout: ConstraintLayout,
    private var speed: Long,
    val size: CactusSizesEnum
) {
    private val cactusImageView = ImageView(parentLayout.context)
    var spriteOffset = 0f
    var x: Float
        get() = cactusImageView.x
        set(value) {
            cactusImageView.x = value
        }
    private var movementAnimator: ObjectAnimator? = null

    init {
        // Dimensions
        val width = size.width
        val height = size.height

        // Position à droite de l'écran, aligné au sol
        val params = ConstraintLayout.LayoutParams(width, height).apply {
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            marginEnd = 0
            bottomToTop = R.id.groundView
            bottomMargin = -(14).dpToPx
        }

        // Configuration du contenu de l'ImageView
        cactusImageView.layoutParams = params
        cactusImageView.setImageResource(size.spriteIdList.random())

        cactusImageView.setBackgroundColor(Color.parseColor("#FF5722"))
    }

    fun startMoving(startX: Float, targetX: Float) {
        movementAnimator?.cancel()

        movementAnimator = ObjectAnimator.ofFloat(
            cactusImageView,
            "x",
            startX,
            targetX
        ).apply {
            interpolator = LinearInterpolator()
            duration = speed
            start()
            doOnEnd {
                dropSelfFromParent()
            }
        }
    }
    private fun cancelMovement() {
        Handler(Looper.getMainLooper()).post {
            movementAnimator?.cancel()
        }
    }

    fun spawn(xPos: Float = Tools.screenWidth){
        // Ajout au layout parent
        addSelfToParent()
        x = xPos
    }

    private fun dropSelfFromParent() {
        if (MainActivity.isGameRunning) {
            parentLayout.removeView(cactusImageView)
        }
    }
    private fun addSelfToParent() {
        parentLayout.addView(cactusImageView)
    }

    suspend fun collisionChecker(dinosaur: Dinosaur) {
        val dino = dinosaur.dinoImageView
        val errorMargin = 20f
        while (MainActivity.isGameRunning) {
            delay(5)

            val dinoBaseY = dino.y + dino.height - errorMargin
            val cactusTopY = cactusImageView.y
            val minX = dino.x - errorMargin
            //(dino.width/2) -> approximation of foot's position
            val maxX = dino.x + (dino.width/2) + errorMargin

            if (x in minX..maxX && dinoBaseY > cactusTopY) {
                dinosaur.deathSequence()
                break
            }
        }
        cancelMovement()
    }
}