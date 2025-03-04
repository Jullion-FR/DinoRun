package com.jdauvergne.dinorun

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.jdauvergne.dinorun.Tools.Companion.initScreenHeight
import com.jdauvergne.dinorun.Tools.Companion.initScreenWidth
import com.jdauvergne.dinorun.cactus.CactusSizesEnum
import com.jdauvergne.dinorun.cactus.CactusSpawner
import com.jdauvergne.dinorun.ground.GroundEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object {
        var isGameRunning = false
    }

    private lateinit var mainView: ConstraintLayout
    private lateinit var groundView: View
    private lateinit var replayImageView: ImageView


    private lateinit var dino: Dinosaur
    private lateinit var cactusSpawner: CactusSpawner

    private lateinit var score: Score
    private var isGameLaunched = false

    private lateinit var groundEffect: GroundEffect
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_main

        context = this
        isGameLaunched = false
        isGameRunning = false

        hideSystemUI()  //deprecated if API<30
        initScreenWidth(this)
        initScreenHeight(this)
        CactusSizesEnum.entries.forEach { it.updateSizes(Tools.screenWidth) }
        setContentView(layout)

        mainView = findViewById(R.id.mainView)
        cactusSpawner = CactusSpawner(context, mainView)

        groundView = findViewById(R.id.groundView)
        replayImageView = findViewById(R.id.replayImageView)

        score = Score(findViewById(R.id.scoreTextView))
        dino = Dinosaur(this, findViewById(R.id.dinoImageView))

        val params = ConstraintLayout.LayoutParams(1096, 34)
        groundEffect = GroundEffect(mainView, R.drawable.ground, params)

        initListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        mainView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchScreenResponse()
            }
            true
        }

        replayImageView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                println("Restart !")
                recreate()
            }
            true
        }
    }

    private fun touchScreenResponse() {
        if (!isGameLaunched) {
            lifecycleScope.launch {
                launchSequence()
            }
        } else if (isGameRunning && !dino.isJumping) {
            lifecycleScope.launch {
                dino.jump((Tools.screenHeight * 0.4).toInt())
            }
        }
    }

    private suspend fun launchSequence() {
        isGameLaunched = true

        lifecycleScope.launch {
            dino.startSequence()
        }

        isGameRunning = true

        delay(1200)
        startGroundMovement()
        score.start()
        delay(1000)
        cactusSpawner.start(lifecycleScope, groundView, dino)
    }

    private fun startGroundMovement() {
        lifecycleScope.launch {
            groundEffect.start()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
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