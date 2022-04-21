package io.ducket.android.presentation.screens.records

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
fun RecordCreateScreen(
    navController: NavHostController,
    // viewModel: SignInViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New record") },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 12.dp,
            )
        },
        content = {
            RecordCreateScreenContent()
        }
    )
}

@Composable
fun RecordCreateScreenContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Record create screen", modifier = Modifier.align(Alignment.Center))
    }
}