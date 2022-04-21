package io.ducket.android.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ducket.android.presentation.ui.theme.SpaceSmall


@Composable
fun AppBar(
    title: String,
    elevation: Dp = 2.dp,
    navigationButton: @Composable (() -> Unit),
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = elevation,
        navigationIcon = navigationButton
    )
}

@Composable
fun ChildAppBar(
    title: String,
    elevation: Dp = 2.dp,
    actionIcon: ImageVector = Icons.Default.ArrowBack,
    onActionClick: () -> Unit,
) {
    AppBar(
        title = title,
        elevation = elevation,
        navigationButton = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIcon.name,
                    modifier = Modifier.padding(horizontal = SpaceSmall)
                )
            }
        }
    )
}