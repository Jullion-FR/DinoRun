package com.example.gamecrashtest.cactus

import com.example.gamecrashtest.R

enum class CactusSizesEnum(val spriteIdList: List<Int>) {
    Small(
        listOf(
            R.drawable.cactus_small1,
            R.drawable.cactus_small2,
            R.drawable.cactus_small3
        )
    ),
    Medium(
        listOf(
            R.drawable.cactus_medium1,
            R.drawable.cactus_medium2,
        )
    );

    var width: Int = 0
        private set
    var height: Int = 0
        private set

    fun updateSizes(screenWidth: Float) {
        height = when (this) {
            Small -> {
                width = (screenWidth * (0.4/8)).toInt()
                // Height / Width of picture
                (width * (100f / 50f)).toInt()
            }
            Medium -> {
                width = (screenWidth * (0.5/8)).toInt()
                //
                (width * (100f / 55f)).toInt()
            }
        }
    }
}

