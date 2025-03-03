package com.example.gamecrashtest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.gamecrashtest.Tools.Companion.initScreenWidth
import com.example.gamecrashtest.cactus.CactusGroup
import com.example.gamecrashtest.cactus.CactusGroupFactory
import com.example.gamecrashtest.cactus.CactusGroupsEnum
import com.example.gamecrashtest.ground.GroundEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    companion object{
        var isGameRunning = false
    }

    private lateinit var mainView:ConstraintLayout
    private lateinit var groundView:View
    private lateinit var scoreTextView:TextView


    private lateinit var dino:Dinosaur
    private var score by Delegates.notNull<Int>()

    private var isGameLaunched by Delegates.notNull<Boolean>()
    private lateinit var groundEffect: GroundEffect
    private lateinit var context:Context

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main

        val DEFAULT_SCORE = 0

        context = this
        isGameLaunched = false
        isGameRunning = false
        score = DEFAULT_SCORE

        setContentView(layout)

        hideSystemUI()  //deprecated
        initScreenWidth(this)

        mainView = findViewById(R.id.mainView)
        groundView = findViewById(R.id.groundView)

        val params = ConstraintLayout.LayoutParams(1096, 34)
        groundEffect = GroundEffect(mainView, R.drawable.ground, params, speed = 35)

        dino = Dinosaur(
            dinoImageView = findViewById(R.id.dinoImageView),
            context = this
        )

        scoreTextView = findViewById(R.id.scoreTextView)
        scoreTextView.text = "$DEFAULT_SCORE"

        mainView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchScreenResponse()
            }
            true
        }
    }

    private fun touchScreenResponse() {
        if (!isGameLaunched) {
            launchSequence()
        }
        else if(isGameRunning && !dino.isJumping){
            lifecycleScope.launch {
                dino.jump()
                addScore(1)
            }
        }
    }

    private fun launchSequence() {
        isGameLaunched = true

        lifecycleScope.launch {
            dino.startSequence()
        }
        startGroundMovement()
        startCactusSpawner()

        isGameRunning = true
    }

    private fun startGroundMovement() {
        lifecycleScope.launch {
            delay(1000)
            groundEffect.start()
        }
    }

    private fun startCactusSpawner() {
        lifecycleScope.launch{
            delay(2000)
            val cactusGroupFactory = CactusGroupFactory(context, mainView)
            while (isActive && isGameRunning) {

                //entries.random()
                val randomCactusGroup = CactusGroupsEnum.entries.random()
                val cactusGroup: CactusGroup = cactusGroupFactory.buildCactusGroup(
                    randomCactusGroup,
                )

                cactusGroup.spawn()
                cactusGroup.startMoving(lifecycleScope)
                cactusGroup.startCollisionCheck(lifecycleScope, dino)
                delay(3000)
            }
        }
    }

    private fun addScore(scoreToAdd: Int) {
        if (scoreToAdd == 0) return
        score += scoreToAdd
        scoreTextView.post {
            scoreTextView.text = "$score"
        }
    }

    //TEMP and bad
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }


}