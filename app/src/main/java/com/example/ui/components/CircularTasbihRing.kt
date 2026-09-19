package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
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
    ringSize: Dp = 250.dp
) {
    val progress = when {
        isUnlimited -> 0f
        target <= 0 -> 0f
        else -> (count.toFloat() / target.toFloat()).coerceIn(0f, 1f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "TasbihProgress"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(ringSize)
            .shadow(elevation = 16.dp, shape = CircleShape, ambientColor = primaryColor.copy(alpha = 0.2f), spotColor = primaryColor.copy(alpha = 0.3f))
            .testTag("tasbih_counter_tap_area")
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, radius = ringSize / 2),
                onClick = onTap
            ),
        contentAlignment = Alignment.Center
    ) {
        // iOS Frosted Glass Disc Background
        Box(
            modifier = Modifier
                .size(ringSize - 20.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            surfaceColor.copy(alpha = 0.85f),
                            surfaceVariantColor.copy(alpha = 0.45f)
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.10f),
                    shape = CircleShape
                )
        )

        // Apple Activity Ring Style Canvas
        Canvas(modifier = Modifier.size(ringSize)) {
            val strokeWidth = 11.dp.toPx()
            val diameter = size.minDimension - strokeWidth - 6.dp.toPx()
            val radius = diameter / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // Deep background track (subtle translucent channel)
            drawCircle(
                color = surfaceVariantColor.copy(alpha = 0.4f),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // Track border line for crisp iOS glass depth
            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = radius + (strokeWidth / 2f),
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )

            // Radial Misbaha bead tick marks around track (33 beads standard division)
            val beadCount = if (target in 1..99) target.coerceIn(11, 33) else 33
            for (i in 0 until beadCount) {
                val angle = (i.toFloat() / beadCount) * (2 * PI.toFloat()) - (PI.toFloat() / 2f)
                val bx = center.x + radius * cos(angle)
                val by = center.y + radius * sin(angle)
                val isReached = !isUnlimited && (i.toFloat() / beadCount) <= animatedProgress

                drawCircle(
                    color = if (isReached) secondaryColor.copy(alpha = 0.95f) else Color.White.copy(alpha = 0.2f),
                    radius = if (i % 11 == 0) 3.5.dp.toPx() else 2.dp.toPx(),
                    center = Offset(bx, by)
                )
            }

            // Glowing iOS Activity Progress Arc with smooth end caps
            if (!isUnlimited && animatedProgress > 0f) {
                val brush = Brush.sweepGradient(
                    0.0f to primaryColor,
                    0.6f to secondaryColor,
                    1.0f to (if (isCompleted) secondaryColor else tertiaryColor),
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

        // Inner Content: Counter & iOS Pill Badge
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = stringResource(R.string.target_completed),
                    tint = secondaryColor,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            // SF Pro styled tabular bold digits
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = if (count >= 1000) 52.sp else 64.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-2).sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // iOS Frosted Target Status Capsule
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.12f))
            ) {
                if (!isUnlimited) {
                    Text(
                        text = stringResource(R.string.target_prefix, target),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = secondaryColor,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.unlimited_symbol),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = primaryColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

