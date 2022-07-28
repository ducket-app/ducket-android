package io.ducket.android.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.ducket.android.presentation.screens.NavGraphs
import kotlinx.coroutines.delay

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@RootNavGraph(start = true)
@Destination
@Composable
fun Splash(
    viewModel: SplashViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val isAuthorized by viewModel.authorized.collectAsState()
    // val startRoute = if (isAuthorized) NavGraphs.tabs.route else NavGraphs.auth.route
    val startRoute = NavGraphs.auth.route

    SplashScreenContent(
        onFinish = {
            // TODO
            navigator.navigate(startRoute) {
                popUpTo(NavGraphs.root.route) {
                    inclusive = true
                }
            }
        }
    )
}

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onFinish: (isAuthorized: Boolean) -> Unit,
) {
    val isAuthorized by viewModel.authorized.collectAsState()

    SplashScreenContent(
        onFinish = {
            onFinish(isAuthorized)
        }
    )
}

@Composable
fun SplashScreenContent(
    onFinish: () -> Unit,
) {
    LaunchedEffect(true) {
        delay(1000L)
        onFinish()
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