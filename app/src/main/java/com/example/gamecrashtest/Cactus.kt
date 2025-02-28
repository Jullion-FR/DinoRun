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
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.gamecrashtest.MainActivity.Companion.isGameRunning
import com.example.gamecrashtest.Tools.Companion.dpToPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class Cactus(
    private val parentLayout: ConstraintLayout,
    val size: CactusSizesEnum) {
    companion object {
        var speed: Long = 1500L
        var activeCactusList = mutableListOf<Cactus>()
        fun cancelAll(){
            activeCactusList.forEach{ cactus ->
                cactus.cancelMovement()
            }
        }
    }

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
                if(MainActivity.isGameRunning) dropSelfFromParent()
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

    private fun addSelfToParent() {
        Cactus.activeCactusList.add(this)
        parentLayout.addView(cactusImageView)
    }

    private fun dropSelfFromParent() {
        Cactus.activeCactusList.remove(this)
        parentLayout.removeView(cactusImageView)
    }

    private fun collisionFlow(dinosaur: Dinosaur) = flow {
        val dino = dinosaur.dinoImageView
        val errorMargin = 20f

        while (MainActivity.isGameRunning) {
            val dinoWidth = dino.width.toFloat()
            val dinoHeight = dino.height.toFloat()
            val dinoBaseY = dino.y + dinoHeight - errorMargin
            val cactusTopY = cactusImageView.y

            val minX = dino.x - errorMargin
            val maxX = dino.x + (dinoWidth * 3 / 4) + errorMargin

            val effectiveDinoBaseY = dinoBaseY - (dinoHeight / 4)

            if (x in minX..maxX && effectiveDinoBaseY > cactusTopY) {
                emit(true)
                break
            }

            delay(5)
        }
    }.flowOn(Dispatchers.Default)

    fun startCollisionCheck(lifecycleScope: LifecycleCoroutineScope, dinosaur: Dinosaur) {
        lifecycleScope.launch {
            collisionFlow(dinosaur).collect { collided ->
                if (collided){
                    isGameRunning = false
                    println("Ouch, it's a cactus")
                    lifecycleScope.launch {
                        dinosaur.deathSequence()
                    }
                    lifecycleScope.launch {
                        Cactus.cancelAll()
                    }
                }
            }
        }
    }
}