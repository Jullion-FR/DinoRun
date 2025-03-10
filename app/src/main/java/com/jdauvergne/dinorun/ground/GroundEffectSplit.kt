package com.jdauvergne.dinorun.ground

import android.widget.ImageView

class GroundEffectSplit(
    val groundEffectImageView: ImageView
) {
    var spriteOffset = 0f
    var x: Float
        get() = groundEffectImageView.x
        set(value) {
            groundEffectImageView.x = value
        }
}