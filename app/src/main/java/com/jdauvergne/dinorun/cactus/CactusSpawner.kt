package com.jdauvergne.dinorun.cactus

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class CactusSpawner(
    context: Context,
    mainView: ConstraintLayout,
) {

    private val cactusGroupFactory = CactusGroupFactory(context, mainView)

    fun start(
        lifecycleScope: androidx.lifecycle.LifecycleCoroutineScope,
        dino: Dinosaur,
        anchorView: View?
    ) {
        lifecycleScope.launch {
            while (isActive && MainActivity.isGameRunning) {
                val randomCactusGroup = CactusGroupsEnum.entries.random()
                val cactusGroup: CactusGroup =
                    cactusGroupFactory.buildCactusGroup(randomCactusGroup)

                cactusGroup.spawn(anchorView)
                cactusGroup.startMoving(lifecycleScope)
                cactusGroup.startCollisionCheck(lifecycleScope, dino)
                delay(Random.nextLong(2000, 3500))
            }
        }
    }
}
