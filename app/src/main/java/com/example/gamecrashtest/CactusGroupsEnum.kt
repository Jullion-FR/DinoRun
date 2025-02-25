package com.example.gamecrashtest
import com.example.gamecrashtest.CactusSizesEnum.*
enum class CactusGroupsEnum(var groupList: List<CactusSizesEnum>) {
    SmlMedSml(listOf(
        Small,
        Medium,
        Small
    )),
    SmlSmlSml(listOf(
        Small,
        Small,
        Small
    )),
    SmlSml(listOf(
        Small,
        Small
    )),
    MedMed(listOf(
        Medium,
        Medium
    )),
    Sml(listOf(
        Small
    )),
    Med(listOf(
        Medium
    ))
    ;
}