import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.jdauvergne.dinorun.display.MainActivity
import kotlin.math.abs

class ShakeDetector(context: Context, private val onShake: () -> Unit) : SensorEventListener {
    private var sensorManager: SensorManager? = null
    private val shakeThreshold = 5.8f //todo tweak
    private val debounceTime = 300L
    private var lastShakeTime = 0L

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    fun startListening() {
        sensorManager?.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (!MainActivity.isGameRunning()) {
            stopListening()
            return
        }

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
