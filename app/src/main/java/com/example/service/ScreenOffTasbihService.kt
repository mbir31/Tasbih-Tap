package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.ScreenOffActivity
import com.example.sensor.BackTapDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScreenOffTasbihService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var backTapDetector: BackTapDetector? = null
    private var screenReceiver: BroadcastReceiver? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var stateObservationJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        TasbihManager.initialize(this)

        createNotificationChannel()

        // Acquire partial wake lock so CPU stays awake to process sensors with screen off
        val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
        wakeLock = powerManager?.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "TasbihTap::ScreenOffSensorWakeLock"
        )?.apply {
            setReferenceCounted(false)
            try {
                acquire(4 * 60 * 60 * 1000L) // 4 hours maximum safety timeout
            } catch (e: Exception) {
                // Ignore wake lock acquire failure
            }
        }

        // Initialize back-tap sensor
        val initialSensitivity = TasbihManager.state.value.sensitivity
        backTapDetector = BackTapDetector(
            context = this,
            sensitivity = initialSensitivity,
            onBackTap = {
                onBackTapDetected()
            }
        )
        backTapDetector?.start()

        // Register screen on/off receiver to dynamically adapt if needed
        screenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_SCREEN_OFF -> {
                        // Ensure sensor is listening when screen turns off
                        val state = TasbihManager.state.value
                        if (state.isBackTapEnabled && state.isScreenOffCountingEnabled) {
                            backTapDetector?.setSensitivity(state.sensitivity)
                            backTapDetector?.start()
                        }
                    }
                    Intent.ACTION_SCREEN_ON -> {
                        // Keep listening
                        val state = TasbihManager.state.value
                        if (state.isBackTapEnabled && state.isScreenOffCountingEnabled) {
                            backTapDetector?.setSensitivity(state.sensitivity)
                            backTapDetector?.start()
                        }
                    }
                }
            }
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        registerReceiver(screenReceiver, filter)

        // Observe state to update notification & detector sensitivity dynamically
        stateObservationJob = serviceScope.launch {
            TasbihManager.state.collectLatest { state ->
                backTapDetector?.setSensitivity(state.sensitivity)
                if (state.isBackTapEnabled && state.isScreenOffCountingEnabled) {
                    backTapDetector?.start()
                } else {
                    backTapDetector?.stop()
                }
                updateNotificationContent(state)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }

        val notification = buildNotification(TasbihManager.state.value)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        return START_STICKY
    }

    private fun onBackTapDetected() {
        val state = TasbihManager.state.value
        if (!state.isBackTapEnabled || !state.isScreenOffCountingEnabled) return
        TasbihManager.increment(fromBackTap = true)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.screen_off_notif_title),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.screen_off_counting_desc)
                setShowBadge(false)
                enableVibration(false)
                setSound(null, null)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(state: com.example.viewmodel.TasbihUiState): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            100,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stealthIntent = Intent(this, ScreenOffActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val stealthPendingIntent = PendingIntent.getActivity(
            this,
            101,
            stealthIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopIntent = Intent(this, ScreenOffTasbihService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            102,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val dhikrName = if (state.is33x3Mode) {
            "Sunnah 33×3 (${state.activeDhikr.name})"
        } else {
            state.activeDhikr.name
        }

        val targetStr = if (state.isUnlimited) "∞" else state.target.toString()
        val contentText = getString(
            R.string.screen_off_notif_content,
            dhikrName,
            state.currentCount,
            targetStr,
            state.round
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.screen_off_notif_title))
            .setContentText(contentText)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                0,
                getString(R.string.screen_off_notif_stealth),
                stealthPendingIntent
            )
            .addAction(
                0,
                getString(R.string.screen_off_notif_stop),
                stopPendingIntent
            )
            .build()
    }

    private fun updateNotificationContent(state: com.example.viewmodel.TasbihUiState) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        manager?.notify(NOTIFICATION_ID, buildNotification(state))
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        stateObservationJob?.cancel()
        serviceScope.cancel()

        try {
            screenReceiver?.let { unregisterReceiver(it) }
        } catch (e: Exception) {
            // Ignore unregister exception
        }

        backTapDetector?.stop()
        backTapDetector = null

        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (e: Exception) {
            // Ignore wake lock release exception
        }
        wakeLock = null
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_STOP = "com.example.action.STOP_SCREEN_OFF"
        const val NOTIFICATION_ID = 9991
        const val CHANNEL_ID = "tasbih_screen_off_channel"

        @Volatile
        var isRunning = false
            private set

        fun startService(context: Context) {
            try {
                val intent = Intent(context, ScreenOffTasbihService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (e: Exception) {
                // If background start is restricted, ignore or log
            }
        }

        fun stopService(context: Context) {
            try {
                val intent = Intent(context, ScreenOffTasbihService::class.java).apply {
                    action = ACTION_STOP
                }
                context.startService(intent)
            } catch (e: Exception) {
                // Ignore stop error
            }
        }

        fun updateNotification(context: Context) {
            if (!isRunning) return
            try {
                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                val intent = Intent(context, ScreenOffTasbihService::class.java)
                // Service self-updates through state flow, or via manager directly:
                manager?.notify(
                    NOTIFICATION_ID,
                    (context as? ScreenOffTasbihService)?.buildNotification(TasbihManager.state.value)
                        ?: return
                )
            } catch (e: Exception) {
                // Ignore update failure
            }
        }
    }
}
