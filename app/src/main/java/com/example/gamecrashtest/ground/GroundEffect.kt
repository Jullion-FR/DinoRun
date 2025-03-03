package com.example.gamecrashtest.ground

import android.graphics.drawable.Drawable
import android.view.Choreographer
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.gamecrashtest.MainActivity
import com.example.gamecrashtest.R
import com.example.gamecrashtest.Tools

class GroundEffect(
    private val parentView: ConstraintLayout,
    drawableId: Int,
    private val layoutParams: ConstraintLayout.LayoutParams,
    private val speed: Int
) {
    private val screenWidth = Tools.screenWidth
    private val imageViews = mutableListOf<ImageView>()
    private var imageWidth = 0
    private var isScrolling = false
    private val choreographer = Choreographer.getInstance()

    // Mise en cache du Drawable
    private val cachedDrawable: Drawable? = ContextCompat.getDrawable(parentView.context, drawableId)

    private val viewPool = GroundViewPool(parentView, cachedDrawable, layoutParams)

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (!MainActivity.isGameRunning) {
                isScrolling = false
                choreographer.removeFrameCallback(this)
                return
            }

            for (imageView in imageViews) {
                imageView.x -= speed
            }

            val firstView = imageViews.first()
            if (firstView.x <= -imageWidth) {
                val lastView = imageViews.last()
                imageViews.removeAt(0)
                viewPool.recycleView(firstView)
                val newView = viewPool.getView()
                newView.x = lastView.x + imageWidth
                imageViews.add(newView)
            }

            choreographer.postFrameCallback(this)
        }
    }

    init {
        initialize()
    }

    private fun initialize() {
        ImageView(parentView.context).apply {
            setImageDrawable(cachedDrawable)
            layoutParams = this@GroundEffect.layoutParams
            parentView.addView(this)
            post {
                imageWidth = width
                parentView.removeView(this)
                setupInitialImages()
            }
        }
    }

    private fun setupInitialImages() {
        if (imageWidth == 0) return

        val numImages = ((screenWidth / imageWidth) + 3).toInt()
        repeat(numImages) { i ->
            val imageView = viewPool.getView()
            imageView.layoutParams = layoutParams.apply {
                width = imageWidth
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = R.id.groundView
            }
            imageView.x = i * imageWidth.toFloat() - imageWidth
            imageViews.add(imageView)
        }
    }

    fun start() {
        if (!isScrolling) {
            isScrolling = true
            choreographer.postFrameCallback(frameCallback)
        }
    }
}
