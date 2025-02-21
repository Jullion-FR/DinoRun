package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.content.Context
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.gamecrashtest.Tools.Companion.dpToPx
import kotlin.random.Random

class Cactus(private val context : Context, var speed:Int = 0, private val size: CactusSizesEnum = CactusSizesEnum.Small):
    AppCompatActivity() {
    val cactusImageView = ImageView(context)

    init {
        // Définition des dimensions
        val width = context.dpToPx(40)
        val height = if (size == CactusSizesEnum.Small) context.dpToPx(75) else context.dpToPx(100)

        // Positionner l'image
        val params = ConstraintLayout.LayoutParams(width, height)

        // Droite
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        params.marginEnd = 0
        // Bas
        params.bottomToTop = R.id.groundView
        params.bottomMargin = context.dpToPx(-14)



        cactusImageView.layoutParams = params

        // Choix de l'image selon la taille
        val resSprite: Int = when (size) {
            CactusSizesEnum.Medium -> R.drawable.cactus_medium1
            CactusSizesEnum.Small -> {
                val randomIndex = 1 //Change to random at some point
                when (randomIndex) {
                    1 -> R.drawable.cactus_small1
                    2 -> R.drawable.cactus_small2
                    3 -> R.drawable.cactus_small3
                    else -> R.drawable.cactus_small4
                }
            }
        }
        cactusImageView.setImageResource(resSprite)
    }


    fun move(){
        val screenWidth = context.resources.displayMetrics.widthPixels.toFloat()

        cactusImageView.x = screenWidth

        val slideLeft = ObjectAnimator.ofFloat(
            cactusImageView,
            "x",
            screenWidth,
            -cactusImageView.width.toFloat()
        )

        slideLeft.duration = 15000
        slideLeft.start()
    }
}