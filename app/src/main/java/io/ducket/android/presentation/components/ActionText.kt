package io.ducket.android.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color = MaterialTheme.colors.secondary,
    linkTextFontWeight: FontWeight = FontWeight.Normal,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    onLinkClick: (String) -> Unit,
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)

        linkText.forEachIndexed { i, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length

            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration,
                ),
                start = startIndex,
                end = endIndex,
            )

            addStringAnnotation(
                tag = "LINK",
                annotation = link,
                start = startIndex,
                end = endIndex,
            )
        }
    }

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        style = MaterialTheme.typography.body1.copy(
            color = MaterialTheme.colors.onSurface.copy(
                alpha = ContentAlpha.disabled
            )
        ),
        onClick = {
            annotatedString.getStringAnnotations("LINK", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    onLinkClick(stringAnnotation.item)
                }
        }
    )
}

@Composable
fun ActionText(
    text: String,
    link: String,
    linkTag: String = text.uppercase(),
    onActionClick: () -> Unit,
) {
    val annotatedString = buildAnnotatedString {
        val startIndex = text.indexOf(link)
        val endIndex = startIndex + link.length

        append(text)

        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.secondary,
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
            style = MaterialTheme.typography.body1.copy(
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = ContentAlpha.disabled
                )
            ),
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