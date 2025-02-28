package com.example.gamecrashtest

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.gamecrashtest.Tools.Companion.dpToPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class CactusGroup(
    private var cactusList: List<Cactus>
) {

    fun spawn() {
        var accumulatedOffset = 0f

        cactusList.forEach { cactus ->
            cactus.spawn()
            cactus.spriteOffset = accumulatedOffset
            accumulatedOffset += cactus.size.width.dpToPx.toFloat() / 3
        }
    }

    fun startMoving(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch(Dispatchers.Main) {
            val animationDuration = Cactus.speed
            var start:Float
            var target:Float
            cactusList.forEach { cactus ->
                start = cactus.x + cactus.spriteOffset
                target = -Tools.screenWidth/6 + cactus.spriteOffset
                cactus.startMoving(start, target)
            }
            delay(animationDuration)
        }
    }

    fun startCollisionCheck(lifecycleScope: LifecycleCoroutineScope, dinosaur: Dinosaur) {
        lifecycleScope.launch {
            cactusList.asFlow().collect { cactus ->
                launch {
                    cactus.startCollisionCheck(lifecycleScope, dinosaur)
                }
            }
        }
    }
}