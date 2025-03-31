package com.jdauvergne.dinorun.display

import com.jdauvergne.dinorun.sensors.ShakeDetector
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.jdauvergne.dinorun.DinoServiceInterface
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.music.MusicPlayer
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.ScoreManager
import com.jdauvergne.dinorun.Tools
import com.jdauvergne.dinorun.Tools.Companion.hideSystemUI
import com.jdauvergne.dinorun.Tools.Companion.initScreenHeight
import com.jdauvergne.dinorun.Tools.Companion.initScreenWidth
import com.jdauvergne.dinorun.cactus.CactusSizesEnum
import com.jdauvergne.dinorun.cactus.CactusSpawner
import com.jdauvergne.dinorun.display.dialogs.OptionsDialog
import com.jdauvergne.dinorun.display.dialogs.ReplayDialog
import com.jdauvergne.dinorun.ground.GroundEffect
import com.jdauvergne.dinorun.music.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object {
        const val DEFAULT_SPEED = 2000L //The lower the faster
        var gameSpeed: Long = DEFAULT_SPEED

        private val isGameRunning = MutableLiveData<Boolean?>()


        fun isGameRunning(): Boolean {
            return isGameRunning.value ?: false
        }
        fun gameOver(){
            isGameRunning.value = false
        }
    }

    private lateinit var parentLayout: ConstraintLayout
    private lateinit var groundView: View
    private lateinit var startTextView: TextView
    private lateinit var pauseImageView: ImageView


    private lateinit var dino: Dinosaur
    private lateinit var cactusSpawner: CactusSpawner
    private lateinit var groundEffect: GroundEffect
    private var shakeDetector: ShakeDetector? = null
    private lateinit var musicPlayer: MusicPlayer

    private lateinit var scoreManager: ScoreManager
    private var isGameLaunched = false

    private var isTouchModeEnabled = true
    private var isShakeModeEnabled = false
    private val services = mutableListOf<DinoServiceInterface>()

    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        hideSystemUI(window)  //deprecated if API<30
        setContentView(layout)

        initScreenWidth(this)
        initScreenHeight(this)
        CactusSizesEnum.entries.forEach { it.updateSizes(Tools.screenWidth) }

        startTextView = findViewById(R.id.startTextView)
        startTextView.text = "Touchez pour commencer."

        isTouchModeEnabled = intent.getBooleanExtra(OptionsDialog.TOUCH_MODE_KEY, isTouchModeEnabled)
        isShakeModeEnabled = intent.getBooleanExtra(OptionsDialog.SHAKE_MODE_KEY, isShakeModeEnabled)

        val fargroundView = findViewById<View>(R.id.fargroundView)
        fargroundView.layoutParams.apply {
            width = (Tools.screenWidth * 1.1f).toInt()
        }

        isGameLaunched = false
        isGameRunning.value = null

        gameSpeed = DEFAULT_SPEED

        parentLayout = findViewById(R.id.mainView)
        groundView = findViewById(R.id.groundView)
        pauseImageView = findViewById(R.id.pauseImageView)

        scoreManager = ScoreManager(
            this,
            findViewById(R.id.scoreTextView),
            findViewById(R.id.highScoreTextView)
        )
        scoreManager.resetScore()

        dino = Dinosaur(this, findViewById(R.id.dinoImageView))
        cactusSpawner = CactusSpawner(parentLayout, dino, groundView)

        groundEffect = GroundEffect(parentLayout, R.drawable.ground_white)

        shakeDetector =
            if(isShakeModeEnabled) ShakeDetector(this) { parentLayout.post{dino.jump()} }
                else null

        musicPlayer = MusicPlayer(this)
        initListeners()
    }
    override fun onResume() {
        super.onResume()
        hideSystemUI(window)
        resumeAll()
    }
    override fun onPause() {
        super.onPause()
        pauseAll()
    }
    override fun onDestroy() {
        super.onDestroy()
        stopAll()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        parentLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && !isPaused) {
                touchScreenResponse()
            }
            true
        }
        pauseImageView.setOnClickListener {
            pauseAll()
            showResumeButton()
        }
        isGameRunning.observe(this as LifecycleOwner){ isRunning ->
            if (isRunning != null && isRunning == false) onGameOver()
        }
    }

    private fun touchScreenResponse() {
        if (!isGameLaunched) {
            lifecycleScope.launch {
                launch()
            }
        } else if (isGameRunning() && isTouchModeEnabled) {
            parentLayout.post {
                dino.jump((Tools.screenHeight * 0.4f))
            }
        }
    }


    private fun launch(){
        isGameLaunched = true
        isGameRunning.value = true

        parentLayout.removeView(startTextView)

        lifecycleScope.launch {
            dino.start()
            services.add(dino)

            groundEffect.start()
            services.add(groundEffect)

            cactusSpawner.start()
            services.add(cactusSpawner)

            shakeDetector?.start()
            shakeDetector?.let { services.add(it) }

            scoreManager.start()
            services.add(scoreManager)

            musicPlayer.start(Music.GAME1)
            services.add(musicPlayer)
        }
    }

    private fun stopAll(){
        lifecycleScope.launch {
            services.forEach {
                CoroutineScope(Dispatchers.Main).launch {
                    it.stop()
                }
            }
        }
    }

    private fun pauseAll() {
        isPaused = true
        lifecycleScope.launch {
            services.forEach {
                CoroutineScope(Dispatchers.Main).launch {
                    it.pause()
                }
            }
        }
    }

    private fun resumeAll() {
        if(isGameLaunched && isGameRunning()) {
            lifecycleScope.launch {
                parentLayout.addView(startTextView)
                for (i in 3 downTo 1) {
                    startTextView.text = "$i"
                    delay(1000)
                }
                parentLayout.removeView(startTextView)

                services.forEach {
                    CoroutineScope(Dispatchers.Main).launch {
                        it.resume()
                    }
                }
            }
        }
        isPaused = false
    }

    private fun onGameOver() {
        isGameRunning.value = null
        window?.decorView?.post {
            showReplay()
        }
        pauseAll()
    }

    private fun showReplay() {
        parentLayout.post {
            val replayDialog = ReplayDialog(this, scoreManager.score,
                scoreManager.score == scoreManager.highScore
            ) {
                recreate()
            }
            replayDialog.show()
        }
    }

    private fun showResumeButton() {
        // Créer et ajouter la vue de grisage
        val dimView = View(this).apply {
            setBackgroundColor(Color.parseColor("#80000000")) // Couleur semi-transparente (gris)
        }

        val dimParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        parentLayout.addView(dimView, dimParams)

        val resumeButton = Button(this).apply {
            text = "Reprendre"
            setOnClickListener {
                resumeAll()
                parentLayout.removeView(this)
                parentLayout.removeView(dimView)
            }
        }

        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        }

        parentLayout.addView(resumeButton, params)
    }
}