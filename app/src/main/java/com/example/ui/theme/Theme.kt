package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.TasbihTheme

private val EmeraldDarkColorScheme = darkColorScheme(
    primary = EmeraldPrimaryDark,
    onPrimary = EmeraldBgDark,
    primaryContainer = EmeraldSurfaceVariantDark,
    onPrimaryContainer = EmeraldGoldLight,
    secondary = EmeraldAccentGold,
    onSecondary = EmeraldBgDark,
    background = EmeraldBgDark,
    onBackground = EmeraldTextDark,
    surface = EmeraldSurfaceDark,
    onSurface = EmeraldTextDark,
    surfaceVariant = EmeraldSurfaceVariantDark,
    onSurfaceVariant = EmeraldTextSecondaryDark,
    tertiary = EmeraldGoldLight
)

private val EmeraldLightColorScheme = lightColorScheme(
    primary = EmeraldPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = EmeraldSurfaceVariantLight,
    onPrimaryContainer = EmeraldTextLight,
    secondary = EmeraldAccentGold,
    onSecondary = Color.Black,
    background = EmeraldBgLight,
    onBackground = EmeraldTextLight,
    surface = EmeraldSurfaceLight,
    onSurface = EmeraldTextLight,
    surfaceVariant = EmeraldSurfaceVariantLight,
    onSurfaceVariant = EmeraldTextSecondaryLight,
    tertiary = EmeraldAccentGold
)

private val MidnightColorScheme = darkColorScheme(
    primary = MidnightPrimary,
    onPrimary = MidnightBg,
    primaryContainer = MidnightSurfaceVariant,
    onPrimaryContainer = MidnightText,
    secondary = MidnightSecondary,
    onSecondary = MidnightBg,
    background = MidnightBg,
    onBackground = MidnightText,
    surface = MidnightSurface,
    onSurface = MidnightText,
    surfaceVariant = MidnightSurfaceVariant,
    onSurfaceVariant = MidnightTextSecondary,
    tertiary = MidnightAccentGold
)

private val SandColorScheme = lightColorScheme(
    primary = SandPrimary,
    onPrimary = Color.White,
    primaryContainer = SandSurfaceVariant,
    onPrimaryContainer = SandText,
    secondary = SandSecondary,
    onSecondary = Color.White,
    background = SandBg,
    onBackground = SandText,
    surface = SandSurface,
    onSurface = SandText,
    surfaceVariant = SandSurfaceVariant,
    onSurfaceVariant = SandTextSecondary,
    tertiary = SandAccent
)

private val NightColorScheme = darkColorScheme(
    primary = NightPrimary,
    onPrimary = Color.Black,
    primaryContainer = NightSurfaceVariant,
    onPrimaryContainer = NightText,
    secondary = NightAccentGold,
    onSecondary = Color.Black,
    background = NightBg,
    onBackground = NightText,
    surface = NightSurface,
    onSurface = NightText,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = NightTextSecondary,
    tertiary = NightSecondary
)

private val RoseGoldColorScheme = darkColorScheme(
    primary = RoseGoldPrimary,
    onPrimary = RoseGoldBg,
    primaryContainer = RoseGoldSurfaceVariant,
    onPrimaryContainer = RoseGoldText,
    secondary = RoseGoldSecondary,
    onSecondary = RoseGoldBg,
    background = RoseGoldBg,
    onBackground = RoseGoldText,
    surface = RoseGoldSurface,
    onSurface = RoseGoldText,
    surfaceVariant = RoseGoldSurfaceVariant,
    onSurfaceVariant = RoseGoldTextSecondary,
    tertiary = RoseGoldAccent
)

private val RoyalAmberColorScheme = darkColorScheme(
    primary = RoyalAmberPrimary,
    onPrimary = RoyalAmberBg,
    primaryContainer = RoyalAmberSurfaceVariant,
    onPrimaryContainer = RoyalAmberText,
    secondary = RoyalAmberSecondary,
    onSecondary = RoyalAmberBg,
    background = RoyalAmberBg,
    onBackground = RoyalAmberText,
    surface = RoyalAmberSurface,
    onSurface = RoyalAmberText,
    surfaceVariant = RoyalAmberSurfaceVariant,
    onSurfaceVariant = RoyalAmberTextSecondary,
    tertiary = RoyalAmberAccent
)

@Composable
fun TasbihTapTheme(
    selectedTheme: TasbihTheme = TasbihTheme.EMERALD,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when (selectedTheme) {
        TasbihTheme.EMERALD -> if (darkTheme) EmeraldDarkColorScheme else EmeraldDarkColorScheme
        TasbihTheme.MIDNIGHT -> MidnightColorScheme
        TasbihTheme.SAND -> SandColorScheme
        TasbihTheme.NIGHT -> NightColorScheme
        TasbihTheme.ROSE_GOLD -> RoseGoldColorScheme
        TasbihTheme.ROYAL_AMBER -> RoyalAmberColorScheme
        TasbihTheme.SYSTEM -> if (darkTheme) EmeraldDarkColorScheme else EmeraldLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
