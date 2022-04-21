package io.ducket.android.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.ducket.android.presentation.navigation.Graph
import kotlinx.coroutines.delay

@Composable
fun LaunchScreen(
    navController: NavController,
    viewModel: LaunchViewModel = hiltViewModel()
) {
    val authState = viewModel.authState.observeAsState(false)

    LaunchedEffect(true) {
        delay(1000L)
        navController.popBackStack()

        if (authState.value) {
            // navController.navigate(Graph.Main.route)
            navController.navigate(Graph.Auth.route)
        } else {
            navController.navigate(Graph.Auth.route)
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
    ) {
        Text(text = "Splash screen")
    }
}