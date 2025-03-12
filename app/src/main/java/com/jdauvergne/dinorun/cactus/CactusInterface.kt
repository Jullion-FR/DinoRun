package com.jdauvergne.dinorun.cactus

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.Tools

interface CactusInterface {
    var x: Float
    var spriteOffset: Float
    val width:Int
    val isMoving: MutableLiveData<Boolean>
    fun initialize(anchorView: View? = null, xPos: Float)
    fun startMovement(startX: Float? = null, targetX: Float? = null)
    fun stopMovement()
    fun startCollisionCheck(dinosaur: Dinosaur)
    fun stopCollisionCheck()
    fun addSelfTo(viewGroup: ViewGroup)
    fun dropSelfFromParent()
}