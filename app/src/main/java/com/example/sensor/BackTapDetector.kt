package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.model.Sensitivity
import kotlin.math.abs
import kotlin.math.sqrt

class BackTapDetector(
    context: Context,
    private var sensitivity: Sensitivity = Sensitivity.MEDIUM,
    private val onBackTap: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelerometer: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    val isSupported: Boolean = accelerometer != null
    val hasGyroscope: Boolean = gyroscope != null

    @Volatile
    var isListening: Boolean = false
        private set

    // Diagnostic fields for calibration and test screen
    @Volatile
    var lastTapTimestamp: Long = 0L
        private set

    @Volatile
    var currentJerkEstimate: Float = 0f
        private set

    // Acceleration tracking
    private var lastAccX = 0f
    private var lastAccY = 0f
    private var lastAccZ = 0f
    private var isFirstAcc = true

    // Gyroscope tracking (for rotational false-positive rejection)
    private var gyroMagnitude = 0f

    // False-positive tracking: sustained movement window
    private var highMotionWindowCount = 0
    private var lastSampleTime = 0L

    // Cooldown debounce to prevent multiple triggers from one physical tap
    private val cooldownMs = 320L

    fun setSensitivity(newSensitivity: Sensitivity) {
        sensitivity = newSensitivity
    }

    fun start() {
        if (isListening || sensorManager == null || accelerometer == null) return

        isFirstAcc = true
        highMotionWindowCount = 0
        gyroMagnitude = 0f
        lastSampleTime = System.currentTimeMillis()

        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )

        gyroscope?.let { gyro ->
            sensorManager.registerListener(
                this,
                gyro,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        isListening = true
    }

    fun stop() {
        if (!isListening || sensorManager == null) return
        sensorManager.unregisterListener(this)
        isListening = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || !isListening) return

        when (event.sensor.type) {
            Sensor.TYPE_GYROSCOPE -> {
                val gx = event.values[0]
                val gy = event.values[1]
                val gz = event.values[2]
                gyroMagnitude = sqrt(gx * gx + gy * gy + gz * gz)
            }

            Sensor.TYPE_ACCELEROMETER -> {
                val ax = event.values[0]
                val ay = event.values[1]
                val az = event.values[2]

                if (isFirstAcc) {
                    lastAccX = ax
                    lastAccY = ay
                    lastAccZ = az
                    isFirstAcc = false
                    return
                }

                val now = System.currentTimeMillis()
                val deltaX = abs(ax - lastAccX)
                val deltaY = abs(ay - lastAccY)
                val deltaZ = abs(az - lastAccZ)

                lastAccX = ax
                lastAccY = ay
                lastAccZ = az

                // Calculate jerk along Z-axis (normal to the phone rear surface)
                // and total 3D jerk
                val totalJerk = deltaX + deltaY + deltaZ
                currentJerkEstimate = deltaZ

                // False Positive Suppression 1: Reject if phone is undergoing strong rotation (e.g. spinning, swinging, waving)
                if (hasGyroscope && gyroMagnitude > 2.2f) {
                    highMotionWindowCount = 5
                    return
                }

                // False Positive Suppression 2: Reject continuous shaking / walking vibration
                if (totalJerk > 7.0f) {
                    highMotionWindowCount = (highMotionWindowCount + 1).coerceAtMost(10)
                } else if (highMotionWindowCount > 0) {
                    highMotionWindowCount--
                }

                if (highMotionWindowCount > 4) {
                    // Continuous motion in progress
                    return
                }

                // False Positive Suppression 3: Cooldown debounce
                if (now - lastTapTimestamp < cooldownMs) {
                    return
                }

                // Tap detection evaluation:
                // A rear-tap exhibits a sudden spike in Z-axis jerk that exceeds the sensitivity threshold
                val threshold = sensitivity.threshold
                val isRearTapCandidate = deltaZ >= threshold && (deltaZ >= deltaX * 0.7f || deltaZ >= deltaY * 0.7f)

                if (isRearTapCandidate) {
                    lastTapTimestamp = now
                    onBackTap()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No action required
    }
}
