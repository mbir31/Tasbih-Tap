package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.model.HapticStrength
import com.example.model.Sensitivity
import com.example.model.TasbihTheme

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("tasbih_tap_prefs", Context.MODE_PRIVATE)

    var currentCount: Int
        get() = prefs.getInt(KEY_CURRENT_COUNT, 0)
        set(value) = prefs.edit().putInt(KEY_CURRENT_COUNT, value).apply()

    var target: Int
        get() = prefs.getInt(KEY_TARGET, 33)
        set(value) = prefs.edit().putInt(KEY_TARGET, value).apply()

    var currentRound: Int
        get() = prefs.getInt(KEY_ROUND, 1)
        set(value) = prefs.edit().putInt(KEY_ROUND, value).apply()

    var activeDhikrId: String
        get() = prefs.getString(KEY_ACTIVE_DHIKR_ID, "subhanallah") ?: "subhanallah"
        set(value) = prefs.edit().putString(KEY_ACTIVE_DHIKR_ID, value).apply()

    var activeDhikrName: String
        get() = prefs.getString(KEY_ACTIVE_DHIKR_NAME, "SubhanAllah") ?: "SubhanAllah"
        set(value) = prefs.edit().putString(KEY_ACTIVE_DHIKR_NAME, value).apply()

    var activeDhikrArabic: String
        get() = prefs.getString(KEY_ACTIVE_DHIKR_ARABIC, "سُبْحَانَ اللَّهِ") ?: "سُبْحَانَ اللَّهِ"
        set(value) = prefs.edit().putString(KEY_ACTIVE_DHIKR_ARABIC, value).apply()

    var isUnlimitedMode: Boolean
        get() = prefs.getBoolean(KEY_UNLIMITED_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_UNLIMITED_MODE, value).apply()

    var is33x3Mode: Boolean
        get() = prefs.getBoolean(KEY_33X3_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_33X3_MODE, value).apply()

    var mode33x3Stage: Int
        get() = prefs.getInt(KEY_33X3_STAGE, 0)
        set(value) = prefs.edit().putInt(KEY_33X3_STAGE, value).apply()

    var isBackTapEnabled: Boolean
        get() = prefs.getBoolean(KEY_BACK_TAP_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_BACK_TAP_ENABLED, value).apply()

    var sensitivity: Sensitivity
        get() {
            val name = prefs.getString(KEY_SENSITIVITY, Sensitivity.MEDIUM.name)
            return try {
                Sensitivity.valueOf(name ?: Sensitivity.MEDIUM.name)
            } catch (e: Exception) {
                Sensitivity.MEDIUM
            }
        }
        set(value) = prefs.edit().putString(KEY_SENSITIVITY, value.name).apply()

    var isHapticEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTIC_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_HAPTIC_ENABLED, value).apply()

    var hapticStrength: HapticStrength
        get() {
            val name = prefs.getString(KEY_HAPTIC_STRENGTH, HapticStrength.MEDIUM.name)
            return try {
                HapticStrength.valueOf(name ?: HapticStrength.MEDIUM.name)
            } catch (e: Exception) {
                HapticStrength.MEDIUM
            }
        }
        set(value) = prefs.edit().putString(KEY_HAPTIC_STRENGTH, value.name).apply()

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_SOUND_ENABLED, value).apply()

    var selectedTheme: TasbihTheme
        get() {
            val name = prefs.getString(KEY_THEME, TasbihTheme.EMERALD.name)
            return try {
                TasbihTheme.valueOf(name ?: TasbihTheme.EMERALD.name)
            } catch (e: Exception) {
                TasbihTheme.EMERALD
            }
        }
        set(value) = prefs.edit().putString(KEY_THEME, value.name).apply()

    var isVolumeKeyCountingEnabled: Boolean
        get() = prefs.getBoolean(KEY_VOLUME_KEY_COUNTING, false)
        set(value) = prefs.edit().putBoolean(KEY_VOLUME_KEY_COUNTING, value).apply()

    var keepScreenAwake: Boolean
        get() = prefs.getBoolean(KEY_KEEP_SCREEN_AWAKE, true)
        set(value) = prefs.edit().putBoolean(KEY_KEEP_SCREEN_AWAKE, value).apply()

    var isScreenOffCountingEnabled: Boolean
        get() = prefs.getBoolean(KEY_SCREEN_OFF_COUNTING, true)
        set(value) = prefs.edit().putBoolean(KEY_SCREEN_OFF_COUNTING, value).apply()

    var isScreenOffStealthTapEnabled: Boolean
        get() = prefs.getBoolean(KEY_SCREEN_OFF_STEALTH_TAP, true)
        set(value) = prefs.edit().putBoolean(KEY_SCREEN_OFF_STEALTH_TAP, value).apply()

    companion object {
        private const val KEY_CURRENT_COUNT = "current_count"
        private const val KEY_TARGET = "target"
        private const val KEY_ROUND = "round"
        private const val KEY_ACTIVE_DHIKR_ID = "active_dhikr_id"
        private const val KEY_ACTIVE_DHIKR_NAME = "active_dhikr_name"
        private const val KEY_ACTIVE_DHIKR_ARABIC = "active_dhikr_arabic"
        private const val KEY_UNLIMITED_MODE = "unlimited_mode"
        private const val KEY_33X3_MODE = "mode_33x3"
        private const val KEY_33X3_STAGE = "stage_33x3"
        private const val KEY_BACK_TAP_ENABLED = "back_tap_enabled"
        private const val KEY_SENSITIVITY = "sensitivity"
        private const val KEY_HAPTIC_ENABLED = "haptic_enabled"
        private const val KEY_HAPTIC_STRENGTH = "haptic_strength"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_THEME = "selected_theme"
        private const val KEY_VOLUME_KEY_COUNTING = "volume_key_counting"
        private const val KEY_KEEP_SCREEN_AWAKE = "keep_screen_awake"
        private const val KEY_SCREEN_OFF_COUNTING = "screen_off_counting"
        private const val KEY_SCREEN_OFF_STEALTH_TAP = "screen_off_stealth_tap"
    }
}
