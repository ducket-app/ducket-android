package io.ducket.android.presentation.screens.records

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.ducket.android.R
import io.ducket.android.presentation.components.AppBar
import io.ducket.android.presentation.navigation.RecordsNavGraph
import io.ducket.android.presentation.screens.destinations.RecordDetailsDestination

@RecordsNavGraph(start = true)
@Destination
@Composable
fun RecordsScreen2(
    navigator: DestinationsNavigator
) {
    RecordsScreenContent(
        onRecordDetailsClick = {
            navigator.navigate(RecordDetailsDestination)
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RecordsScreen(
    navController: NavHostController,
    onRecordClick: () -> Unit,
    // viewModel: SignInViewModel = hiltViewModel()
) {
    var checkBoxState by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Records") },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 12.dp,
            )
        },
        content = {
            RecordsScreenContent()
        }
    )
}

@Composable
fun RecordsScreenContent(
    onRecordDetailsClick: () -> Unit = {},
) {
    Column {
        AppBar(
            title = stringResource(id = R.string.records_tab_title),
            elevation = 0.dp,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
        ) {
            Text(
                modifier = Modifier.clickable {
                    onRecordDetailsClick()
                },
                text = "Records screen"
            )
        }
    }
}

@Composable
fun Record(
    title: String,
    caption: String,
    secondaryTitle: String,
    secondaryCaption: String,
    icon: ImageVector,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(RoundedCornerShape(8.dp))
        .clickable {

        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CardIcon(
                icon = icon,
                desc = "category_icon",
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                CardTitle(
                    text = title,
                )
                CardCaption(
                    text = caption,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.End,
            ) {
                CardTitle(
                    text = secondaryTitle,
                )
                CardCaption(
                    text = secondaryCaption,
                )
            }
        }
    }
}

@Composable
fun CardIcon(
    icon: ImageVector,
    desc: String,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Blue.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = desc,
            tint = Color.Blue,
        )
    }
}

@Composable
fun CardTitle(
    text: String,
    weight: FontWeight = FontWeight.Bold,
) {
    Text(
        text = text,
        maxLines = 1,
        fontWeight = weight,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.body1,
    )
}

@Composable
fun CardCaption(
    text: String
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    )
}