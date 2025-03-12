package com.jdauvergne.dinorun.cactus

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.Tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CactusGroup(
    private val context: Context,
    private var cactusList: List<CactusInterface>
) : CactusInterface {
    override val isMoving = MutableLiveData<Boolean>()
    override val width = cactusList.sumOf { it.width }
    override var x: Float
        get() = cactusList.first().x
        set(value) {
            forEachCactus { it.x = value + it.spriteOffset + this.spriteOffset }
        }
    override var spriteOffset: Float = 0f

    override fun initialize(anchorView: View?, xPos: Float) {
        var accumulatedOffset = 0f
        forEachCactus{
            it.spriteOffset = accumulatedOffset
            it.initialize(anchorView, xPos)
            accumulatedOffset += it.width //* 1.1f
        }
    }

    private inline fun forEachCactus(action: (CactusInterface) -> Unit) {
        cactusList.forEach(action)
    }

    override fun startMovement(startX: Float?, targetX: Float?) {
        //args only for Interface, idk how to make it clean
        isMoving.value = true
        CoroutineScope(Dispatchers.Main).launch {
            val lifecycleOwner = context as LifecycleOwner
            forEachCactus {
                it.observeCactus(lifecycleOwner)
                it.startMovement(
                    startX = it.x,
                    targetX = Tools.globalMaxLeftX + it.spriteOffset
                )
            }
        }
    }

    override fun stopMovement() {
        forEachCactus { it.stopMovement() }
    }

    override fun startCollisionCheck(dinosaur: Dinosaur) {
        forEachCactus { it.startCollisionCheck(dinosaur) }
    }

    override fun stopCollisionCheck() {
        forEachCactus { it.stopCollisionCheck() }
    }

    override fun addSelfTo(viewGroup: ViewGroup) {
        forEachCactus { it.addSelfTo(viewGroup) }
    }

    override fun dropSelfFromParent() {
        forEachCactus { it.dropSelfFromParent() }
    }

    private fun CactusInterface.observeCactus(lifecycleOwner: LifecycleOwner) {
        isMoving.observe(lifecycleOwner) { isActive ->
            if (!isActive){
                this@CactusGroup.isMoving.value = false
                this@CactusGroup.stopCollisionCheck()
                this@CactusGroup.dropSelfFromParent()
            }
        }
    }
}