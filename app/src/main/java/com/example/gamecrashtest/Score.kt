package com.example.gamecrashtest

import android.widget.TextView
import com.example.gamecrashtest.cactus.Cactus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Score(
    private val scoreTextView: TextView
) {
    private val DEFAULT_SCORE = 0
    var score:Int = DEFAULT_SCORE
        private set
    var loopJob:Job? = null

    init {
        initScore()
    }

    private fun initScore() {
        score = DEFAULT_SCORE
        updateTextView()
    }

    private fun addScore(int: Int){
        score += int
        updateTextView()
    }

    private fun updateTextView() {
        scoreTextView.text = "$score".padStart(5,'0')
    }

    fun start(){
        loopJob?.cancel()
        loopJob = CoroutineScope(Dispatchers.Main).launch {
            while (MainActivity.isGameRunning){
                addScore(5)
                if (score%500 == 0){
                    Cactus.speed -= 50
                    println("Time to speed up ! : ${Cactus.speed}")
                }
                delay(100)
            }
        }
    }
    fun stop(){
        loopJob?.cancel()
    }
}