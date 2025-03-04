package com.jdauvergne.dinorun.cactus

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jdauvergne.dinorun.cactus.CactusSizesEnum.Medium
import com.jdauvergne.dinorun.cactus.CactusSizesEnum.Small

class CactusGroupFactory(
    private val context: Context,
    private val parentLayout: ConstraintLayout
) {
    private val cachedSpritesSmall = drawableListBySize(Small)
    private val cachedSpritesMedium = drawableListBySize(Medium)


    fun buildCactusGroup(
        groupsEnum: CactusGroupsEnum,
    ): CactusGroup {
        val cactusList = mutableListOf<Cactus>()

        groupsEnum.groupList.forEach { size ->
            cactusList.add(buildCactus(size))
        }

        return CactusGroup(cactusList)
    }


    private fun buildCactus(size: CactusSizesEnum): Cactus {
        return when(size){
            Small -> Cactus(parentLayout, size = Small, spritedImageView(Small))
            Medium -> Cactus(parentLayout, size = Medium, spritedImageView(Medium))
        }
    }

    private fun drawableListBySize(sizesEnum: CactusSizesEnum): List<Drawable> {
        return sizesEnum.spriteIdList.mapNotNull { spriteId ->
            ContextCompat.getDrawable(context, spriteId)
        }
    }

    private fun spritedImageView(sizesEnum: CactusSizesEnum): ImageView {
        val imageView = ImageView(context)
        when (sizesEnum) {
            Small -> imageView.setImageDrawable(cachedSpritesSmall.random())
            Medium -> imageView.setImageDrawable(cachedSpritesMedium.random())
        }
        //imageView.setBackgroundColor(Color.CYAN)
        return imageView
    }
}