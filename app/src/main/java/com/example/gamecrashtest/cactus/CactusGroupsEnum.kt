package com.example.gamecrashtest.cactus

import com.example.gamecrashtest.cactus.CactusSizesEnum.Medium
import com.example.gamecrashtest.cactus.CactusSizesEnum.Small

enum class CactusGroupsEnum(var groupList: List<CactusSizesEnum>) {
    SmlMedSml(
        listOf(
            Small,
            Medium,
            Small
        )
    ),
    SmlSmlSml(
        listOf(
            Small,
            Small,
            Small
        )
    ),
    SmlSml(
        listOf(
            Small,
            Small
        )
    ),
    MedMed(
        listOf(
            Medium,
            Medium
        )
    ),
    Sml(
        listOf(
            Small
        )
    ),
    Med(
        listOf(
            Medium
        )
    )
    ;
}