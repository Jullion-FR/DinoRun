package com.jdauvergne.dinorun.cactus

import com.jdauvergne.dinorun.R
/**
 * Enum representing different cactus sizes in the game.
 * Each size has a list of associated sprite resources and dynamically calculated dimensions.
 *
 * @property spriteIdList List of drawable resource IDs for the cactus sprites.
 */
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
                width = (screenWidth * (0.4 / 8)).toInt()
                // Height / Width of picture
                (width * (100f / 50f)).toInt()
            }

            Medium -> {
                width = (screenWidth * (0.5 / 8)).toInt()
                //
                (width * (100f / 55f)).toInt()
            }
        }
    }
}

