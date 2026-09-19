package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.DhikrPresets
import com.example.ui.components.CircularTasbihRing
import com.example.ui.components.IslamicPatternBackground
import com.example.viewmodel.TasbihUiState

@Composable
fun CounterScreen(
    state: TasbihUiState,
    onScreenTap: () -> Unit,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    onAdvanceRound: () -> Unit,
    onOpenDhikrSheet: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showResetDialog by remember { mutableStateOf(false) }

    val progress = when {
        state.isUnlimited -> 0f
        state.target <= 0 -> 0f
        else -> (state.currentCount.toFloat() / state.target.toFloat()).coerceIn(0f, 1f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Dynamic Islamic geometric pattern background
        IslamicPatternBackground(
            progress = progress,
            tintColor = MaterialTheme.colorScheme.secondary
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- TOP NAVIGATION BAR: iOS Capsule Action Cards ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val dhikrTitle = if (state.is33x3Mode) {
                    stringResource(R.string.misbaha_33x3)
                } else {
                    state.activeDhikr.getLocalizedName(context)
                }

                // iOS Frosted Capsule Pill: Current Dhikr & Target
                Card(
                    onClick = onOpenDhikrSheet,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), ambientColor = Color.Black.copy(alpha = 0.3f))
                        .testTag("dhikr_selector_pill"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.12f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (state.is33x3Mode) "33" else state.activeDhikr.arabic.take(1),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    text = dhikrTitle,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.5.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (state.is33x3Mode) stringResource(R.string.mode_33x3_title) else if (state.isUnlimited) stringResource(R.string.unlimited) else stringResource(R.string.target_prefix, state.target),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.95f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.select_dhikr),
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Stealth / Screen-Off Mode Action Button
                IosCircularActionButton(
                    icon = Icons.Default.DarkMode,
                    contentDesc = stringResource(R.string.enter_screen_off_mode),
                    testTag = "open_screen_off_button",
                    onClick = {
                        val intent = android.content.Intent(context, com.example.ScreenOffActivity::class.java)
                        context.startActivity(intent)
                    }
                )

                // History Action Button
                IosCircularActionButton(
                    icon = Icons.Default.History,
                    contentDesc = stringResource(R.string.history),
                    testTag = "open_history_button",
                    onClick = onOpenHistory
                )

                // Settings & Customization Action Button
                IosCircularActionButton(
                    icon = Icons.Default.Tune,
                    contentDesc = stringResource(R.string.settings),
                    testTag = "open_settings_button",
                    onClick = onOpenSettings
                )
            }

            // --- iOS FROSTED PRESENTATION CARD (Dhikr, Meaning, Sunnah Progress) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.10f)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    // Arabic script with adaptive sizing (never pushes ring offscreen)
                    val arabicText = state.activeDhikr.arabic
                    val fontSize = when {
                        arabicText.length > 80 -> 18.sp
                        arabicText.length > 40 -> 22.sp
                        arabicText.length > 20 -> 26.sp
                        else -> 30.sp
                    }
                    val lineHeight = when {
                        arabicText.length > 80 -> 26.sp
                        arabicText.length > 40 -> 30.sp
                        else -> 36.sp
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 84.dp)
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = arabicText,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = fontSize,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary,
                                lineHeight = lineHeight
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Transliteration / Name
                    Text(
                        text = state.activeDhikr.getLocalizedName(context),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Localized Meaning (Crucial user requested improvement)
                    val localizedMeaning = state.activeDhikr.getLocalizedMeaning(context)
                    if (localizedMeaning.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = localizedMeaning,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                                fontSize = 11.5.sp,
                                lineHeight = 15.sp
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Progress / Round indicator
                    Spacer(modifier = Modifier.height(6.dp))
                    if (state.is33x3Mode) {
                        // 33x3 Sunnah Stage Pill Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 0.5.dp,
                                    color = Color.White.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            val stagePresets = DhikrPresets.THIRTY_THREE_TIMES_THREE
                            stagePresets.forEachIndexed { index, preset ->
                                val count = state.mode33x3Counts[index]
                                val isCurrent = state.mode33x3Stage == index
                                val stageTarget = if (index == 2) 34 else 33
                                val isDone = count >= stageTarget
                                val stageName = preset.getLocalizedName(context)
                                val shortName = if (stageName.length > 6) stageName.take(5) else stageName

                                Text(
                                    text = "$shortName: $count" + (if (isDone) "✓" else ""),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (index < stagePresets.size - 1) {
                                    Text(
                                        text = " • ",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                                    )
                                }
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.08f))
                        ) {
                            Text(
                                text = if (state.isUnlimited) stringResource(R.string.round_unlimited) else stringResource(R.string.round_format, state.round),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // --- CENTRAL CIRCULAR TASBIH RING (iOS Watch Activity Ring) ---
            CircularTasbihRing(
                count = state.currentCount,
                target = state.target,
                isUnlimited = state.isUnlimited,
                isCompleted = state.isCompleted,
                onTap = onScreenTap,
                ringSize = 236.dp
            )

            // --- INTERACTION GUIDANCE / STATUS CAPSULE ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (state.isBackTapEnabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.45f) else Color.White.copy(alpha = 0.08f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    color = if (state.isBackTapEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (state.isBackTapEnabled) stringResource(R.string.tap_back_to_count) else stringResource(R.string.tap_screen_to_count),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.8.sp,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                // If completed, provide Next Round button
                AnimatedVisibility(
                    visible = state.isCompleted && !state.isUnlimited,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FilledTonalButton(
                        onClick = onAdvanceRound,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .testTag("next_round_button"),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(stringResource(R.string.target_reached_next_round), style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // --- BOTTOM ACTIONS: iOS Glass Undo (−) and Reset (↻) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Decrement / Undo Action Button
                IosGlassButton(
                    icon = Icons.Default.Remove,
                    contentDesc = stringResource(R.string.undo_count),
                    enabled = state.currentCount > 0,
                    testTag = "undo_button",
                    onClick = onUndo
                )

                // Reset Action Button
                IosGlassButton(
                    icon = Icons.Default.Refresh,
                    contentDesc = stringResource(R.string.reset_counter),
                    enabled = true,
                    testTag = "reset_button",
                    onClick = { showResetDialog = true }
                )
            }

            // Discreet mandatory developer credit
            Text(
                text = stringResource(R.string.app_credit),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }

    // Deliberate Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.reset_dialog_title), fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.reset_dialog_message)) },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            confirmButton = {
                Button(
                    onClick = {
                        onReset()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.reset))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun IosCircularActionButton(
    icon: ImageVector,
    contentDesc: String,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(52.dp)
            .shadow(elevation = 6.dp, shape = CircleShape)
            .testTag(testTag),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.12f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDesc,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun IosGlassButton(
    icon: ImageVector,
    contentDesc: String,
    enabled: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        onClick = { if (enabled) onClick() },
        modifier = Modifier
            .size(54.dp)
            .shadow(elevation = if (enabled) 8.dp else 2.dp, shape = CircleShape)
            .testTag(testTag),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (enabled) 0.70f else 0.30f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (enabled) Color.White.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.05f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDesc,
                tint = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

