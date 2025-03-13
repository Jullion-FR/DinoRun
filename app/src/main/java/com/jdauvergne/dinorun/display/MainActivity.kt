package com.jdauvergne.dinorun.display

import ShakeDetector
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.jdauvergne.dinorun.Dinosaur
import com.jdauvergne.dinorun.R
import com.jdauvergne.dinorun.Score
import com.jdauvergne.dinorun.Tools
import com.jdauvergne.dinorun.Tools.Companion.initScreenHeight
import com.jdauvergne.dinorun.Tools.Companion.initScreenWidth
import com.jdauvergne.dinorun.cactus.CactusSizesEnum
import com.jdauvergne.dinorun.cactus.CactusSpawner
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

    private lateinit var score: Score
    private var isGameLaunched = false
    private lateinit var fullScreenModalDialog: FullScreenModalDialog

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        hideSystemUI()  //deprecated if API<30
        setContentView(layout)

        initScreenWidth(this)
        initScreenHeight(this)
        CactusSizesEnum.entries.forEach { it.updateSizes(Tools.screenWidth) }

        context = this
        isGameLaunched = false
        isGameRunning.value = null

        gameSpeed = DEFAULT_SPEED


        mainView = findViewById(R.id.mainView)
        cactusSpawner = CactusSpawner(mainView)

        groundView = findViewById(R.id.groundView)

        score = Score(findViewById(R.id.scoreTextView))
        dino = Dinosaur(this, findViewById(R.id.dinoImageView))

        groundEffect = GroundEffect(mainView, R.drawable.ground)

        shakeDetector = ShakeDetector(context) { dino.jump() }

        fullScreenModalDialog = FullScreenModalDialog(this) { recreate() }

        initListeners()

    }
    override fun onResume() {
        super.onResume()
        hideSystemUI()
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
            lifecycleScope.launch {
                dino.jump((Tools.screenHeight * 0.4).toInt())
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
        score.stop()
        cactusSpawner.stop()
    }
    private fun startAll(){
        dino.startSequence()
        startGroundMovement()

        shakeDetector.startListening()
        score.start()
        cactusSpawner.start(dino, groundView)
    }
    private fun onGameOver() {
        isGameRunning.value = null
        stopAll()
        window?.decorView?.post {
            fullScreenModalDialog.show()
        }
    }

    private fun startGroundMovement() {
        lifecycleScope.launch {
            delay(1075)
            groundEffect.startFirstMovementLoop()
        }
    }


    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.post {
                window.insetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.systemBars())
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        } else {
            @Suppress("DEPRECATION")
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
}