package com.example

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ui.screens.CounterScreen
import com.example.ui.screens.DhikrSelectionSheet
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.TasbihTapTheme
import com.example.viewmodel.TasbihViewModel

enum class CurrentScreen {
    COUNTER,
    HISTORY,
    SETTINGS
}

class MainActivity : ComponentActivity() {
    private val viewModel: TasbihViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val sessions by viewModel.allSessions.collectAsState()
            val customDhikrList by viewModel.customDhikrList.collectAsState()
            val todayDhikrCount by viewModel.todayDhikrCount.collectAsState()
            val todaySessionsCount by viewModel.todaySessionsCount.collectAsState()
            val completedSessionsCount by viewModel.completedSessionsCount.collectAsState()
            val allTimeDhikrCount by viewModel.allTimeDhikrCount.collectAsState()

            var currentScreen by remember { mutableStateOf(CurrentScreen.COUNTER) }
            var showDhikrSheet by remember { mutableStateOf(false) }

            // Handle Keep Screen Awake preference
            DisposableEffect(uiState.keepScreenAwake) {
                if (uiState.keepScreenAwake) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
                onDispose {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }

            // Lifecycle and screen observer to pause sensors when in background or on other screens
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner, currentScreen, uiState.isBackTapEnabled) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> {
                            if (currentScreen == CurrentScreen.COUNTER && uiState.isBackTapEnabled) {
                                viewModel.backTapDetector.start()
                            }
                        }
                        Lifecycle.Event.ON_PAUSE -> viewModel.onPause()
                        else -> {}
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)

                if (currentScreen == CurrentScreen.COUNTER && uiState.isBackTapEnabled) {
                    viewModel.backTapDetector.start()
                } else {
                    viewModel.backTapDetector.stop()
                }

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            TasbihTapTheme(selectedTheme = uiState.selectedTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                        when (screen) {
                            CurrentScreen.COUNTER -> {
                                CounterScreen(
                                    state = uiState,
                                    onScreenTap = { viewModel.increment(fromBackTap = false) },
                                    onUndo = { viewModel.decrement() },
                                    onReset = { viewModel.reset() },
                                    onAdvanceRound = { viewModel.advanceRound() },
                                    onOpenDhikrSheet = { showDhikrSheet = true },
                                    onOpenHistory = { currentScreen = CurrentScreen.HISTORY },
                                    onOpenSettings = { currentScreen = CurrentScreen.SETTINGS }
                                )
                            }

                            CurrentScreen.HISTORY -> {
                                HistoryScreen(
                                    sessions = sessions,
                                    todayDhikrCount = todayDhikrCount,
                                    todaySessionsCount = todaySessionsCount,
                                    completedSessionsCount = completedSessionsCount,
                                    allTimeDhikrCount = allTimeDhikrCount,
                                    onClearHistory = { viewModel.clearHistory() },
                                    onBack = { currentScreen = CurrentScreen.COUNTER }
                                )
                            }

                            CurrentScreen.SETTINGS -> {
                                SettingsScreen(
                                    isBackTapEnabled = uiState.isBackTapEnabled,
                                    sensitivity = uiState.sensitivity,
                                    isHapticEnabled = uiState.isHapticEnabled,
                                    hapticStrength = uiState.hapticStrength,
                                    isSoundEnabled = uiState.isSoundEnabled,
                                    selectedTheme = uiState.selectedTheme,
                                    keepScreenAwake = uiState.keepScreenAwake,
                                    volumeKeyCounting = uiState.isVolumeKeyCountingEnabled,
                                    backTapDetector = viewModel.backTapDetector,
                                    onToggleBackTap = { viewModel.toggleBackTap(it) },
                                    onSelectSensitivity = { viewModel.setSensitivity(it) },
                                    onToggleHaptic = { viewModel.toggleHaptic(it) },
                                    onSelectHapticStrength = { viewModel.setHapticStrength(it) },
                                    onToggleSound = { viewModel.toggleSound(it) },
                                    onSelectTheme = { viewModel.selectTheme(it) },
                                    onToggleKeepScreenAwake = { viewModel.toggleKeepScreenAwake(it) },
                                    onToggleVolumeKeyCounting = { viewModel.toggleVolumeKeyCounting(it) },
                                    onBack = { currentScreen = CurrentScreen.COUNTER }
                                )
                            }
                        }
                    }

                    if (showDhikrSheet) {
                        DhikrSelectionSheet(
                            activeDhikr = uiState.activeDhikr,
                            currentTarget = uiState.target,
                            isUnlimited = uiState.isUnlimited,
                            is33x3Mode = uiState.is33x3Mode,
                            customDhikrList = customDhikrList,
                            onSelectDhikr = { viewModel.selectDhikr(it) },
                            onSelectTarget = { viewModel.selectTarget(it) },
                            onToggleUnlimited = { viewModel.toggleUnlimitedMode(it) },
                            onToggle33x3Mode = { viewModel.toggle33x3Mode(it) },
                            onCreateCustomDhikr = { name, arabic, target ->
                                viewModel.createCustomDhikr(name, arabic, target)
                            },
                            onDeleteCustomDhikr = { viewModel.deleteCustomDhikr(it) },
                            onDismiss = { showDhikrSheet = false }
                        )
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (viewModel.uiState.value.isVolumeKeyCountingEnabled && event?.repeatCount == 0) {
            when (keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    viewModel.increment(fromBackTap = false)
                    return true
                }
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    viewModel.decrement()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
