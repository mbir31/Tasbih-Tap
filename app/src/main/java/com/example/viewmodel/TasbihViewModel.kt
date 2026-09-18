package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.SoundEffectConstants
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CustomDhikrEntity
import com.example.data.local.DhikrSessionEntity
import com.example.data.local.UserPreferences
import com.example.model.DhikrItem
import com.example.model.DhikrPresets
import com.example.model.HapticStrength
import com.example.model.Sensitivity
import com.example.model.TasbihTheme
import com.example.sensor.BackTapDetector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class TasbihUiState(
    val currentCount: Int = 0,
    val target: Int = 33,
    val round: Int = 1,
    val activeDhikr: DhikrItem = DhikrPresets.PRESETS[0],
    val isUnlimited: Boolean = false,
    val isCompleted: Boolean = false,
    val is33x3Mode: Boolean = false,
    val mode33x3Stage: Int = 0, // 0: SubhanAllah, 1: Alhamdulillah, 2: Allahu Akbar
    val mode33x3Counts: List<Int> = listOf(0, 0, 0),
    val isBackTapEnabled: Boolean = true,
    val sensitivity: Sensitivity = Sensitivity.MEDIUM,
    val isHapticEnabled: Boolean = true,
    val hapticStrength: HapticStrength = HapticStrength.MEDIUM,
    val isSoundEnabled: Boolean = false,
    val selectedTheme: TasbihTheme = TasbihTheme.EMERALD,
    val isVolumeKeyCountingEnabled: Boolean = false,
    val keepScreenAwake: Boolean = true,
    val lastTapMethod: String = ""
)

class TasbihViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefs = UserPreferences(application)
    private val db = AppDatabase.getDatabase(application)
    private val sessionDao = db.sessionDao()
    private val customDhikrDao = db.customDhikrDao()

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    private val _uiState = MutableStateFlow(
        TasbihUiState(
            currentCount = userPrefs.currentCount,
            target = userPrefs.target,
            round = userPrefs.currentRound,
            activeDhikr = findDhikrById(userPrefs.activeDhikrId, userPrefs.activeDhikrName, userPrefs.activeDhikrArabic, userPrefs.target),
            isUnlimited = userPrefs.isUnlimitedMode,
            isCompleted = !userPrefs.isUnlimitedMode && userPrefs.currentCount >= userPrefs.target && userPrefs.target > 0,
            is33x3Mode = userPrefs.is33x3Mode,
            mode33x3Stage = userPrefs.mode33x3Stage,
            isBackTapEnabled = userPrefs.isBackTapEnabled,
            sensitivity = userPrefs.sensitivity,
            isHapticEnabled = userPrefs.isHapticEnabled,
            hapticStrength = userPrefs.hapticStrength,
            isSoundEnabled = userPrefs.isSoundEnabled,
            selectedTheme = userPrefs.selectedTheme,
            isVolumeKeyCountingEnabled = userPrefs.isVolumeKeyCountingEnabled,
            keepScreenAwake = userPrefs.keepScreenAwake
        )
    )
    val uiState: StateFlow<TasbihUiState> = _uiState.asStateFlow()

    // Back-Tap Detector instance
    val backTapDetector: BackTapDetector = BackTapDetector(
        context = application,
        sensitivity = userPrefs.sensitivity,
        onBackTap = {
            onBackTapTriggered()
        }
    )

    // History and Statistics Flows
    val allSessions: StateFlow<List<DhikrSessionEntity>> = sessionDao.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customDhikrList: StateFlow<List<CustomDhikrEntity>> = customDhikrDao.getAllCustomDhikr()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val startOfDayTimestamp: Long
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }

    val todayDhikrCount: StateFlow<Int> = sessionDao.getTodayDhikrCount(startOfDayTimestamp)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todaySessionsCount: StateFlow<Int> = sessionDao.getTodaySessionsCount(startOfDayTimestamp)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedSessionsCount: StateFlow<Int> = sessionDao.getCompletedSessionsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allTimeDhikrCount: StateFlow<Int> = sessionDao.getTotalDhikrCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        if (userPrefs.isBackTapEnabled) {
            backTapDetector.start()
        }
    }

    fun onResume() {
        if (_uiState.value.isBackTapEnabled) {
            backTapDetector.start()
        }
    }

    fun onPause() {
        backTapDetector.stop()
    }

    override fun onCleared() {
        super.onCleared()
        backTapDetector.stop()
    }

    private fun onBackTapTriggered() {
        viewModelScope.launch {
            increment(fromBackTap = true)
        }
    }

    fun increment(fromBackTap: Boolean = false) {
        val currentState = _uiState.value

        if (currentState.is33x3Mode) {
            handle33x3Increment(currentState, fromBackTap)
            return
        }

        val newCount = currentState.currentCount + 1
        val isTargetReached = !currentState.isUnlimited && currentState.target > 0 && newCount >= currentState.target

        _uiState.value = currentState.copy(
            currentCount = newCount,
            isCompleted = isTargetReached,
            lastTapMethod = if (fromBackTap) "back_tap" else "screen_tap"
        )
        userPrefs.currentCount = newCount

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
    }

    private fun handle33x3Increment(currentState: TasbihUiState, fromBackTap: Boolean) {
        val currentStage = currentState.mode33x3Stage
        val stages = DhikrPresets.THIRTY_THREE_TIMES_THREE
        val updatedCounts = currentState.mode33x3Counts.toMutableList()

        val stageCount = updatedCounts[currentStage] + 1
        updatedCounts[currentStage] = stageCount

        val isStageCompleted = stageCount >= 33
        var nextStage = currentStage
        var isOverallCompleted = false

        if (isStageCompleted) {
            if (currentStage < stages.size - 1) {
                nextStage = currentStage + 1
                triggerSensoryFeedback(isCompletion = true)
            } else {
                isOverallCompleted = true
                triggerSensoryFeedback(isCompletion = true)
                logSession(
                    dhikrName = "33×3 Misbaha (Complete)",
                    dhikrArabic = "سُبْحَانَ اللَّهِ • الْحَمْدُ لِلَّهِ • اللَّهُ أَكْبَرُ",
                    count = 99,
                    target = 99,
                    completed = true,
                    round = currentState.round
                )
            }
        } else {
            triggerSensoryFeedback(isCompletion = false)
        }

        val currentStageDhikr = stages[nextStage]

        _uiState.value = currentState.copy(
            currentCount = stageCount,
            target = 33,
            activeDhikr = currentStageDhikr,
            mode33x3Stage = nextStage,
            mode33x3Counts = updatedCounts,
            isCompleted = isOverallCompleted,
            lastTapMethod = if (fromBackTap) "back_tap" else "screen_tap"
        )

        userPrefs.currentCount = stageCount
        userPrefs.mode33x3Stage = nextStage
    }

    fun decrement() {
        val currentState = _uiState.value
        if (currentState.currentCount <= 0) return

        val newCount = currentState.currentCount - 1
        _uiState.value = currentState.copy(
            currentCount = newCount,
            isCompleted = false
        )
        userPrefs.currentCount = newCount
        triggerSensoryFeedback(isCompletion = false)
    }

    fun reset() {
        val currentState = _uiState.value
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

        _uiState.value = currentState.copy(
            currentCount = 0,
            isCompleted = false,
            mode33x3Stage = 0,
            mode33x3Counts = listOf(0, 0, 0)
        )
        userPrefs.currentCount = 0
        userPrefs.mode33x3Stage = 0
    }

    fun advanceRound() {
        val currentState = _uiState.value
        val nextRound = currentState.round + 1

        _uiState.value = currentState.copy(
            currentCount = 0,
            round = nextRound,
            isCompleted = false,
            mode33x3Stage = 0,
            mode33x3Counts = listOf(0, 0, 0)
        )
        userPrefs.currentCount = 0
        userPrefs.currentRound = nextRound
        userPrefs.mode33x3Stage = 0
    }

    fun selectDhikr(dhikr: DhikrItem) {
        val currentState = _uiState.value

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

        _uiState.value = currentState.copy(
            activeDhikr = dhikr,
            target = dhikr.defaultTarget,
            currentCount = 0,
            round = 1,
            isCompleted = false,
            is33x3Mode = false
        )

        userPrefs.activeDhikrId = dhikr.id
        userPrefs.activeDhikrName = dhikr.name
        userPrefs.activeDhikrArabic = dhikr.arabic
        userPrefs.target = dhikr.defaultTarget
        userPrefs.currentCount = 0
        userPrefs.currentRound = 1
        userPrefs.is33x3Mode = false
    }

    fun selectTarget(target: Int) {
        val isCompleted = target in 1.._uiState.value.currentCount
        _uiState.value = _uiState.value.copy(
            target = target,
            isUnlimited = false,
            isCompleted = isCompleted
        )
        userPrefs.target = target
        userPrefs.isUnlimitedMode = false
    }

    fun toggleUnlimitedMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            isUnlimited = enabled,
            isCompleted = false
        )
        userPrefs.isUnlimitedMode = enabled
    }

    fun toggle33x3Mode(enabled: Boolean) {
        val currentState = _uiState.value
        val stageDhikr = DhikrPresets.THIRTY_THREE_TIMES_THREE[0]

        _uiState.value = currentState.copy(
            is33x3Mode = enabled,
            mode33x3Stage = 0,
            mode33x3Counts = listOf(0, 0, 0),
            currentCount = 0,
            target = 33,
            activeDhikr = if (enabled) stageDhikr else currentState.activeDhikr,
            isUnlimited = false,
            isCompleted = false
        )

        userPrefs.is33x3Mode = enabled
        userPrefs.currentCount = 0
        userPrefs.mode33x3Stage = 0
        if (enabled) {
            userPrefs.target = 33
        }
    }

    fun toggleBackTap(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isBackTapEnabled = enabled)
        userPrefs.isBackTapEnabled = enabled
        if (enabled) {
            backTapDetector.start()
        } else {
            backTapDetector.stop()
        }
    }

    fun setSensitivity(sensitivity: Sensitivity) {
        _uiState.value = _uiState.value.copy(sensitivity = sensitivity)
        userPrefs.sensitivity = sensitivity
        backTapDetector.setSensitivity(sensitivity)
    }

    fun toggleHaptic(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isHapticEnabled = enabled)
        userPrefs.isHapticEnabled = enabled
    }

    fun setHapticStrength(strength: HapticStrength) {
        _uiState.value = _uiState.value.copy(hapticStrength = strength)
        userPrefs.hapticStrength = strength
    }

    fun toggleSound(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isSoundEnabled = enabled)
        userPrefs.isSoundEnabled = enabled
    }

    fun selectTheme(theme: TasbihTheme) {
        _uiState.value = _uiState.value.copy(selectedTheme = theme)
        userPrefs.selectedTheme = theme
    }

    fun toggleVolumeKeyCounting(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isVolumeKeyCountingEnabled = enabled)
        userPrefs.isVolumeKeyCountingEnabled = enabled
    }

    fun toggleKeepScreenAwake(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(keepScreenAwake = enabled)
        userPrefs.keepScreenAwake = enabled
    }

    fun createCustomDhikr(name: String, arabic: String, target: Int) {
        if (name.isBlank() && arabic.isBlank()) return
        viewModelScope.launch {
            val entity = CustomDhikrEntity(
                name = name.ifBlank { "Custom Dhikr" },
                arabic = arabic.ifBlank { "ذِكْر" },
                target = if (target > 0) target else 33
            )
            val newId = customDhikrDao.insertCustomDhikr(entity)
            val customItem = DhikrItem(
                id = "custom_$newId",
                name = entity.name,
                arabic = entity.arabic,
                meaning = "Custom",
                defaultTarget = entity.target,
                isCustom = true
            )
            selectDhikr(customItem)
        }
    }

    fun deleteCustomDhikr(entity: CustomDhikrEntity) {
        viewModelScope.launch {
            customDhikrDao.deleteCustomDhikr(entity.id)
            if (_uiState.value.activeDhikr.id == "custom_${entity.id}") {
                selectDhikr(DhikrPresets.PRESETS[0])
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            sessionDao.clearAllSessions()
        }
    }

    private fun triggerSensoryFeedback(isCompletion: Boolean) {
        if (_uiState.value.isHapticEnabled && vibrator != null && vibrator.hasVibrator()) {
            try {
                val strength = _uiState.value.hapticStrength
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (isCompletion) {
                        // Double pulse on round/target completion
                        val timing = longArrayOf(0, 35, 60, 45)
                        val amplitudes = intArrayOf(0, (strength.normalAmplitude * 1.2).toInt().coerceAtMost(255), 0, 255)
                        vibrator.vibrate(VibrationEffect.createWaveform(timing, amplitudes, -1))
                    } else {
                        // Subtle crisp single pulse on ordinary tap with user-selected strength
                        vibrator.vibrate(VibrationEffect.createOneShot(strength.normalDuration, strength.normalAmplitude))
                    }
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(if (isCompletion) 80 else strength.normalDuration)
                }
            } catch (e: Exception) {
                // Ignore vibration failure on unsupported devices
            }
        }

        if (_uiState.value.isSoundEnabled && audioManager != null) {
            try {
                audioManager.playSoundEffect(SoundEffectConstants.CLICK, 0.4f)
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
        viewModelScope.launch {
            sessionDao.insertSession(
                DhikrSessionEntity(
                    dhikrName = dhikrName,
                    dhikrArabic = dhikrArabic,
                    count = count,
                    target = target,
                    completed = completed,
                    round = round
                )
            )
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
}
