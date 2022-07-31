package io.ducket.android.presentation.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout

@ExperimentalMotionApi
@ExperimentalMaterialApi
@Composable
fun CollapsableToolbar() {
    val expandanceState = rememberSwipeableState(initialValue = ExpandanceState.EXPANDED)
    val scrollProgress = if (expandanceState.progress.to == ExpandanceState.COLLAPSED) {
        expandanceState.progress.fraction
    } else {
        1f - expandanceState.progress.fraction
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val heightInPx = with(LocalDensity.current) { maxHeight.toPx() } // Get height of screen
        val connection = remember {
            object : NestedScrollConnection {

                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        expandanceState.performDrag(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return expandanceState.performDrag(delta).toOffset()
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    expandanceState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .swipeable(
                    state = expandanceState,
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical,
                    anchors = mapOf(
                        // Maps anchor points (in px) to states
                        0f to ExpandanceState.COLLAPSED,
                        heightInPx to ExpandanceState.EXPANDED,
                    )
                )
                .nestedScroll(connection)
        ) {
            Column {
//                MotionLayoutHeader(
//                    progress = scrollProgress
//                ) {
//                    // ScrollableContent()
//                }
            }
        }
    }
}

enum class ExpandanceState {
    EXPANDED,
    COLLAPSED
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@ExperimentalMotionApi
@Composable
fun MotionLayoutHeader(
    title: String,
    progress: Float,
    animatableContent: @Composable () -> Unit,
    scrollableContent: @Composable () -> Unit
) {
    MotionLayout(
        start = JsonConstraintSetStart(),
        end = JsonConstraintSetEnd(),
        progress = progress,
        modifier = Modifier.fillMaxWidth()
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.poster),
//            contentDescription = "poster",
//            modifier = Modifier
//                .layoutId("poster")
//                .background(MaterialTheme.colors.primary),
//            contentScale = ContentScale.FillWidth,
//            alpha = 1f - progress
//        )
        Text(
            modifier = Modifier
                .layoutId("title")
                .wrapContentHeight(),
            text = title,
            // color = motionColor("title", "textColor"),
            color = MaterialTheme.colors.onSurface,
            fontSize = motionFontSize("title", "textSize"),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Box(
            Modifier.layoutId("content")
        ) {
            scrollableContent()
        }
    }
}

@Composable
private fun JsonConstraintSetStart() = ConstraintSet (""" {
	poster: { 
		width: "spread",
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['parent', 'top', 0],
	},
	title: {
		top: ['poster', 'bottom', 16],
		start: ['parent', 'start', 16],
		custom: {
			textColor: "#000000", 
			textSize: 40
		}
	},
	content: {
		width: "spread",
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['title', 'bottom', 16],
	}
} """ )

@Composable
private fun JsonConstraintSetEnd() = ConstraintSet (""" {
	poster: { 
		width: "spread",
		height: 56,
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['parent', 'top', 0],
	},
	title: {
		top: ['parent', 'top', 0],
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0], 
		bottom: ['poster', 'bottom', 0],
		custom: {
			textColor: "#ffffff",
			textSize: 20
        }
	},
	content: {
		width: "spread",
		start: ['parent', 'start', 0],
		end: ['parent', 'end', 0],
		top: ['poster', 'bottom', 0],
	}
                  
} """)