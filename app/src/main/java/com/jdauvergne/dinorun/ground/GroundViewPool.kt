package com.jdauvergne.dinorun.ground

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

class GroundViewPool(
    private val parentView: ConstraintLayout,
    private val cachedDrawable: Drawable?,
    private val layoutParams: ConstraintLayout.LayoutParams
) {
    private val pool = mutableListOf<ImageView>()

    fun getView(): ImageView {
        return if (pool.isNotEmpty()) {
            pool.removeAt(0)
        } else {
            createNewView()
        }
    }

    fun recycleView(view: ImageView) {
        view.x = 0f
        pool.add(view)
    }

    private fun createNewView(): ImageView {
        return ImageView(parentView.context).apply {
            layoutParams = this@GroundViewPool.layoutParams
            setImageDrawable(cachedDrawable)
            parentView.addView(this)
        }
    }
}
