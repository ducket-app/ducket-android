package io.ducket.android.presentation.screens.auth.start_balance

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import io.ducket.android.R
import io.ducket.android.common.extensions.clearFocusOnTapOutside
import io.ducket.android.presentation.components.*
import io.ducket.android.presentation.navigation.AppSnackbarManager
import io.ducket.android.presentation.navigation.AuthNavGraph
import io.ducket.android.presentation.screens.InputFieldState
import io.ducket.android.presentation.screens.NavGraphs
import io.ducket.android.presentation.screens.auth.sign_up.GetStartedUiEvent
import io.ducket.android.presentation.screens.destinations.*
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme
import io.ducket.android.presentation.ui.theme.SpaceMedium
import io.ducket.android.presentation.ui.theme.caption

@OptIn(ExperimentalComposeUiApi::class)
@AuthNavGraph
@Destination(navArgsDelegate = FinishSignUpScreenArgs::class)
@Composable
fun FinishSignUpScreen(
    viewModel: FinishSignUpViewModel = hiltViewModel(),
    snackbarManager: AppSnackbarManager,
    navController: NavController,
) {
    val uiState by viewModel.stateFlow.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is FinishSignUpUiEvent.ShowMessage -> {
                    snackbarManager.showMessage(it.text)
                }
                is FinishSignUpUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                is FinishSignUpUiEvent.NavigateToHome -> {
                    navController.navigate(HomeScreen2Destination) {
                        popUpTo(NavGraphs.root.route)
                    }
                }
            }
        }
    }

    FinishSignUpContent(
        state = uiState,
        scrollState = scrollState,
        onAccountNameChange = { viewModel.onEvent(FinishSignUpViewModel.Event.OnAccountNameChange(it)) },
        onStartBalanceChange = { viewModel.onEvent(FinishSignUpViewModel.Event.OnStartBalanceChange(it)) },
        onFinishButton = { viewModel.onEvent(FinishSignUpViewModel.Event.OnFinishClick) },
        onBackClick = { viewModel.onEvent(FinishSignUpViewModel.Event.OnBackClick) },
        onSkipClick = { viewModel.onEvent(FinishSignUpViewModel.Event.OnSkipClick) }
    )
}

@Composable
fun StartBalanceHelpDialog(
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(id = R.string.start_balance_helper_title),
                style = MaterialTheme.typography.h6,
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.start_balance_helper_message),
                style = MaterialTheme.typography.body1,
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(
                    text = stringResource(id = R.string.got_it_button),
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FinishSignUpContent(
    state: FinishSignUpUiState,
    scrollState: ScrollState,
    onAccountNameChange: (String) -> Unit,
    onStartBalanceChange: (String) -> Unit,
    onFinishButton: () -> Unit,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var showHelpDialog by remember { mutableStateOf(false) }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (showHelpDialog) {
        StartBalanceHelpDialog(
            onConfirmClick = { showHelpDialog = false },
            onDismiss = { showHelpDialog = false }
        )
    }

    Column(
        modifier = Modifier.clearFocusOnTapOutside(focusManager)
    ) {
        StartBalanceAppBar(
            onBackClick = onBackClick,
            onSkipClick = onSkipClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colors.surface)
                .padding(
                    start = SpaceMedium,
                    end = SpaceMedium,
                    bottom = SpaceMedium,
                ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.align(Alignment.Start),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.start_balance_title),
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = stringResource(id = R.string.start_balance_subtitle, state.currency),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.caption,
                )
            }

            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AccountNameTextField(
                    state = state.accountName,
                    onValueChange = onAccountNameChange,
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                BalanceTextField(
                    state = state.startBalance,
                    onHelpClick = {
                        showHelpDialog = true
                    },
                    onValueChange = onStartBalanceChange,
                    onImeAction = onFinishButton
                )
            }

            FinishButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormValid,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()

                    onFinishButton()
                },
            )
        }
    }
}


@Composable
fun StartBalanceAppBar(
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    AppBar(
        elevation = 0.dp,
        navigationButton = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = Icons.Default.ArrowBack.name,
                )
            }
        },
        actionButtons = {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        onSkipClick()
                    }
                    .padding(horizontal = SpaceMedium),
                text = stringResource(id = R.string.skip),
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.secondary,
                textAlign = TextAlign.End,
            )
        },
    )
}

@Composable
fun FinishButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    AppPrimaryButton(
        modifier = modifier,
        text = stringResource(id = R.string.finish_button),
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
fun AccountNameTextField(
    modifier: Modifier = Modifier,
    state: InputFieldState,
    onValueChange: (String) -> Unit,
    onImeAction: () -> Unit = {},
) {
    TextField(
        modifier = modifier,
        label = stringResource(id = R.string.account_name_label),
        value = state.text,
        error = state.error?.asString(),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onImeAction()
            }
        ),
    )
}

@Composable
fun BalanceTextField(
    modifier: Modifier = Modifier,
    state: InputFieldState,
    onHelpClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onImeAction: () -> Unit = {},
) {
    // TODO add visual transformation
    TextField(
        modifier = modifier,
        value = state.text,
        error = state.error?.asString(),
        label = stringResource(id = R.string.balance_label),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onImeAction()
            }
        ),
        trailingIcon = Icons.Default.Help,
        onTrailingIconClick = onHelpClick,
    )
}

@Preview
@Composable
private fun StartBalanceContentPreview() {
    DucketAndroidTheme {
        FinishSignUpContent(
            state = FinishSignUpUiState(),
            scrollState = rememberScrollState(),
            onAccountNameChange = {},
            onStartBalanceChange = {},
            onFinishButton = {},
            onBackClick = {},
            onSkipClick = {},
        )
    }
}