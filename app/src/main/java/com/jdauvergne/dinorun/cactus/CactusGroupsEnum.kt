package com.jdauvergne.dinorun.cactus

import com.jdauvergne.dinorun.cactus.CactusSizesEnum.Medium
import com.jdauvergne.dinorun.cactus.CactusSizesEnum.Small
/**
 * Represents predefined groups of cacti, each containing a specific sequence of cactus sizes.
 * The groups are used to define different formations of cacti in the game.
 *
 * @property groupList A list of cactus sizes that define the composition of the group.
 */
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