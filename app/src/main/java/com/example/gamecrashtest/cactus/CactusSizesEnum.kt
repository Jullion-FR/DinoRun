package com.example.gamecrashtest.cactus

import com.example.gamecrashtest.R
import com.example.gamecrashtest.Tools

enum class CactusSizesEnum(val width: Float, val height: Float, val spriteIdList: List<Int>) {
    Small(
        //(100 / 50) = small image dimensions (height, width)
        // 0.7 is hardcoded -> 0.7/8 of total height
        (Tools.screenHeight * 0.8 / 8).toFloat(),
        (Tools.screenHeight * 0.8 / 8 * (100 / 50)).toFloat(),
        listOf(
            R.drawable.cactus_small1,
            R.drawable.cactus_small2,
            R.drawable.cactus_small3
        )
    ),

    Medium(
        //(100 / 55) = medium image dimensions (height, width)
        (Tools.screenHeight * 1.1 / 8).toFloat(),
        (Tools.screenHeight * 1.1 / 8 * (100 / 55)).toFloat(),
        listOf(
            R.drawable.cactus_medium1,
            R.drawable.cactus_medium2
        )
    );
}
