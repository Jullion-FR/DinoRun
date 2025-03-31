package com.jdauvergne.dinorun.cactus

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.jdauvergne.dinorun.Dinosaur

/**
 * Defines the behavior and properties of a cactus in the game.
 * A cactus can be initialized, moved, paused, resumed, and checked for collisions.
 */
interface CactusInterface {

    /**
     * The horizontal position of the cactus.
     */
    var x: Float

    /**
     * The offset applied to the sprite for rendering adjustments.
     */
    var spriteOffset: Float

    /**
     * The width of the cactus.
     */
    val width: Int

    /**
     * A live data flag indicating whether the cactus is currently moving.
     */
    val isMoving: MutableLiveData<Boolean>

    /**
     * Initializes the cactus and sets its starting position.
     *
     * @param anchorView An optional view used for positioning (default is null).
     * @param xPos The initial X position of the cactus.
     */
    fun initialize(anchorView: View? = null, xPos: Float)

    /**
     * Starts the movement of the cactus from a given position to a target position.
     *
     * @param startX The optional starting X position (default is null).
     * @param targetX The optional target X position (default is null).
     */
    fun startMovement(startX: Float? = null, targetX: Float? = null)

    /**
     * Stops the movement of the cactus immediately.
     */
    fun stopMovement()

    /**
     * Pauses the movement of the cactus.
     */
    fun pauseMovement()

    /**
     * Resumes the movement of the cactus if it was paused.
     */
    fun resumeMovement()

    /**
     * Starts checking for collisions with the given dinosaur.
     *
     * @param dinosaur The dinosaur object to check for collisions.
     */
    fun startCollisionCheck(dinosaur: Dinosaur)

    /**
     * Stops collision detection.
     */
    fun stopCollisionCheck()

    /**
     * Adds the cactus to a given view group.
     *
     * @param viewGroup The parent view group where the cactus should be added.
     */
    fun addSelfTo(viewGroup: ViewGroup)

    /**
     * Removes the cactus from its parent view group.
     */
    fun dropSelfFromParent()
}
