package io.ducket.android.presentation.screens.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun StatsScreen(
    navController: NavHostController,
    // viewModel: SignInViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 12.dp,
            )
        },
        content = {
            StatsScreenContent()
        }
    )
}

@Composable
fun StatsScreenContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Statistics screen", modifier = Modifier.align(Alignment.Center))
    }
}