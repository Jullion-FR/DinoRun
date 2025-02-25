package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import com.example.gamecrashtest.Tools.Companion.dpToPx

class Cactus(
    private val parentLayout: ConstraintLayout,
    var speed: Long = 2500L,
    val size: CactusSizesEnum = CactusSizesEnum.Small
) {
    private val cactusImageView = ImageView(parentLayout.context)
    var spriteOffset = 0f
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
    fun spawn(){
        // Ajout au layout parent
        addSelfToParent()
        x = Tools.screenWidth
    }
    private fun dropSelfFromParent() {
        println("${x}")
        parentLayout.removeView(cactusImageView)
    }
    private fun addSelfToParent() {
        parentLayout.addView(cactusImageView)
    }
}