package com.example.gamecrashtest.cactus

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.example.gamecrashtest.cactus.CactusSizesEnum.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.gamecrashtest.R

class CactusGroupFactory(
    private val context: Context,
    private val parentLayout: ConstraintLayout
    ) {
    private val cachedSpritesSmall = drawableListBySize(Small)
    private val cachedSpritesMedium = drawableListBySize(Medium)


    fun buildCactusGroup(
        groupsEnum: CactusGroupsEnum,
        ): CactusGroup {
        return when (groupsEnum) {
            CactusGroupsEnum.SmlMedSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small, spritedImageView(Small)),
                Cactus(parentLayout, size = Medium, spritedImageView(Medium)),
                Cactus(parentLayout, size = Small, spritedImageView(Small))
            ))

            CactusGroupsEnum.SmlSmlSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small, spritedImageView(Small)),
                Cactus(parentLayout, size = Small, spritedImageView(Small)),
                Cactus(parentLayout, size = Small, spritedImageView(Small))
            ))

            CactusGroupsEnum.SmlSml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small, spritedImageView(Small)),
                Cactus(parentLayout, size = Small, spritedImageView(Small))
            ))

            CactusGroupsEnum.MedMed -> CactusGroup(listOf(
                Cactus(parentLayout, size = Medium, spritedImageView(Medium)),
                Cactus(parentLayout, size = Medium, spritedImageView(Medium))
            ))

            CactusGroupsEnum.Sml -> CactusGroup(listOf(
                Cactus(parentLayout, size = Small, spritedImageView(Small))
            ))

            CactusGroupsEnum.Med -> CactusGroup(listOf(
                Cactus(parentLayout, size = Medium, spritedImageView(Medium))
            ))
        }
    }

    private fun drawableListBySize(sizesEnum: CactusSizesEnum): List<Drawable> {
        return when (sizesEnum) {
            CactusSizesEnum.Small -> listOf(
                ContextCompat.getDrawable(context, R.drawable.cactus_small1)!!,
                ContextCompat.getDrawable(context, R.drawable.cactus_small2)!!,
                ContextCompat.getDrawable(context, R.drawable.cactus_small3)!!
            )

            CactusSizesEnum.Medium -> listOf(
                ContextCompat.getDrawable(context, R.drawable.cactus_medium1)!!,
                ContextCompat.getDrawable(context, R.drawable.cactus_medium2)!!,
            )
        }
    }

    private fun spritedImageView(sizesEnum: CactusSizesEnum): ImageView {
        val imageView = ImageView(context)
        when(sizesEnum){
            Small -> imageView.setImageDrawable(cachedSpritesSmall.random())
            Medium -> imageView.setImageDrawable(cachedSpritesMedium.random())
        }
        return imageView
    }
}