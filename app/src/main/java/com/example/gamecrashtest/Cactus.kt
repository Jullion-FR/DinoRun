package com.example.gamecrashtest

import android.animation.ObjectAnimator
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
    var isActive = false
    var x: Float
        get() = cactusImageView.x
        set(value) {
            cactusImageView.x = value
        }

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

    fun startMoving(startX:Float, targetX:Float) {
        ObjectAnimator.ofFloat(
            cactusImageView,
            "x",
            startX,
            targetX
        ).apply {
            interpolator = LinearInterpolator()
            duration = speed
            start()
        }.doOnEnd {
            dropSelfFromParent()
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
        val errorMargin = 10f

        val minX = 0f
        val maxX = dino.x + dino.width + errorMargin

        var dinoBaseY:Float
        val cactusTopY = cactusImageView.y - errorMargin

        while (isActive){
            dinoBaseY = dino.y + dino.translationY
            //if the cactus is possibly in contact with Dino
            if(x in minX..maxX){
                //if Y of dino's base > Y of cactus' top
                if(dinoBaseY > cactusTopY){
                    println("In")
                }
            }
            delay(200)
        }
    }
}