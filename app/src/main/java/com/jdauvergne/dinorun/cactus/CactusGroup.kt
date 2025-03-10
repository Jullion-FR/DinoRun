package com.jdauvergne.dinorun.cactus

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.Tools
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class CactusGroup(
    private var cactusList: List<Cactus>
) {

    fun spawn(anchorView: View? = null) {
        var accumulatedOffset = 0f
        cactusList.forEach { cactus ->
            cactus.spawn(anchorView)
            cactus.spriteOffset = accumulatedOffset
            accumulatedOffset += cactus.size.width * 1.1f
        }
    }

    fun startMoving(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch {
            var start: Float
            var target: Float
            cactusList.forEach { cactus ->
                start = cactus.x + cactus.spriteOffset
                target = Tools.globalMaxLeftX + cactus.spriteOffset
                cactus.startMoving(start, target)
            }
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