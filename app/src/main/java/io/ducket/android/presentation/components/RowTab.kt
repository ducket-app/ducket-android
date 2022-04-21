package io.ducket.android.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun AccountsSummaryTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable (() -> Unit),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {
    val styledText: @Composable (() -> Unit) = text.let {
        @Composable {
            val style = MaterialTheme.typography.button.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(style, content = text)
        }
    }
    Tab(
        selected,
        onClick,
        modifier,
        enabled,
        interactionSource,
        selectedContentColor,
        unselectedContentColor
    ) {
        AccountsSummaryTabBaselineLayout(text = styledText)
    }
}

@Composable
private fun AccountsSummaryTabBaselineLayout(
    text: @Composable (() -> Unit),
) {
    Layout(
        {
            Box(
                // padding is the only thing changed
                Modifier.layoutId("text")
            ) {
                text()
            }
        }
    ) { measurables, constraints ->
        val textPlaceable = text.let {
            measurables.first { it.layoutId == "text" }.measure(
                // Measure with loose constraints for height as we don't want the text to take up more
                // space than it needs
                constraints.copy(minHeight = 0)
            )
        }

        val tabWidth = textPlaceable.width
        val tabHeight = 48.dp.roundToPx()

        layout(tabWidth, tabHeight) {
            val contentY = (tabHeight - textPlaceable.height) / 2
            textPlaceable.placeRelative(0, contentY)
        }
    }
}