package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var mainView:ConstraintLayout
    private lateinit var groundView:View
    private lateinit var scoreTextView:TextView

    private lateinit var dino:Dinosaur
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())

    var isGameLaunched = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main
        setContentView(layout)
        hideSystemUI()  //TEMP and bad

        mainView = findViewById(R.id.mainView)
        groundView = findViewById(R.id.groundView)

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

    private fun touchScreenResponse(){

        lifecycleScope.launch{
            if (!isGameLaunched){
                lifecycleScope.launch {
                    dino.startGameAnimation()
                }
                isGameLaunched = true
                lifecycleScope.launch{
                    generateCacties()
                }
            }else{
                dino.touchScreenResponse()
                addScore(1)
            }
        }
    }

    private fun generateCacties() {
        addCactus()
    }

    private fun addCactus() {
        val cactus = Cactus(this)
        mainView.addView(cactus.cactusImageView)
        cactus.move()
        //todo change this looping method and move to Cactus class
        handler.postDelayed({ addCactus() }, 2000)
    }

    private fun addScore(scoreToAdd:Int){
        val scoreRender = scoreTextView
        score += scoreToAdd
        scoreRender.text = "$score"
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