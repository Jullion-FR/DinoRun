package com.example.gamecrashtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.gamecrashtest.Tools.Companion.initScreenWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object{
        var isGameRunning = false
    }

    private lateinit var mainView:ConstraintLayout
    private lateinit var groundView:View
    private lateinit var scoreTextView:TextView


    private lateinit var dino:Dinosaur
    private var score = 0

    private var isGameLaunched = false
    private lateinit var groundEffect: GroundEffect

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main
        setContentView(layout)

        hideSystemUI()  //TEMP and deprecated
        initScreenWidth(this)

        mainView = findViewById(R.id.mainView)
        groundView = findViewById(R.id.groundView)

        val params = ConstraintLayout.LayoutParams(1096, 34)
        groundEffect = GroundEffect(mainView, R.drawable.ground, params, speed = 35)

        dino = Dinosaur(findViewById(R.id.dinoImageView))

        val DEFAULT_SCORE = 0
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
        if(!isGameRunning && isGameLaunched) return

        if (!isGameLaunched) {
            lauchSequence()
        }
        else {
            lifecycleScope.launch {
                dino.touchScreenResponse()
                addScore(1)
            }
        }
    }

    private fun lauchSequence() {
        isGameLaunched = true

        lifecycleScope.launch {
            dino.dinoStartingAnimation()
            dino.startSpriteCycle()
        }

        startGroundMovement()
        cactusSpawner()
        isGameRunning = true
    }

    private fun startGroundMovement() {
        lifecycleScope.launch {
            delay(1000)
            groundEffect.start()
        }
    }

    private fun cactusSpawner() {
        lifecycleScope.launch{
            delay(2000)
            val cactusGroupFactory = CactusGroupFactory(mainView)
            while (isActive && isGameRunning) {

                //entries.random()
                val randomCactusGroup = CactusGroupsEnum.entries.random()
                val cactusGroup = cactusGroupFactory.buildCactusGroup(
                    randomCactusGroup,
                    1800L
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