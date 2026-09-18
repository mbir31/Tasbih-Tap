package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.model.DhikrPresets
import com.example.ui.screens.CounterScreen
import com.example.ui.theme.TasbihTapTheme
import com.example.viewmodel.TasbihUiState
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      TasbihTapTheme {
        CounterScreen(
          state = TasbihUiState(
            currentCount = 27,
            target = 33,
            round = 1,
            activeDhikr = DhikrPresets.PRESETS[0]
          ),
          onScreenTap = {},
          onUndo = {},
          onReset = {},
          onAdvanceRound = {},
          onOpenDhikrSheet = {},
          onOpenHistory = {},
          onOpenSettings = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
