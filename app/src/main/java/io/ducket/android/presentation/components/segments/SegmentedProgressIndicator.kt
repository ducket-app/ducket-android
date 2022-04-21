package io.ducket.android.presentation.components.segments

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.*

private fun Float.fractional(): Float {
    return this - this.toInt()
}

@Composable
fun SegmentedProgressIndicator(
    modifier: Modifier = Modifier,
    @IntRange(from = 1) segmentsCount: Int,
    @FloatRange(from = 0.0) progress: Float = 0f,
    @FloatRange(from = 0.0) spacing: Dp = 0.dp,
    progressColor: Color = MaterialTheme.colors.primary,
    segmentColor: Color = progressColor.copy(alpha = ContentAlpha.disabled),
    progressAnimationSpec: AnimationSpec<Float> = tween(),
    // onProgressChanged: ((progress: Float, progressCoordinates: SegmentCoordinates) -> Unit)? = null,
    onProgressFinished: ((progress: Float) -> Unit)? = null,
) {
    val processor = remember { SegmentCoordinatesProcessor() }
    var progressCoordinates by remember { mutableStateOf(SegmentCoordinates(0f, 0f, 0f, 0f)) }
    val spacingPx = LocalDensity.current.run { spacing.toPx() }
    // var lastProgress by rememberSaveable { mutableStateOf(progress) }
    var staticProgress by rememberSaveable { mutableStateOf(0f) }

    // SideEffect {
    //     staticProgress = abs(if (lastProgress <= progress) lastProgress else progress)
    //     lastProgress = progress
    // }

    val progressRange = 0f..segmentsCount.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = progressAnimationSpec,
        finishedListener = onProgressFinished,
    )

    // re-writes static progress on every animated progress update
    // to cover the case when progress bar moves to another segment
    if (floor(animatedProgress) % 1f == 0f) {
        staticProgress = animatedProgress
    }

    Canvas(
        modifier = modifier
            .progressSemantics(
                value = progress,
                valueRange = progressRange
            )
            .fillMaxWidth()
            .clipToBounds(),
        onDraw = {
            (0 until segmentsCount).forEach { index ->
                val segmentCoordinates = processor.segmentCoordinates(
                    position = index,
                    segmentsCount = segmentsCount,
                    width = size.width,
                    spacing = spacingPx,
                )

                if (segmentCoordinates.topRightX.compareTo(progressCoordinates.topRightX) > 0) {
                    drawSegment(
                        coordinates = segmentCoordinates,
                        color = segmentColor,
                        alpha = 1f,
                    )
                }

                val segmentProgressRange = index.toFloat()..index + 1f
                val segmentProgress = when {
                    staticProgress <= segmentProgressRange.start -> 0f
                    staticProgress < segmentProgressRange.endInclusive -> staticProgress
                    else -> segmentProgressRange.endInclusive
                }

                val completedProgressCoordinates = processor.progressCoordinates(
                    progress = segmentProgress,
                    segmentsCount = segmentsCount,
                    width = size.width,
                    spacing = spacingPx,
                )

                drawSegment(
                    coordinates = completedProgressCoordinates,
                    color = progressColor,
                    alpha = 1f,
                )
            }

            progressCoordinates = processor.progressCoordinates(
                progress = animatedProgress.coerceIn(progressRange),
                segmentsCount = segmentsCount,
                width = size.width,
                spacing = spacingPx,
            )

            drawSegment(
                coordinates = progressCoordinates,
                color = progressColor,
                alpha = 1f,
            )

        }
    )
}

private fun DrawScope.drawSegment(
    coordinates: SegmentCoordinates,
    color: Color,
    alpha: Float,
) {
    val path = Path().apply {
        reset()
        moveTo(coordinates.topLeftX, 0f)
        lineTo(coordinates.topRightX, 0f)
        lineTo(coordinates.bottomRightX, size.height)
        lineTo(coordinates.bottomLeftX, size.height)
        close()
    }

    drawPath(
        path = path,
        color = color,
        alpha = alpha,
    )
}