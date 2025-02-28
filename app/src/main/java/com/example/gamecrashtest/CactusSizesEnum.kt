package com.example.gamecrashtest

import com.example.gamecrashtest.Tools.Companion.dpToPx

enum class CactusSizesEnum(widthDp: Int, heightDp: Int, val spriteIdList: List<Int>) {
    Small(40, 75,
        listOf(
            R.drawable.cactus_small1,
            R.drawable.cactus_small2,
            R.drawable.cactus_small3
        )),
    Medium(50, 85,
        listOf(
            R.drawable.cactus_medium1,
            R.drawable.cactus_medium2
        ));

    val width = widthDp.dpToPx
    val height = heightDp.dpToPx
}