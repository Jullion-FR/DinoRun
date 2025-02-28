package com.example.gamecrashtest

import com.example.gamecrashtest.CactusSizesEnum.*
import androidx.constraintlayout.widget.ConstraintLayout

class CactusGroupFactory(
    private val parentLayout: ConstraintLayout,
    ) {

    fun buildCactusGroup(
        groupsEnum: CactusGroupsEnum,
        ): CactusGroup {
        return when (groupsEnum) {
            CactusGroupsEnum.SmlMedSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small),
                Cactus(parentLayout, size = Medium),
                Cactus(parentLayout, size = Small)
            ))

            CactusGroupsEnum.SmlSmlSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small),
                Cactus(parentLayout, size = Small),
                Cactus(parentLayout, size = Small)
            ))

            CactusGroupsEnum.SmlSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small),
                Cactus(parentLayout, size = Small)
            ))

            CactusGroupsEnum.MedMed -> CactusGroup(listOf(
                Cactus(parentLayout, size = Medium),
                Cactus(parentLayout, size = Medium)
            ))

            CactusGroupsEnum.Sml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small)
            ))

            CactusGroupsEnum.Med -> CactusGroup(listOf(
                Cactus(parentLayout, size = Medium)
            ))
        }
    }
}