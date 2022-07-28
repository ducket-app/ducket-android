package io.ducket.android.presentation.screens.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.annotation.Destination
import io.ducket.android.R
import io.ducket.android.presentation.components.AppBar
import io.ducket.android.presentation.navigation.StatsNavGraph

@StatsNavGraph(start = true)
@Destination
@Composable
fun StatsScreen2() {
    StatsScreenContent()
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatsScreen(
    navController: NavHostController,
    // viewModel: SignInViewModel = hiltViewModel()
) {
    StatsScreenContent()
}

@Composable
fun StatsScreenContent() {
    Column {
        AppBar(
            title = stringResource(id = R.string.stats_tab_title),
            elevation = 0.dp,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
        ) {
            Text(text = "Statistics screen")
        }
    }
}