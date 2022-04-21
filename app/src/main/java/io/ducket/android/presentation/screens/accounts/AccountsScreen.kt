package io.ducket.android.presentation.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.ducket.android.presentation.ui.theme.subtitle

@Composable
fun AccountsScreen(
    navController: NavHostController,
    // viewModel: SignInViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Accounts") },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 12.dp,
            )
        },
        content = {
            AccountsScreenContent()
        }
    )
}

@Preview
@Composable
fun AccountsScreenContent() {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Accounts screen", modifier = Modifier.align(Alignment.Center))
    }
}

@Preview
@Composable
fun AccountsSummary() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
    ) {
        Column {
            SummaryHeader()

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    AccountsSelectAll()

                    Spacer(modifier = Modifier.width(16.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(items = listOf("Millennium bank", "IGN", "Cash")) {
                            AccountsSummaryItem()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                InfoBadge()
            }
        }
    }
}

@Preview
@Composable
fun InfoBadge() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.error,
                shape = RoundedCornerShape(4.dp),
            )
            .background(color = MaterialTheme.colors.primary)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Overview balance:",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )

        Text(
            text = "zł 52,320.00",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Preview
@Composable
fun AccountsSelectAll() {
    val selected = remember { mutableStateOf(false) }

    val borderColor = if (selected.value) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.subtitle
    }

    Box(
        modifier = Modifier
            .height(64.dp)
            .wrapContentWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.PlaylistAddCheck,
            contentDescription = "select_all",
            tint = borderColor
        )
    }
}

@Preview
@Composable
fun AccountsSummaryItem() {
    val selected = remember { mutableStateOf(false) }

    val borderColor = if (selected.value) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.subtitle
    }

    Box(
        modifier = Modifier
            .height(64.dp)
            .wrapContentWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(12.dp)
            .clickable {
                selected.value = !selected.value
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
//            Checkbox(
//                checked = selected.value,
//                onCheckedChange = {
//                    selected.value = !selected.value
//                },
//                colors = CheckboxDefaults.colors(
//                    uncheckedColor = MaterialTheme.colors.caption,
//                )
//            )
            Column {
                Text(
                    text = "Millennium bank",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.subtitle
                )

                Text(
                    text = "zł 51,320.00",
                    maxLines = 1,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

@Preview
@Composable
fun SummaryHeader() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Accounts",
                maxLines = 1,
                fontWeight = FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1,
            )
            Text(
                text = "View all",
                maxLines = 1,
                fontWeight = FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary,
            )
        }
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colors.subtitle
        )
    }
}