package com.jdauvergne.dinorun.cactus

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.lifecycle.MutableLiveData
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.display.MainActivity.Companion.gameOver
import com.jdauvergne.dinorun.display.MainActivity.Companion.gameSpeed
import com.jdauvergne.dinorun.display.MainActivity.Companion.isGameRunning
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
): CactusInterface {
    override var x: Float
        get() = cactusImageView.x
        set(value) {
            cactusImageView.x = value + spriteOffset
        }
    override val width = size.width
    override var spriteOffset = 0f

    private var parentLayout: ViewGroup? = null
    private var movementAnimator: ObjectAnimator? = null
    private var job: Job? = null

    override val isMoving = MutableLiveData<Boolean>()

    override fun startMovement(startX: Float?, targetX: Float?) {
        if (startX == null || targetX == null) return

        cactusImageView.post {
            movementAnimator = ObjectAnimator.ofFloat(
                cactusImageView,
                "x",
                startX,
                targetX
            ).apply {
                interpolator = LinearInterpolator()
                duration = gameSpeed
                addUpdateListener {
                    if (!isGameRunning()) pause()
                    if(cactusImageView.x <= -(size.width*2) ) {
                        cancel()
                    }
                }
                doOnStart {
                    isMoving.value = true
                }
                doOnEnd {
                    isMoving.value = false
                }
                start()
            }
        }
    }

    override fun stopMovement() {
        CoroutineScope(Dispatchers.Main).launch {
            movementAnimator?.cancel()
        }
    }

    override fun pauseMovement() {
        movementAnimator?.pause()
    }

    override fun resumeMovement() {
        movementAnimator?.resume()
    }

    override fun initialize(anchorView: View?, xPos: Float) {
        cactusImageView.layoutParams = buildCactusConstraints(anchorView)
        x = xPos
    }

    override fun stopCollisionCheck() {
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

    override fun startCollisionCheck(dinosaur: Dinosaur) {
        job = CoroutineScope(Dispatchers.Main).launch {
            collisionFlow(dinosaur).collect { collided ->
                if (collided) {
                    println("Ouch, it's a cactus")
                    dinosaur.death()
                }
            }
        }
    }

    private fun collisionFlow(dinosaur: Dinosaur) = flow {
        val dino = dinosaur.dinoImageView
        val dinoWidth = dino.width.toFloat()
        val dinoHeight = dino.height.toFloat()

        val exclusionMarginX = dinoWidth / 4
        val exclusionMarginY = dinoHeight / 4

        val errorMarginX = 0 //dinoWidth / 24
        val errorMarginY = 0 //dinoHeight / 24

        while (currentCoroutineContext().isActive) {
            val minX = dino.x + errorMarginX
            val maxX = dino.x + dinoWidth - exclusionMarginX

            val dinoBaseY = dino.y + dinoHeight - exclusionMarginY - errorMarginY
            val cactusTopY = cactusImageView.y

            if (x in minX..maxX && dinoBaseY >= cactusTopY) {
                emit(true)
                break
            }
            delay(5)
        }
    }.flowOn(Dispatchers.Default)
}

