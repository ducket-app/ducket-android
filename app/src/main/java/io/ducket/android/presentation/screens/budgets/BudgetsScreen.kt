package io.ducket.android.presentation.screens.budgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BudgetsScreen(
    navController: NavHostController,
    // viewModel: SignInViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budgets") },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 12.dp,
            )
        },
        content = {
            BudgetsScreenContent()
        }
    )
}

@Composable
fun BudgetsScreenContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Budgets screen", modifier = Modifier.align(Alignment.Center))
    }
}