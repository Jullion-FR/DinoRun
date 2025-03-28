package com.jdauvergne.dinorun.display

import ShakeDetector
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.ScoreManager
import com.jdauvergne.dinorun.Tools
import com.jdauvergne.dinorun.Tools.Companion.hideSystemUI
import com.jdauvergne.dinorun.Tools.Companion.initScreenHeight
import com.jdauvergne.dinorun.Tools.Companion.initScreenWidth
import com.jdauvergne.dinorun.cactus.CactusSizesEnum
import com.jdauvergne.dinorun.cactus.CactusSpawner
import com.jdauvergne.dinorun.display.dialogs.ReplayDialog
import com.jdauvergne.dinorun.ground.GroundEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object {
        val DEFAULT_SPEED = 2000L //The lower the faster
        var gameSpeed: Long = DEFAULT_SPEED

        private val isGameRunning = MutableLiveData<Boolean?>()


        fun isGameRunning(): Boolean {
            return isGameRunning.value ?: false
        }
        fun gameOver(){
            isGameRunning.value = false
        }
    }

    private lateinit var mainView: ConstraintLayout
    private lateinit var groundView: View


    private lateinit var dino: Dinosaur
    private lateinit var cactusSpawner: CactusSpawner
    private lateinit var groundEffect: GroundEffect
    private lateinit var shakeDetector: ShakeDetector

    private lateinit var scoreManager: ScoreManager
    private var isGameLaunched = false

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        hideSystemUI(window)  //deprecated if API<30
        setContentView(layout)

        initScreenWidth(this)
        initScreenHeight(this)
        CactusSizesEnum.entries.forEach { it.updateSizes(Tools.screenWidth) }


        val fargroundView = findViewById<View>(R.id.fargroundView)
        fargroundView.layoutParams.apply {
            width = (Tools.screenWidth * 1.1f).toInt()
        }

        context = this
        isGameLaunched = false
        isGameRunning.value = null

        gameSpeed = DEFAULT_SPEED

        mainView = findViewById(R.id.mainView)
        cactusSpawner = CactusSpawner(mainView)

        groundView = findViewById(R.id.groundView)

        scoreManager = ScoreManager(
            context,
            findViewById(R.id.scoreTextView),
            findViewById(R.id.highScoreTextView)
        )
        scoreManager.resetScore()

        dino = Dinosaur(context, findViewById(R.id.dinoImageView))
        val ground_R_IDs = intArrayOf(R.drawable.ground, R.drawable.ground_white)
        groundEffect = GroundEffect(mainView, ground_R_IDs[1])

        shakeDetector = ShakeDetector(context) { dino.jump() }


        initListeners()

    }
    override fun onResume() {
        super.onResume()
        hideSystemUI(window)
        if (isGameRunning()) {
            startAll()
        }
    }
    override fun onPause() {
        super.onPause()
        stopAll()
    }
    override fun onDestroy() {
        super.onDestroy()
        stopAll()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        mainView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchScreenResponse()
            }
            true
        }
        isGameRunning.observe(this as LifecycleOwner){ isRunning ->
            if (isRunning != null && isRunning == false)  onGameOver()
        }
    }

    private fun touchScreenResponse() {
        if (!isGameLaunched) {
            lifecycleScope.launch {
                launchSequence()
            }
        } else if (isGameRunning()) {
            mainView.post {
                dino.jump((Tools.screenHeight * 0.4f))
            }
        }
    }

    private fun launchSequence() {
        isGameLaunched = true
        isGameRunning.value = true
        startAll()
    }

    private fun stopAll(){
        shakeDetector.stopListening()
        scoreManager.stop()
        cactusSpawner.stop()
    }
    private fun startAll(){
        mainView.post {
            dino.startSequence()
        }

        startGroundMovement()

        shakeDetector.startListening()
        scoreManager.start()

        cactusSpawner.start(dino, groundView)

    }
    private fun onGameOver() {
        isGameRunning.value = null
        stopAll()
        window?.decorView?.post {
            showReplay()
        }
    }

    private fun startGroundMovement() {
        lifecycleScope.launch {
            delay(1075)
            groundEffect.startFirstMovementLoop()
        }
    }

    private fun showReplay() {
        mainView.post {
            val replayDialog = ReplayDialog(context, scoreManager.score, if(scoreManager.score == scoreManager.highScore) true else false) {
                recreate()
            }
            replayDialog.show()
        }
    }
}