package com.example.gamecrashtest

import com.example.gamecrashtest.CactusSizesEnum.*
import androidx.constraintlayout.widget.ConstraintLayout

class CactusGroupFactory(
        val parentLayout: ConstraintLayout,
    ) {
    fun buildCactus():Cactus{
        return Cactus(parentLayout)
    }
    fun buildCactusGroup(cactusGroup: CactusGroupsEnum): CactusGroup {
        return when (cactusGroup) {
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