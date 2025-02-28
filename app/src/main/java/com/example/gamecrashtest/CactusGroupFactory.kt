package com.example.gamecrashtest

import com.example.gamecrashtest.CactusSizesEnum.*
import androidx.constraintlayout.widget.ConstraintLayout

class CactusGroupFactory(
    private val parentLayout: ConstraintLayout,
    ) {
    fun buildCactus(enum: CactusSizesEnum, speed: Long = 1500L):Cactus{
        return Cactus(parentLayout, size = enum, speed = speed)
    }
    fun buildCactusGroup(
        groupsEnum: CactusGroupsEnum,
        speed: Long = 1500L,
        ): CactusGroup {
        return when (groupsEnum) {
            CactusGroupsEnum.SmlMedSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small, speed = speed),
                Cactus(parentLayout, size = Medium, speed = speed),
                Cactus(parentLayout, size = Small, speed = speed)
            ))

            CactusGroupsEnum.SmlSmlSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small, speed = speed),
                Cactus(parentLayout, size = Small, speed = speed),
                Cactus(parentLayout, size = Small, speed = speed)
            ))

            CactusGroupsEnum.SmlSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small, speed = speed),
                Cactus(parentLayout, size = Small, speed = speed)
            ))

            CactusGroupsEnum.MedMed -> CactusGroup(listOf(
                Cactus(parentLayout, size = Medium, speed = speed),
                Cactus(parentLayout, size = Medium, speed = speed)
            ))

            CactusGroupsEnum.Sml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small, speed = speed)
            ))

            CactusGroupsEnum.Med -> CactusGroup(listOf(
                Cactus(parentLayout, size = Medium, speed = speed)
            ))
        }
    }
}