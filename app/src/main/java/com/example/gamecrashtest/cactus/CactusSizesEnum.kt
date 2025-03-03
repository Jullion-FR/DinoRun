package com.example.gamecrashtest.cactus

import com.example.gamecrashtest.R
import com.example.gamecrashtest.Tools

enum class CactusSizesEnum(val width: Float, val height: Float, val spriteIdList: List<Int>) {

    Small(
        //(50 / 100) = small image dimensions (width, height)
        // 0.x is hardcoded -> 0.x/8 of total screen width
        (Tools.screenWidth * 0.35 / 8 ).toFloat(),
        (Tools.screenWidth * 0.35 / 8 * (50 / 100)).toFloat(),
        listOf(
            R.drawable.cactus_small1,
            R.drawable.cactus_small2,
            R.drawable.cactus_small3
        )
    ),

    Medium(
        //(55 / 100) = medium image dimensions (width, height)
        (Tools.screenWidth * 0.45 / 8 ).toFloat(),
        (Tools.screenWidth * 0.45 / 8 * (55 / 100)).toFloat(),
        listOf(
            R.drawable.cactus_medium1,
            R.drawable.cactus_medium2
        )
    );
}
