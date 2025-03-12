package com.jdauvergne.dinorun.cactus

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.lifecycle.MutableLiveData
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.MainActivity.Companion.gameOver
import com.jdauvergne.dinorun.MainActivity.Companion.gameSpeed
import com.jdauvergne.dinorun.MainActivity.Companion.isGameRunning
import com.jdauvergne.dinorun.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class Cactus(
    private val cactusImageView: ImageView,
    val size: CactusSizesEnum
):CactusInterface {
    override var x: Float
        get() = cactusImageView.x
        set(value) {
            cactusImageView.x = value + spriteOffset
        }
    override val width = size.width
    override var spriteOffset = 0f

    private var parentLayout: ViewGroup? = null
    private var movementAnimator: ObjectAnimator? = null
    private var job:Job? = null

    override val isMoving = MutableLiveData<Boolean>()

    override fun startMovement(startX: Float?, targetX: Float?) {
        if(startX == null || targetX == null) return
        movementAnimator = ObjectAnimator.ofFloat(
            cactusImageView,
            "x",
            startX,
            targetX
        ).apply {
            interpolator = LinearInterpolator()
            duration = gameSpeed
            addUpdateListener {
                if (!isGameRunning()) cancel()
            }
            isMoving.value = true
            start()
            doOnEnd {
                isMoving.value = false
            }
        }
    }

    override fun stopMovement() {
        CoroutineScope(Dispatchers.Main).launch {
            movementAnimator?.cancel()
        }
    }

    override fun initialize(anchorView: View?, xPos: Float) {
        cactusImageView.layoutParams = buildCactusConstraints(anchorView)
        x = xPos
    }

    override fun startCollisionCheck(dinosaur: Dinosaur) {
        job = CoroutineScope(Dispatchers.Main).launch {
            collisionFlow(dinosaur).collect { collided ->
                if (collided) {
                    gameOver()
                    println("Ouch, it's a cactus")
                    dinosaur.deathSequence()
                }
            }
        }
    }

    override fun stopCollisionCheck(){
        job?.cancel()
    }

    override fun addSelfTo(viewGroup: ViewGroup) {
        parentLayout = viewGroup
        viewGroup.addView(cactusImageView)
        cactusImageView.requestLayout()
    }

    override fun dropSelfFromParent() {
        parentLayout?.removeView(cactusImageView)
        cactusImageView.setImageDrawable(null)
    }

    private fun buildCactusConstraints(anchorView: View?): ConstraintLayout.LayoutParams {
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
        return params
    }

    private fun collisionFlow(dinosaur: Dinosaur) = flow {
        val dino = dinosaur.dinoImageView
        val dinoWidth = dino.width.toFloat()
        val dinoHeight = dino.height.toFloat()

        val errorMarginX = dinoWidth / 24
        val errorMarginY = dinoHeight / 24

        while (currentCoroutineContext().isActive) {
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
}