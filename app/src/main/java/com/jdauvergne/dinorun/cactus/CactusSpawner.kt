package com.jdauvergne.dinorun.cactus

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.Tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class CactusSpawner(
    private val parent: ConstraintLayout
) {
    private val context = parent.context
    private val cactusFactory = CactusFactory(context)
    private var job: Job? = null
    private val activeGroupList: MutableList<CactusGroup> = mutableListOf()

    fun start(dino: Dinosaur, anchorView: View?) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val lifecycleOwner = context as LifecycleOwner
            delay(1000)
            while (isActive) {
                val cactusGroup = cactusFactory.buildCactusGroup(CactusGroupsEnum.entries.random())

                (cactusGroup as CactusGroup).apply {
                    initialize(anchorView, Tools.screenWidth)
                    addSelfTo(parent)
                    startCollisionCheck(dino)
                    observeCactusGroup(lifecycleOwner)
                    startMovement()
                }
                delay(Random.nextLong(1500, 2500))
            }
        }
    }

    private fun CactusGroup.observeCactusGroup(lifecycleOwner: LifecycleOwner) {
        isMoving.observe(lifecycleOwner) { isActive ->
            if (isActive) activeGroupList.add(this) else activeGroupList.remove(this)
        }
    }

    fun stop(){
        activeGroupList.forEach { cactusGroup ->
            cactusGroup.stopCollisionCheck()
        }
        job?.cancel()
    }
}
