package com.jdauvergne.dinorun.cactus

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.jdauvergne.dinorun.DinoServiceInterface
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.Tools
import com.jdauvergne.dinorun.display.MainActivity
import com.jdauvergne.dinorun.display.MainActivity.Companion.gameSpeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class CactusSpawner(
    private val parent: ConstraintLayout,
    private val dino: Dinosaur,
    private val anchorView: View? = null
) : DinoServiceInterface {
    private val context = parent.context
    private val cactusFactory = CactusFactory(context)
    private var job: Job? = null
    private val activeGroupList: MutableList<CactusGroup> = mutableListOf()
    private var isPaused = false

    fun start() {
        job = CoroutineScope(Dispatchers.Main).launch {
            val lifecycleOwner = context as LifecycleOwner
            val minDelay = 1200L
            val maxDelay = 2000L
            var delay: Long
            delay(2000)
            while (isActive) {
                if (!isPaused) {
                    (cactusFactory.buildCactusGroup(CactusGroupsEnum.entries.random()) as CactusGroup).apply {
                        initialize(anchorView, Tools.screenWidth)
                        addSelfTo(parent)
                        startCollisionCheck(dino)
                        observeCactusGroup(lifecycleOwner)
                        startMovement()
                    }
                }
                delay = Random.nextLong(
                    (minDelay * gameSpeed / MainActivity.DEFAULT_SPEED),
                    (maxDelay * gameSpeed / MainActivity.DEFAULT_SPEED)
                )
                delay(delay)
            }
        }
    }

    override fun stop() {
        activeGroupList.forEach { cactusGroup ->
            cactusGroup.stopCollisionCheck()
        }
        job?.cancel()
        isPaused = true
    }

    override fun pause() {
        isPaused = true
        CoroutineScope(Dispatchers.Main).launch {
            activeGroupList.forEach {
                CoroutineScope(Dispatchers.Main).launch {
                    it.stopCollisionCheck()
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            activeGroupList.forEach {
                CoroutineScope(Dispatchers.Main).launch {
                    it.pauseMovement()
                }
            }
        }
    }

    override fun resume() {
        CoroutineScope(Dispatchers.Main).launch {
            activeGroupList.forEach { it.startCollisionCheck(dino) }
        }
        CoroutineScope(Dispatchers.Main).launch {
            activeGroupList.forEach { it.resumeMovement() }
        }
        isPaused = false
    }

    private fun CactusGroup.observeCactusGroup(lifecycleOwner: LifecycleOwner) {
        isMoving.observe(lifecycleOwner) { isActive ->
            if (isActive) activeGroupList.add(this) else activeGroupList.remove(this)
        }
    }
}
