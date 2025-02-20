package com.example.gamecrashtest

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd


class MainActivity : AppCompatActivity() {

    private lateinit var mainView:View
    private lateinit var groundView:View
    private lateinit var dinoImageView:ImageView
    private lateinit var scoreTextView:TextView

    private var isJumping:Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val DEFAULT_SCORE = 0


        mainView = findViewById(R.id.mainView)
        groundView = findViewById(R.id.groundView)
        dinoImageView = findViewById(R.id.dinoImageView)

        scoreTextView = findViewById(R.id.scoreTextView)
        scoreTextView.text = "$DEFAULT_SCORE"

        mainView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && !isJumping) {
                isJumping = true
                dinoJump(325)
                addScore(1)
            }
            true
        }


    }
    private fun addScore(scoreToAdd:Int){
        val scoreRender = scoreTextView
        var score = (scoreRender.text).toString().toInt()
        score += scoreToAdd
        scoreRender.text = "$score"
    }

    private fun dinoJump(height: Int) {
        val baseDinoY = dinoImageView.y

        val animUp = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY, baseDinoY - height)
        val animDown = ObjectAnimator.ofFloat(dinoImageView, "y", baseDinoY - height, baseDinoY)

        val duration: Long = 300
        animUp.duration = duration
        animDown.duration = duration

        animUp.start()
        animUp.doOnEnd {
            animDown.start()
            animDown.doOnEnd {
                isJumping = false
            }
        }
    }
}