package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularTasbihRing(
    count: Int,
    target: Int,
    isUnlimited: Boolean,
    isCompleted: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    ringSize: Dp = 260.dp
) {
    val progress = when {
        isUnlimited -> 0f
        target <= 0 -> 0f
        else -> (count.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "TasbihProgress"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(ringSize)
            .testTag("tasbih_counter_tap_area")
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = ringSize / 2),
                onClick = onTap
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(ringSize)) {
            val strokeWidth = 10.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val radius = diameter / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // Background track
            drawCircle(
                color = surfaceVariantColor.copy(alpha = 0.45f),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // Draw subtle misbaha bead nodes around track (33 bead divisions)
            val beadCount = if (target in 1..99) target.coerceIn(11, 33) else 33
            for (i in 0 until beadCount) {
                val angle = (i.toFloat() / beadCount) * (2 * PI.toFloat()) - (PI.toFloat() / 2f)
                val bx = center.x + radius * cos(angle)
                val by = center.y + radius * sin(angle)
                val isReached = !isUnlimited && (i.toFloat() / beadCount) <= animatedProgress

                drawCircle(
                    color = if (isReached) secondaryColor.copy(alpha = 0.8f) else surfaceVariantColor.copy(alpha = 0.6f),
                    radius = if (i % 11 == 0) 3.5.dp.toPx() else 2.dp.toPx(),
                    center = Offset(bx, by)
                )
            }

            // Foreground progress arc
            if (!isUnlimited && animatedProgress > 0f) {
                val brush = Brush.sweepGradient(
                    0.0f to primaryColor,
                    0.7f to secondaryColor,
                    1.0f to tertiaryColor,
                    center = center
                )

                drawArc(
                    brush = brush,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Inner Content: Counter & Target
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = stringResource(R.string.target_completed),
                    tint = secondaryColor,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = if (count >= 1000) 54.sp else 66.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            if (!isUnlimited) {
                Text(
                    text = "/ $target",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                )
            } else {
                Text(
                    text = stringResource(R.string.unlimited_symbol),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
