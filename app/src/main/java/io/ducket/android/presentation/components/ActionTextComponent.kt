package io.ducket.android.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun ActionText(
    text: String,
    link: String,
    linkTag: String = text.uppercase(),
    onActionClick: () -> Unit,
) {
    val annotatedString = buildAnnotatedString {
        val fullString = "$text $link"
        val startIndex = fullString.indexOf(link)
        val endIndex = startIndex + link.length

        append(fullString)

        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.primary,
                textDecoration = TextDecoration.Underline,
            ),
            start = startIndex,
            end = endIndex,
        )

        addStringAnnotation(
            tag = linkTag,
            annotation = link,
            start = startIndex,
            end = endIndex,
        )
    }

    Box(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
            onClick = {
                annotatedString
                    .getStringAnnotations(linkTag, it, it)
                    .firstOrNull()?.let {
                        onActionClick()
                    }
            }
        )
    }
}