package com.jdauvergne.dinorun.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.jdauvergne.dinorun.DinoServiceInterface
import com.jdauvergne.dinorun.display.MainActivity
import kotlin.math.abs

class ShakeDetector(context: Context, private val onShake: () -> Unit) : SensorEventListener, DinoServiceInterface {
    private var sensorManager: SensorManager? = null
    private val shakeThreshold = 5.8f //todo tweak
    private val debounceTime = 300L
    private var lastShakeTime = 0L
    private var isPaused = false

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    fun start() {
        startListening()
    }

    override fun stop() {
        stopListening()
    }

    override fun pause() {
        isPaused = true
    }

    override fun resume() {
        isPaused = false
        startListening() // Reprend l'écoute des secousses
    }

    private fun startListening() {
        if (!isPaused) {
            sensorManager?.registerListener(
                this,
                sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    private fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (!MainActivity.isGameRunning()) {
            stopListening()
            return
        }

        if (isPaused) return // Ignore les secousses si en pause

        val x = event.values[0]
        val y = event.values[1]
        val currentTime = System.currentTimeMillis()

        if (abs(y + x*0.9) / 2 > shakeThreshold && (currentTime - lastShakeTime > debounceTime)) {
            lastShakeTime = currentTime
            onShake.invoke()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
