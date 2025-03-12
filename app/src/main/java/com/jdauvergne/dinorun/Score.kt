package com.jdauvergne.dinorun

import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Score(
    private val scoreTextView: TextView
) {
    private val DEFAULT_SCORE = 0
    private var score: Int = DEFAULT_SCORE
    private var job: Job? = null

    init {
        initScore()
    }

    private fun initScore() {
        score = DEFAULT_SCORE
        updateTextView()
    }

    private fun incrementScore() {
        score += 5
        updateTextView()
    }

    private fun updateTextView() {
        scoreTextView.text = "$score".padStart(5, '0')
    }

    fun start() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            while (job?.isActive == true) {
                incrementScore()
                if (score % 500 == 0) {
                    MainActivity.gameSpeed -= 50
                    println("Time to speed up ! : ${MainActivity.gameSpeed}")
                }
                delay(100)
            }
        }
    }
    fun stop(){
        job?.cancel()
    }
}