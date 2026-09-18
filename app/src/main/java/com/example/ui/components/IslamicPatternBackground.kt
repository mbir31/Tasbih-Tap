package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Programmatic, zero-asset Islamic geometric pattern.
 * Draws subtle 8-pointed stars (Rub el Hizb) with delicate connecting arabesque lines.
 * Reacts subtly to progress without distracting from the counter.
 */
@Composable
fun IslamicPatternBackground(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    tintColor: Color = MaterialTheme.colorScheme.secondary
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        // Progress subtly increases opacity from 0.04 to 0.12
        val dynamicAlpha = 0.04f + (progress.coerceIn(0f, 1f) * 0.08f)
        val strokeColor = tintColor.copy(alpha = dynamicAlpha)
        val strokeWidth = 1.2f

        // Draw central geometric medallion
        val centralRadius = (width * 0.42f).coerceAtMost(height * 0.35f)
        drawEightPointStar(
            center = Offset(centerX, centerY),
            outerRadius = centralRadius,
            innerRadius = centralRadius * 0.65f,
            strokeColor = strokeColor,
            strokeWidth = strokeWidth
        )

        // Draw outer concentric decorative rings
        drawCircle(
            color = strokeColor,
            radius = centralRadius * 0.95f,
            center = Offset(centerX, centerY),
            style = Stroke(width = strokeWidth)
        )
        drawCircle(
            color = strokeColor.copy(alpha = dynamicAlpha * 0.6f),
            radius = centralRadius * 1.15f,
            center = Offset(centerX, centerY),
            style = Stroke(width = strokeWidth * 0.8f)
        )

        // Draw corner geometric accents
        val cornerRadius = centralRadius * 0.45f
        val cornerOffsets = listOf(
            Offset(0f, 0f),
            Offset(width, 0f),
            Offset(0f, height),
            Offset(width, height)
        )

        cornerOffsets.forEach { corner ->
            drawCircle(
                color = strokeColor.copy(alpha = dynamicAlpha * 0.7f),
                radius = cornerRadius,
                center = corner,
                style = Stroke(width = strokeWidth)
            )
            drawCircle(
                color = strokeColor.copy(alpha = dynamicAlpha * 0.4f),
                radius = cornerRadius * 1.35f,
                center = corner,
                style = Stroke(width = strokeWidth * 0.7f)
            )
        }
    }
}

private fun DrawScope.drawEightPointStar(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    strokeColor: Color,
    strokeWidth: Float
) {
    val points = 16
    val angleStep = (2 * PI / points).toFloat()
    val path = Path()

    for (i in 0 until points) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * angleStep - (PI.toFloat() / 2f)
        val x = center.x + r * cos(angle)
        val y = center.y + r * sin(angle)

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()

    drawPath(
        path = path,
        color = strokeColor,
        style = Stroke(width = strokeWidth)
    )

    // Inner secondary star rotated by 22.5 degrees for arabesque depth
    val innerPath = Path()
    val innerOffsetAngle = (PI / 8).toFloat()
    val smallerOuter = outerRadius * 0.5f
    val smallerInner = innerRadius * 0.45f

    for (i in 0 until points) {
        val r = if (i % 2 == 0) smallerOuter else smallerInner
        val angle = i * angleStep + innerOffsetAngle - (PI.toFloat() / 2f)
        val x = center.x + r * cos(angle)
        val y = center.y + r * sin(angle)

        if (i == 0) {
            innerPath.moveTo(x, y)
        } else {
            innerPath.lineTo(x, y)
        }
    }
    innerPath.close()

    drawPath(
        path = innerPath,
        color = strokeColor.copy(alpha = strokeColor.alpha * 0.8f),
        style = Stroke(width = strokeWidth)
    )
}
