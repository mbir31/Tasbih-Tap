package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class TasbihTheme(
    val id: String,
    val nameRes: Int,
    val descRes: Int,
    val previewBg: Color,
    val previewAccent: Color
) {
    EMERALD(
        "EMERALD",
        com.example.R.string.theme_emerald_name,
        com.example.R.string.theme_emerald_desc,
        Color(0xFF042116),
        Color(0xFFEAB308)
    ),
    MIDNIGHT(
        "MIDNIGHT",
        com.example.R.string.theme_midnight_name,
        com.example.R.string.theme_midnight_desc,
        Color(0xFF070D1B),
        Color(0xFF38BDF8)
    ),
    SAND(
        "SAND",
        com.example.R.string.theme_sand_name,
        com.example.R.string.theme_sand_desc,
        Color(0xFFF7F3EB),
        Color(0xFF78592B)
    ),
    NIGHT(
        "NIGHT",
        com.example.R.string.theme_night_name,
        com.example.R.string.theme_night_desc,
        Color(0xFF000000),
        Color(0xFF22C55E)
    ),
    ROSE_GOLD(
        "ROSE_GOLD",
        com.example.R.string.theme_rose_gold_name,
        com.example.R.string.theme_rose_gold_desc,
        Color(0xFF1F1118),
        Color(0xFFF472B6)
    ),
    ROYAL_AMBER(
        "ROYAL_AMBER",
        com.example.R.string.theme_royal_amber_name,
        com.example.R.string.theme_royal_amber_desc,
        Color(0xFF1C1304),
        Color(0xFFF59E0B)
    ),
    SYSTEM(
        "SYSTEM",
        com.example.R.string.theme_system_name,
        com.example.R.string.theme_system_desc,
        Color(0xFF1E293B),
        Color(0xFF10B981)
    )
}
