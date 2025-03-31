package com.jdauvergne.dinorun.ground

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jdauvergne.dinorun.DinoServiceInterface
import com.jdauvergne.dinorun.display.MainActivity.Companion.gameSpeed
import com.jdauvergne.dinorun.display.MainActivity.Companion.isGameRunning
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.Tools

class GroundEffect(
    private val parentLayout: ConstraintLayout,
    drawableId: Int
):DinoServiceInterface {
    private val context = parentLayout.context
    private val cachedGround: Drawable? = ContextCompat.getDrawable(context, drawableId)
    private lateinit var groundList: List<GroundEffectSplit>

    private val ratio = 34f / 1096f
    private val groundWidth = (Tools.screenWidth * 1.05).toInt()
    private val groundHeight = (groundWidth * ratio).toInt()

    private var animator: ObjectAnimator? = null

    init {
        initialize()
    }

    private fun initialize() {
        val nb = 2
        groundList = buildGroundEffectSplit(nb)

        var accumulatedOffset = 0f
        groundList.forEach { groundImageSplit ->
            groundImageSplit.spriteOffset = accumulatedOffset
            accumulatedOffset += groundWidth
            parentLayout.addView(groundImageSplit.groundEffectImageView)

            groundImageSplit.x = groundImageSplit.spriteOffset
        }
    }

    private fun buildGroundEffectSplit(nb: Int): List<GroundEffectSplit> {
        return List(nb) {
            GroundEffectSplit(
                buildImageView()
            )
        }
    }

    private fun buildImageView(): ImageView {
        val layoutParam = ConstraintLayout.LayoutParams(groundWidth, groundHeight)
        layoutParam.apply {
            topToTop = R.id.groundView
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        }
        val imageView = ImageView(context)
        imageView.apply {
            setImageDrawable(cachedGround)
            x = Tools.screenWidth.toFloat()
            layoutParams = layoutParam
        }

        return imageView
    }

    fun start() {
        startFirstMovementLoop()
    }

    override fun stop() {
        animator?.cancel()
    }

    override fun pause() {
        animator?.pause()
    }

    override fun resume() {
        animator?.resume()
    }

    private fun startFirstMovementLoop() {
        var groundImageView: ImageView

        groundList.forEachIndexed { index, groundImageSplit ->
            groundImageView = groundImageSplit.groundEffectImageView

            animator = buildMovementLooper(groundImageView)

            if (index == 0) {
                animator?.apply {
                    val propertyValuesHolder = PropertyValuesHolder.ofFloat(
                        "x", 0f, Tools.globalMaxLeftX * 2
                    )
                    setValues(propertyValuesHolder)

                    addUpdateListener {
                        if ((target as ImageView).x <= Tools.globalMaxLeftX) {
                            cancel()
                            buildMovementLooper(target as ImageView)?.start()
                        }
                    }
                }
            }
            animator?.start()
        }
    }

    private fun buildMovementLooper(groundImageView: ImageView): ObjectAnimator? {
        val startX: Float = Tools.screenWidth.toFloat()
        val targetX: Float = Tools.globalMaxLeftX.toFloat()
        val movementAnimator: ObjectAnimator

        try {
            movementAnimator = ObjectAnimator.ofFloat(
                groundImageView,
                "x",
                startX,
                targetX
            ).apply {
                interpolator = LinearInterpolator()
                duration = gameSpeed
                repeatCount = ObjectAnimator.INFINITE
                addUpdateListener {
                    if (duration != gameSpeed) duration = gameSpeed
                    if (!isGameRunning()) pause()
                }
            }
        } catch (e: Exception) {
            return null
        }

        return movementAnimator
    }
}
