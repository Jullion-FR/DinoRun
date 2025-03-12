package com.jdauvergne.dinorun.cactus

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.jdauvergne.dinorun.cactus.CactusSizesEnum.Medium
import com.jdauvergne.dinorun.cactus.CactusSizesEnum.Small

class CactusFactory(
    private val context: Context,
) {
    private val cachedSpritesSmall = drawableListBySize(Small)
    private val cachedSpritesMedium = drawableListBySize(Medium)


    fun buildCactusGroup(
        groupsEnum: CactusGroupsEnum,
    ): CactusInterface {
        val cactusList = mutableListOf<CactusInterface>()

        groupsEnum.groupList.forEach { size ->
            cactusList.add(buildCactus(size))
        }

        return CactusGroup(context, cactusList)
    }


    fun buildCactus(size: CactusSizesEnum): CactusInterface {
        return when(size){
            Small -> Cactus(spritedImageView(Small), size = Small)
            Medium -> Cactus(spritedImageView(Medium), size = Medium)
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