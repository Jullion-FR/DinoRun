import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.jdauvergne.dinorun.MainActivity
import kotlin.math.abs
import kotlin.math.sqrt

class ShakeDetector(context: Context, private val onShake: () -> Unit) : SensorEventListener {
    private var sensorManager: SensorManager? = null
    private val shakeThreshold = 6f

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    fun startListening() {
        sensorManager?.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(!MainActivity.isGameRunning){
            stopListening()
            return
        }

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        //Diagonal detection
        if (abs(y+x)/2 > shakeThreshold) {
            onShake.invoke()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
