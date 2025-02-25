package com.example.gamecrashtest

import com.example.gamecrashtest.Tools.Companion.dpToPx

enum class CactusSizesEnum(val width: Int, val height: Int, val spriteIdList: List<Int>) {
    Small(40.dpToPx,75.dpToPx,
        listOf(
            R.drawable.cactus_small1,
            R.drawable.cactus_small2,
            R.drawable.cactus_small3
        )
    ),
    Medium(50.dpToPx,85.dpToPx,
        listOf(
            R.drawable.cactus_medium1,
            R.drawable.cactus_medium2
        ));
}