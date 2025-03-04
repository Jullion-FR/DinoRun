package com.jdauvergne.dinorun.cactus

import com.jdauvergne.dinorun.cactus.CactusSizesEnum.Medium
import com.jdauvergne.dinorun.cactus.CactusSizesEnum.Small

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