package com.example.gamecrashtest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.gamecrashtest.Tools.Companion.initScreenHeight
import com.example.gamecrashtest.Tools.Companion.initScreenWidth
import com.example.gamecrashtest.cactus.CactusGroup
import com.example.gamecrashtest.cactus.CactusGroupFactory
import com.example.gamecrashtest.cactus.CactusGroupsEnum
import com.example.gamecrashtest.cactus.CactusSizesEnum
import com.example.gamecrashtest.ground.GroundEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {
    companion object{
        var isGameRunning = false
    }

    private lateinit var mainView:ConstraintLayout
    private lateinit var groundView:View
    private lateinit var scoreTextView:TextView
    private lateinit var replayImageView: ImageView


    private lateinit var dino:Dinosaur

    private lateinit var score:Score
    private var isGameLaunched = false

    private lateinit var groundEffect: GroundEffect
    private lateinit var context:Context

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main


        context = this
        isGameLaunched = false
        isGameRunning = false


        hideSystemUI()  //deprecated
        initScreenWidth(this)
        initScreenHeight(this)
        CactusSizesEnum.entries.forEach { it.updateSizes(Tools.screenWidth) }
        setContentView(layout)

        scoreTextView = findViewById(R.id.scoreTextView)
        score = Score(scoreTextView)

        mainView = findViewById(R.id.mainView)
        groundView = findViewById(R.id.groundView)

        val params = ConstraintLayout.LayoutParams(1096, 34)
        groundEffect = GroundEffect(mainView, R.drawable.ground, params, speed = 30)

        dino = Dinosaur(
            dinoImageView = findViewById(R.id.dinoImageView),
            context = this
        )

        mainView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchScreenResponse()
            }
            true
        }

        replayImageView = findViewById(R.id.replayImageView)

        replayImageView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                println("Restart !")
                recreate()
            }
            true
        }

        test()
    }
    private fun test() {

    }


    private fun touchScreenResponse() {
        if (!isGameLaunched) {
            lifecycleScope.launch {
                launchSequence()
            }
        }
        else if(isGameRunning && !dino.isJumping){
            lifecycleScope.launch {
                dino.jump((Tools.screenHeight*0.4).toInt())
            }
        }
    }

    private suspend fun launchSequence() {
        isGameLaunched = true

        lifecycleScope.launch {
            dino.startSequence()
        }

        isGameRunning = true

        delay(1000)
        startGroundMovement()
        score.start()
        delay(1000)
        startCactusSpawner()
    }

    private fun startGroundMovement() {
        lifecycleScope.launch {
            groundEffect.start()
        }
    }

    private fun startCactusSpawner() {
        lifecycleScope.launch{
            val cactusGroupFactory = CactusGroupFactory(context, mainView)
            while (isActive && isGameRunning) {

                val randomCactusGroup = CactusGroupsEnum.entries.random()
                val cactusGroup: CactusGroup = cactusGroupFactory.buildCactusGroup(
                    randomCactusGroup,
                )

                cactusGroup.spawn(groundView)
                cactusGroup.startMoving(lifecycleScope)
                cactusGroup.startCollisionCheck(lifecycleScope, dino)
                delay(3000)
            }
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