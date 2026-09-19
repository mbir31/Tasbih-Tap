package com.example.service

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.SoundEffectConstants
import com.example.data.local.AppDatabase
import com.example.data.local.CustomDhikrEntity
import com.example.data.local.DhikrSessionEntity
import com.example.data.local.UserPreferences
import com.example.model.DhikrItem
import com.example.model.DhikrPresets
import com.example.model.HapticStrength
import com.example.model.Sensitivity
import com.example.model.TasbihTheme
import com.example.viewmodel.TasbihUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object TasbihManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var appContext: Context? = null
    private var userPrefs: UserPreferences? = null
    private var database: AppDatabase? = null
    private var vibrator: Vibrator? = null
    private var audioManager: AudioManager? = null

    private val _state = MutableStateFlow(TasbihUiState())
    val state: StateFlow<TasbihUiState> = _state.asStateFlow()

    @Volatile
    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) return
        synchronized(this) {
            if (isInitialized) return
            val app = context.applicationContext
            appContext = app
            val prefs = UserPreferences(app)
            userPrefs = prefs
            val db = AppDatabase.getDatabase(app)
            database = db

            vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = app.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                app.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            audioManager = app.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

            val activeDhikr = findDhikrById(
                prefs.activeDhikrId,
                prefs.activeDhikrName,
                prefs.activeDhikrArabic,
                prefs.target
            )

            val initialCounts = if (prefs.is33x3Mode) {
                when (prefs.mode33x3Stage) {
                    0 -> listOf(prefs.currentCount, 0, 0)
                    1 -> listOf(33, prefs.currentCount, 0)
                    else -> listOf(33, 33, prefs.currentCount)
                }
            } else listOf(0, 0, 0)

            _state.value = TasbihUiState(
                currentCount = prefs.currentCount,
                target = prefs.target,
                round = prefs.currentRound,
                activeDhikr = activeDhikr,
                isUnlimited = prefs.isUnlimitedMode,
                isCompleted = !prefs.isUnlimitedMode && prefs.currentCount >= prefs.target && prefs.target > 0,
                is33x3Mode = prefs.is33x3Mode,
                mode33x3Stage = prefs.mode33x3Stage,
                mode33x3Counts = initialCounts,
                isBackTapEnabled = prefs.isBackTapEnabled,
                sensitivity = prefs.sensitivity,
                isHapticEnabled = prefs.isHapticEnabled,
                hapticStrength = prefs.hapticStrength,
                isSoundEnabled = prefs.isSoundEnabled,
                selectedTheme = prefs.selectedTheme,
                isVolumeKeyCountingEnabled = prefs.isVolumeKeyCountingEnabled,
                keepScreenAwake = prefs.keepScreenAwake,
                isScreenOffCountingEnabled = prefs.isScreenOffCountingEnabled,
                isScreenOffStealthTapEnabled = prefs.isScreenOffStealthTapEnabled
            )

            isInitialized = true
        }
    }

    private fun findDhikrById(id: String, fallbackName: String, fallbackArabic: String, defaultTarget: Int): DhikrItem {
        return DhikrPresets.PRESETS.find { it.id == id } ?: DhikrItem(
            id = id,
            name = fallbackName,
            arabic = fallbackArabic,
            meaning = "",
            defaultTarget = defaultTarget
        )
    }

    fun increment(fromBackTap: Boolean = false) {
        val currentState = _state.value
        val prefs = userPrefs ?: return

        if (currentState.is33x3Mode) {
            handle33x3Increment(currentState, fromBackTap)
            return
        }

        val newCount = currentState.currentCount + 1
        val isTargetReached = !currentState.isUnlimited && currentState.target > 0 && newCount >= currentState.target

        _state.value = currentState.copy(
            currentCount = newCount,
            isCompleted = isTargetReached,
            lastTapMethod = if (fromBackTap) "back_tap" else "screen_tap"
        )
        prefs.currentCount = newCount

        triggerSensoryFeedback(isCompletion = isTargetReached)

        if (isTargetReached) {
            logSession(
                dhikrName = currentState.activeDhikr.name,
                dhikrArabic = currentState.activeDhikr.arabic,
                count = newCount,
                target = currentState.target,
                completed = true,
                round = currentState.round
            )
        }

        appContext?.let { ScreenOffTasbihService.updateNotification(it) }
    }

    private fun handle33x3Increment(currentState: TasbihUiState, fromBackTap: Boolean) {
        val prefs = userPrefs ?: return
        val stages = DhikrPresets.THIRTY_THREE_TIMES_THREE
        val currentStage = currentState.mode33x3Stage.coerceIn(0, stages.size - 1)
        val stageTarget = if (currentStage == 2) 34 else 33

        // If the full 100-count Misbaha was completed, tapping again advances to next round
        if (currentState.isCompleted) {
            val nextRound = currentState.round + 1
            val newCounts = mutableListOf(1, 0, 0)
            val firstStageDhikr = stages[0]
            _state.value = currentState.copy(
                currentCount = 1,
                target = 33,
                round = nextRound,
                activeDhikr = firstStageDhikr,
                mode33x3Stage = 0,
                mode33x3Counts = newCounts,
                isCompleted = false,
                lastTapMethod = if (fromBackTap) "back_tap" else "screen_tap"
            )
            prefs.currentCount = 1
            prefs.currentRound = nextRound
            prefs.mode33x3Stage = 0
            prefs.target = 33
            triggerSensoryFeedback(isCompletion = false)
            appContext?.let { ScreenOffTasbihService.updateNotification(it) }
            return
        }

        // If current stage reached target, advance stage
        if (currentState.currentCount >= stageTarget && currentStage < stages.size - 1) {
            val nextStage = currentStage + 1
            val nextTarget = if (nextStage == 2) 34 else 33
            val nextStageDhikr = stages[nextStage]
            val updatedCounts = currentState.mode33x3Counts.toMutableList()
            updatedCounts[nextStage] = 1

            _state.value = currentState.copy(
                currentCount = 1,
                target = nextTarget,
                activeDhikr = nextStageDhikr,
                mode33x3Stage = nextStage,
                mode33x3Counts = updatedCounts,
                isCompleted = false,
                lastTapMethod = if (fromBackTap) "back_tap" else "screen_tap"
            )
            prefs.currentCount = 1
            prefs.mode33x3Stage = nextStage
            prefs.target = nextTarget
            triggerSensoryFeedback(isCompletion = false)
            appContext?.let { ScreenOffTasbihService.updateNotification(it) }
            return
        }

        // Increment count within the current stage
        val newCount = currentState.currentCount + 1
        val updatedCounts = currentState.mode33x3Counts.toMutableList()
        updatedCounts[currentStage] = newCount
        val isStageCompleted = newCount >= stageTarget

        if (isStageCompleted) {
            if (currentStage < stages.size - 1) {
                triggerSensoryFeedback(isCompletion = true)
                _state.value = currentState.copy(
                    currentCount = newCount,
                    target = stageTarget,
                    activeDhikr = stages[currentStage],
                    mode33x3Stage = currentStage,
                    mode33x3Counts = updatedCounts,
                    isCompleted = false,
                    lastTapMethod = if (fromBackTap) "back_tap" else "screen_tap"
                )
            } else {
                triggerSensoryFeedback(isCompletion = true)
                logSession(
                    dhikrName = "33×3 Misbaha (Complete)",
                    dhikrArabic = "سُبْحَانَ اللَّهِ • الْحَمْدُ لِلَّهِ • اللَّهُ أَكْبَرُ",
                    count = 100,
                    target = 100,
                    completed = true,
                    round = currentState.round
                )
                _state.value = currentState.copy(
                    currentCount = newCount,
                    target = stageTarget,
                    activeDhikr = stages[currentStage],
                    mode33x3Stage = currentStage,
                    mode33x3Counts = updatedCounts,
                    isCompleted = true,
                    lastTapMethod = if (fromBackTap) "back_tap" else "screen_tap"
                )
            }
        } else {
            triggerSensoryFeedback(isCompletion = false)
            _state.value = currentState.copy(
                currentCount = newCount,
                target = stageTarget,
                activeDhikr = stages[currentStage],
                mode33x3Stage = currentStage,
                mode33x3Counts = updatedCounts,
                isCompleted = false,
                lastTapMethod = if (fromBackTap) "back_tap" else "screen_tap"
            )
        }

        prefs.currentCount = newCount
        prefs.mode33x3Stage = currentStage
        prefs.target = stageTarget
        appContext?.let { ScreenOffTasbihService.updateNotification(it) }
    }

    fun decrement() {
        val currentState = _state.value
        val prefs = userPrefs ?: return

        if (currentState.is33x3Mode) {
            val stages = DhikrPresets.THIRTY_THREE_TIMES_THREE
            val currentStage = currentState.mode33x3Stage.coerceIn(0, stages.size - 1)

            if (currentState.currentCount > 0) {
                val newCount = currentState.currentCount - 1
                val updatedCounts = currentState.mode33x3Counts.toMutableList()
                updatedCounts[currentStage] = newCount

                _state.value = currentState.copy(
                    currentCount = newCount,
                    mode33x3Counts = updatedCounts,
                    isCompleted = false
                )
                prefs.currentCount = newCount
                triggerSensoryFeedback(isCompletion = false)
                appContext?.let { ScreenOffTasbihService.updateNotification(it) }
            } else if (currentStage > 0) {
                val prevStage = currentStage - 1
                val prevTarget = if (prevStage == 2) 34 else 33
                val prevStageDhikr = stages[prevStage]
                val prevCount = (prevTarget - 1).coerceAtLeast(0)
                val updatedCounts = currentState.mode33x3Counts.toMutableList()
                updatedCounts[prevStage] = prevCount

                _state.value = currentState.copy(
                    currentCount = prevCount,
                    target = prevTarget,
                    activeDhikr = prevStageDhikr,
                    mode33x3Stage = prevStage,
                    mode33x3Counts = updatedCounts,
                    isCompleted = false
                )
                prefs.currentCount = prevCount
                prefs.mode33x3Stage = prevStage
                prefs.target = prevTarget
                triggerSensoryFeedback(isCompletion = false)
                appContext?.let { ScreenOffTasbihService.updateNotification(it) }
            }
            return
        }

        if (currentState.currentCount > 0) {
            val newCount = currentState.currentCount - 1
            _state.value = currentState.copy(
                currentCount = newCount,
                isCompleted = false
            )
            prefs.currentCount = newCount
            triggerSensoryFeedback(isCompletion = false)
            appContext?.let { ScreenOffTasbihService.updateNotification(it) }
        }
    }

    fun reset() {
        val currentState = _state.value
        val prefs = userPrefs ?: return

        if (currentState.currentCount > 0) {
            logSession(
                dhikrName = currentState.activeDhikr.name,
                dhikrArabic = currentState.activeDhikr.arabic,
                count = currentState.currentCount,
                target = currentState.target,
                completed = currentState.isCompleted,
                round = currentState.round
            )
        }

        val resetCounts = if (currentState.is33x3Mode) listOf(0, 0, 0) else currentState.mode33x3Counts
        val activeDhikr = if (currentState.is33x3Mode) DhikrPresets.THIRTY_THREE_TIMES_THREE[0] else currentState.activeDhikr
        val target = if (currentState.is33x3Mode) 33 else currentState.target

        _state.value = currentState.copy(
            currentCount = 0,
            round = 1,
            target = target,
            activeDhikr = activeDhikr,
            mode33x3Stage = 0,
            mode33x3Counts = resetCounts,
            isCompleted = false
        )
        prefs.currentCount = 0
        prefs.currentRound = 1
        prefs.target = target
        if (currentState.is33x3Mode) {
            prefs.mode33x3Stage = 0
        }
        appContext?.let { ScreenOffTasbihService.updateNotification(it) }
    }

    fun advanceRound() {
        val currentState = _state.value
        val prefs = userPrefs ?: return

        if (currentState.currentCount > 0) {
            logSession(
                dhikrName = currentState.activeDhikr.name,
                dhikrArabic = currentState.activeDhikr.arabic,
                count = currentState.currentCount,
                target = currentState.target,
                completed = currentState.isCompleted,
                round = currentState.round
            )
        }

        val nextRound = currentState.round + 1
        val resetCounts = if (currentState.is33x3Mode) listOf(0, 0, 0) else currentState.mode33x3Counts
        val activeDhikr = if (currentState.is33x3Mode) DhikrPresets.THIRTY_THREE_TIMES_THREE[0] else currentState.activeDhikr
        val target = if (currentState.is33x3Mode) 33 else currentState.target

        _state.value = currentState.copy(
            currentCount = 0,
            round = nextRound,
            target = target,
            activeDhikr = activeDhikr,
            mode33x3Stage = 0,
            mode33x3Counts = resetCounts,
            isCompleted = false
        )
        prefs.currentCount = 0
        prefs.currentRound = nextRound
        prefs.target = target
        if (currentState.is33x3Mode) {
            prefs.mode33x3Stage = 0
        }
        appContext?.let { ScreenOffTasbihService.updateNotification(it) }
    }

    fun selectDhikr(dhikr: DhikrItem) {
        val currentState = _state.value
        val prefs = userPrefs ?: return

        if (currentState.currentCount > 0) {
            logSession(
                dhikrName = currentState.activeDhikr.name,
                dhikrArabic = currentState.activeDhikr.arabic,
                count = currentState.currentCount,
                target = currentState.target,
                completed = currentState.isCompleted,
                round = currentState.round
            )
        }

        val newTarget = dhikr.defaultTarget
        _state.value = currentState.copy(
            activeDhikr = dhikr,
            currentCount = 0,
            round = 1,
            target = newTarget,
            isUnlimited = false,
            is33x3Mode = false,
            mode33x3Stage = 0,
            mode33x3Counts = listOf(0, 0, 0),
            isCompleted = false
        )

        prefs.activeDhikrId = dhikr.id
        prefs.activeDhikrName = dhikr.name
        prefs.activeDhikrArabic = dhikr.arabic
        prefs.currentCount = 0
        prefs.currentRound = 1
        prefs.target = newTarget
        prefs.isUnlimitedMode = false
        prefs.is33x3Mode = false
        prefs.mode33x3Stage = 0

        appContext?.let { ScreenOffTasbihService.updateNotification(it) }
    }

    fun selectTarget(target: Int) {
        val currentState = _state.value
        val prefs = userPrefs ?: return

        _state.value = currentState.copy(
            target = target,
            isUnlimited = false,
            isCompleted = currentState.currentCount >= target && target > 0
        )
        prefs.target = target
        prefs.isUnlimitedMode = false
        appContext?.let { ScreenOffTasbihService.updateNotification(it) }
    }

    fun toggleUnlimitedMode(enabled: Boolean) {
        val currentState = _state.value
        val prefs = userPrefs ?: return

        _state.value = currentState.copy(
            isUnlimited = enabled,
            isCompleted = if (enabled) false else (currentState.target > 0 && currentState.currentCount >= currentState.target)
        )
        prefs.isUnlimitedMode = enabled
        appContext?.let { ScreenOffTasbihService.updateNotification(it) }
    }

    fun toggle33x3Mode(enabled: Boolean) {
        val currentState = _state.value
        val prefs = userPrefs ?: return

        if (currentState.currentCount > 0) {
            logSession(
                dhikrName = currentState.activeDhikr.name,
                dhikrArabic = currentState.activeDhikr.arabic,
                count = currentState.currentCount,
                target = currentState.target,
                completed = currentState.isCompleted,
                round = currentState.round
            )
        }

        if (enabled) {
            val stages = DhikrPresets.THIRTY_THREE_TIMES_THREE
            _state.value = currentState.copy(
                is33x3Mode = true,
                mode33x3Stage = 0,
                mode33x3Counts = listOf(0, 0, 0),
                activeDhikr = stages[0],
                currentCount = 0,
                target = 33,
                round = 1,
                isUnlimited = false,
                isCompleted = false
            )
            prefs.is33x3Mode = true
            prefs.mode33x3Stage = 0
            prefs.currentCount = 0
            prefs.target = 33
            prefs.currentRound = 1
            prefs.isUnlimitedMode = false
        } else {
            val firstPreset = DhikrPresets.PRESETS[0]
            _state.value = currentState.copy(
                is33x3Mode = false,
                mode33x3Stage = 0,
                mode33x3Counts = listOf(0, 0, 0),
                activeDhikr = firstPreset,
                currentCount = 0,
                target = firstPreset.defaultTarget,
                round = 1,
                isCompleted = false
            )
            prefs.is33x3Mode = false
            prefs.mode33x3Stage = 0
            prefs.currentCount = 0
            prefs.target = firstPreset.defaultTarget
            prefs.currentRound = 1
        }
        appContext?.let { ScreenOffTasbihService.updateNotification(it) }
    }

    fun toggleBackTap(enabled: Boolean) {
        _state.value = _state.value.copy(isBackTapEnabled = enabled)
        userPrefs?.isBackTapEnabled = enabled
    }

    fun setSensitivity(sensitivity: Sensitivity) {
        _state.value = _state.value.copy(sensitivity = sensitivity)
        userPrefs?.sensitivity = sensitivity
    }

    fun toggleHaptic(enabled: Boolean) {
        _state.value = _state.value.copy(isHapticEnabled = enabled)
        userPrefs?.isHapticEnabled = enabled
    }

    fun setHapticStrength(strength: HapticStrength) {
        _state.value = _state.value.copy(hapticStrength = strength)
        userPrefs?.hapticStrength = strength
    }

    fun toggleSound(enabled: Boolean) {
        _state.value = _state.value.copy(isSoundEnabled = enabled)
        userPrefs?.isSoundEnabled = enabled
    }

    fun selectTheme(theme: TasbihTheme) {
        _state.value = _state.value.copy(selectedTheme = theme)
        userPrefs?.selectedTheme = theme
    }

    fun toggleKeepScreenAwake(keepAwake: Boolean) {
        _state.value = _state.value.copy(keepScreenAwake = keepAwake)
        userPrefs?.keepScreenAwake = keepAwake
    }

    fun toggleVolumeKeyCounting(enabled: Boolean) {
        _state.value = _state.value.copy(isVolumeKeyCountingEnabled = enabled)
        userPrefs?.isVolumeKeyCountingEnabled = enabled
    }

    fun toggleScreenOffCounting(enabled: Boolean) {
        _state.value = _state.value.copy(isScreenOffCountingEnabled = enabled)
        userPrefs?.isScreenOffCountingEnabled = enabled
        val ctx = appContext ?: return
        if (enabled) {
            ScreenOffTasbihService.startService(ctx)
        } else {
            ScreenOffTasbihService.stopService(ctx)
        }
    }

    fun toggleScreenOffStealthTap(enabled: Boolean) {
        _state.value = _state.value.copy(isScreenOffStealthTapEnabled = enabled)
        userPrefs?.isScreenOffStealthTapEnabled = enabled
    }

    fun triggerSensoryFeedback(isCompletion: Boolean) {
        val vib = vibrator
        val audio = audioManager
        val s = _state.value

        if (s.isHapticEnabled && vib != null && vib.hasVibrator()) {
            try {
                val strength = s.hapticStrength
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (isCompletion) {
                        // Double pulse on round/target completion
                        val timing = longArrayOf(0, 35, 60, 45)
                        val amplitudes = intArrayOf(0, (strength.normalAmplitude * 1.2).toInt().coerceAtMost(255), 0, 255)
                        vib.vibrate(VibrationEffect.createWaveform(timing, amplitudes, -1))
                    } else {
                        // Subtle crisp single pulse on ordinary tap
                        vib.vibrate(VibrationEffect.createOneShot(strength.normalDuration, strength.normalAmplitude))
                    }
                } else {
                    @Suppress("DEPRECATION")
                    vib.vibrate(if (isCompletion) 80 else strength.normalDuration)
                }
            } catch (e: Exception) {
                // Ignore vibration failure
            }
        }

        if (s.isSoundEnabled && audio != null) {
            try {
                audio.playSoundEffect(SoundEffectConstants.CLICK, 0.4f)
            } catch (e: Exception) {
                // Ignore audio failure
            }
        }
    }

    private fun logSession(
        dhikrName: String,
        dhikrArabic: String,
        count: Int,
        target: Int,
        completed: Boolean,
        round: Int
    ) {
        if (count <= 0) return
        val db = database ?: return
        scope.launch(Dispatchers.IO) {
            try {
                db.sessionDao().insertSession(
                    DhikrSessionEntity(
                        dhikrName = dhikrName,
                        dhikrArabic = dhikrArabic,
                        count = count,
                        target = target,
                        completed = completed,
                        round = round
                    )
                )
            } catch (e: Exception) {
                // Ignore DB logging failure
            }
        }
    }
}
