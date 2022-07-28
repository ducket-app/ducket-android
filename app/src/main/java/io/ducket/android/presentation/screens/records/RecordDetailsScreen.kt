package io.ducket.android.presentation.screens.records

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
import io.ducket.android.presentation.navigation.HomeNavGraph
import io.ducket.android.presentation.navigation.RecordsNavGraph

@RecordsNavGraph
@HomeNavGraph
@Destination
@Composable
fun RecordDetails() {
    RecordDetailsScreenContent()
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RecordDetailsScreen(
    navController: NavHostController,
    // viewModel: SignInViewModel = hiltViewModel()
) {
    RecordDetailsScreenContent()
}

@Composable
fun RecordDetailsScreenContent() {
    Column {
        AppBar(
            title = stringResource(id = R.string.record_details_title),
            elevation = 0.dp,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
        ) {
            Text(text = "Record details screen")
        }
    }
}