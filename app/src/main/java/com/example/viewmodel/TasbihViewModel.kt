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
    val isScreenOffCountingEnabled: Boolean = true,
    val isScreenOffStealthTapEnabled: Boolean = true,
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
            keepScreenAwake = userPrefs.keepScreenAwake,
            isScreenOffCountingEnabled = userPrefs.isScreenOffCountingEnabled,
            isScreenOffStealthTapEnabled = userPrefs.isScreenOffStealthTapEnabled
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
        com.example.service.TasbihManager.initialize(application)
        viewModelScope.launch {
            com.example.service.TasbihManager.state.collect { state ->
                _uiState.value = state
                backTapDetector.setSensitivity(state.sensitivity)
            }
        }
        if (userPrefs.isBackTapEnabled) {
            backTapDetector.start()
        }
        if (userPrefs.isScreenOffCountingEnabled && userPrefs.isBackTapEnabled) {
            com.example.service.ScreenOffTasbihService.startService(application)
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
        com.example.service.TasbihManager.increment(fromBackTap = true)
    }

    fun increment(fromBackTap: Boolean = false) {
        com.example.service.TasbihManager.increment(fromBackTap)
    }

    fun decrement() {
        com.example.service.TasbihManager.decrement()
    }

    fun reset() {
        com.example.service.TasbihManager.reset()
    }

    fun advanceRound() {
        com.example.service.TasbihManager.advanceRound()
    }

    fun selectDhikr(dhikr: DhikrItem) {
        com.example.service.TasbihManager.selectDhikr(dhikr)
    }

    fun selectTarget(target: Int) {
        com.example.service.TasbihManager.selectTarget(target)
    }

    fun toggleUnlimitedMode(enabled: Boolean) {
        com.example.service.TasbihManager.toggleUnlimitedMode(enabled)
    }

    fun toggle33x3Mode(enabled: Boolean) {
        com.example.service.TasbihManager.toggle33x3Mode(enabled)
    }

    fun toggleBackTap(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isBackTapEnabled = enabled)
        userPrefs.isBackTapEnabled = enabled
        com.example.service.TasbihManager.toggleBackTap(enabled)
        if (enabled) {
            backTapDetector.start()
            if (_uiState.value.isScreenOffCountingEnabled) {
                com.example.service.ScreenOffTasbihService.startService(getApplication())
            }
        } else {
            backTapDetector.stop()
            com.example.service.ScreenOffTasbihService.stopService(getApplication())
        }
    }

    fun setSensitivity(sensitivity: Sensitivity) {
        _uiState.value = _uiState.value.copy(sensitivity = sensitivity)
        userPrefs.sensitivity = sensitivity
        backTapDetector.setSensitivity(sensitivity)
        com.example.service.TasbihManager.setSensitivity(sensitivity)
    }

    fun toggleHaptic(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isHapticEnabled = enabled)
        userPrefs.isHapticEnabled = enabled
        com.example.service.TasbihManager.toggleHaptic(enabled)
    }

    fun setHapticStrength(strength: HapticStrength) {
        _uiState.value = _uiState.value.copy(hapticStrength = strength)
        userPrefs.hapticStrength = strength
        com.example.service.TasbihManager.setHapticStrength(strength)
    }

    fun toggleSound(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isSoundEnabled = enabled)
        userPrefs.isSoundEnabled = enabled
        com.example.service.TasbihManager.toggleSound(enabled)
    }

    fun selectTheme(theme: TasbihTheme) {
        _uiState.value = _uiState.value.copy(selectedTheme = theme)
        userPrefs.selectedTheme = theme
        com.example.service.TasbihManager.selectTheme(theme)
    }

    fun toggleVolumeKeyCounting(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isVolumeKeyCountingEnabled = enabled)
        userPrefs.isVolumeKeyCountingEnabled = enabled
        com.example.service.TasbihManager.toggleVolumeKeyCounting(enabled)
    }

    fun toggleKeepScreenAwake(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(keepScreenAwake = enabled)
        userPrefs.keepScreenAwake = enabled
        com.example.service.TasbihManager.toggleKeepScreenAwake(enabled)
    }

    fun toggleScreenOffCounting(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isScreenOffCountingEnabled = enabled)
        userPrefs.isScreenOffCountingEnabled = enabled
        com.example.service.TasbihManager.toggleScreenOffCounting(enabled)
        if (enabled && _uiState.value.isBackTapEnabled) {
            com.example.service.ScreenOffTasbihService.startService(getApplication())
        } else if (!enabled) {
            com.example.service.ScreenOffTasbihService.stopService(getApplication())
        }
    }

    fun toggleScreenOffStealthTap(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isScreenOffStealthTapEnabled = enabled)
        userPrefs.isScreenOffStealthTapEnabled = enabled
        com.example.service.TasbihManager.toggleScreenOffStealthTap(enabled)
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
