package com.example.gamecrashtest

import android.animation.ObjectAnimator
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
    var speed: Long = 1500L,
    val size: CactusSizesEnum = CactusSizesEnum.Small
) {
    private val cactusImageView = ImageView(parentLayout.context)
    var spriteOffset = 0f
    private var isActive = false
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
    fun cancelMovement() {
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
        parentLayout.removeView(cactusImageView)
        isActive = false
    }
    private fun addSelfToParent() {
        parentLayout.addView(cactusImageView)
        isActive = true
    }


    suspend fun collisionChecker(dinosaur: Dinosaur){
        val dino = dinosaur.dinoImageView
        val errorMargin = 20f

        val minX = 0f
        val maxX = dino.x + dino.width + errorMargin

        var dinoBaseY:Float
        delay(100)
        var cactusTopY:Float

        while (isActive && MainActivity.isGameRunning){
            dinoBaseY = dino.y + dino.height - errorMargin
            cactusTopY = cactusImageView.y

            //if the cactus is possibly in contact with Dino
            if(x in minX..maxX){
                if(dinoBaseY > cactusTopY){
                    println("$dinoBaseY, $cactusTopY")
                    cancelMovement()
                    dinosaur.deathSequence()
                }
            }
            delay(200)
        }
    }
}