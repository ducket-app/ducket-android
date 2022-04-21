package io.ducket.android.presentation.screens.auth.sign_up

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import com.google.accompanist.pager.ExperimentalPagerApi
import io.ducket.android.R
import io.ducket.android.presentation.states.*
import io.ducket.android.presentation.components.*
import io.ducket.android.presentation.components.segments.SegmentedProgressIndicator
import io.ducket.android.presentation.navigation.*
import io.ducket.android.presentation.navigation.NavArgKey.ARG_CURRENCY_ISO_CODE
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme
import io.ducket.android.presentation.ui.theme.SpaceMedium
import io.ducket.android.presentation.ui.theme.SpaceSmall
import io.ducket.android.presentation.ui.theme.subtitle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    scaffoldState: ScaffoldState,
    appSnackbarManager: AppSnackbarManager,
    navigateBack: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    navigateToSignInScreen: () -> Unit,
    navigateToCurrencySelectionScreen: (String) -> Unit,
) {
    val screenState = rememberSignUpScreenState()
    val uiState by viewModel.uiState.collectAsState()
    val showStartingBalanceHelpDialog by viewModel.showStartingBalanceHelpDialog.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
//    val selectedCurrency by savedStateHandle.getLiveData<String>(ARG_CURRENCY_ISO_CODE).observeAsState("")

    screenState.currencyFieldState.value = selectedCurrency
    screenState.signUpButtonState.enabled = screenState.signUpFormValid() && uiState !is SignUpViewModel.UiState.Loading

    LaunchedEffect(Unit) {
        viewModel.screenEvent.collect {
            if (it is SignUpViewModel.ScreenEvent.ShowMessage) {
                appSnackbarManager.showMessage(it.text)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SignUpAppBar(onCloseClick = navigateBack)
        },
        content = {
            when (uiState) {
                is SignUpViewModel.UiState.Loading,
                is SignUpViewModel.UiState.NotSignedUp -> {
                    if (uiState is SignUpViewModel.UiState.Loading) {
                        screenState.toggleControls(false)
                        LoadingDialog()
                    } else {
                        screenState.toggleControls(true)
                    }

                    SignUpContent(
                        nameFieldState = screenState.nameFieldState,
                        emailFieldState = screenState.emailFieldState,
                        currencyFieldState = screenState.currencyFieldState,
                        balanceFieldState = screenState.balanceFieldState,
                        passwordFieldState = screenState.passwordFieldState,
                        confirmPasswordFieldState = screenState.confirmPasswordFieldState,
                        signUpButtonState = screenState.signUpButtonState,
                        showStartingBalanceHelpDialog = showStartingBalanceHelpDialog,
                        onStartingBalanceHelpClick = {
                            viewModel.onStartingBalanceHelpClick()
                        },
                        onStartingBalanceHelpDialogDismiss = {
                            viewModel.onStartingBalanceHelpDialogDismiss()
                        },
                        onSelectCurrencyFieldClick = navigateToCurrencySelectionScreen,
                        onSignInLinkClick = navigateToSignInScreen,
                        onSignUpClick = {
                            viewModel.onSignUp(
                                name = screenState.nameFieldState.value,
                                email = screenState.emailFieldState.value,
                                currencyCode = screenState.currencyFieldState.value,
                                // startBalance = screenState.balanceFieldState.value,
                                password = screenState.passwordFieldState.value,
                            )
                        },
                    )
                }
                is SignUpViewModel.UiState.SignedUp -> {
                    // appState.navHostController.navigate(Graph.Main.route) {
                    //     popUpTo(Graph.Host.route)
                    // }
                }
            }
        }
    )
}

@Composable
fun SignUpAppBar(
    onCloseClick: () -> Unit,
) {
    ChildAppBar(
        title = stringResource(id = R.string.sign_up_title),
        elevation = 0.dp,
        onActionClick = onCloseClick,
        actionIcon = Icons.Default.Close,
    )
}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SignUpContent(
    nameFieldState: NameFieldState,
    emailFieldState: EmailFieldState,
    currencyFieldState: TextFieldState,
    balanceFieldState: TextFieldState,
    showStartingBalanceHelpDialog: Boolean,
    passwordFieldState: PasswordFieldState,
    confirmPasswordFieldState: ConfirmPasswordFieldState,
    signUpButtonState: ButtonState,
    onSignUpClick: () -> Unit,
    onStartingBalanceHelpDialogDismiss: () -> Unit,
    onStartingBalanceHelpClick: () -> Unit,
    onSelectCurrencyFieldClick: (String) -> Unit,
    onSignInLinkClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(SpaceMedium),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val scrollState = rememberScrollState()

        if (showStartingBalanceHelpDialog) {
            AlertDialog(
                title = {
                    Text(
                        text = stringResource(id = R.string.start_balance_help_heading),
                        style = MaterialTheme.typography.h6,
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.start_balance_help_helper),
                        style = MaterialTheme.typography.body1,
                    )
                },
                onDismissRequest = onStartingBalanceHelpDialogDismiss,
                confirmButton = {
                    TextButton(onClick = onStartingBalanceHelpDialogDismiss) {
                        Text(
                            text = stringResource(id = R.string.got_it_button),
                            style = MaterialTheme.typography.subtitle2,
                        )
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Start),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up_heading),
                    style = MaterialTheme.typography.h4,
                )
                Text(
                    text = stringResource(id = R.string.sign_up_subheading),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.subtitle,
                )
            }

            Spacer(modifier = Modifier.height(SpaceSmall))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(SpaceSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                NameTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = nameFieldState,
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                EmailTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = emailFieldState,
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SpaceMedium),
                ) {
                    CurrencyTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        state = currencyFieldState,
                        onClick = onSelectCurrencyFieldClick
                    )

                    BalanceTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        state = balanceFieldState,
                        onHelpClick = onStartingBalanceHelpClick,
                        onImeAction = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                }

                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = passwordFieldState,
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                ConfirmPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = confirmPasswordFieldState,
                    onImeAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()

                        onSignUpClick()
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SpaceMedium),
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.sign_up_button),
                    state = signUpButtonState,
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()

                        onSignUpClick()
                    },
                )

                ActionText(
                    text = stringResource(id = R.string.sign_up_helper),
                    link = stringResource(id = R.string.sign_in_title),
                    onActionClick = onSignInLinkClick,
                )
            }
        }
    }
}

@Composable
fun NameTextField(
    modifier: Modifier = Modifier,
    state: NameFieldState,
    onImeAction: () -> Unit = {},
) {
    AppTextField(
        modifier = modifier,
        state = state,
        error = stringResource(id = R.string.invalid_name_error),
        label = {
            Text(stringResource(id = R.string.name_label))
        },
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
fun EmailTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onImeAction: () -> Unit = {},
) {
    AppTextField(
        modifier = modifier,
        state = state,
        error = stringResource(id = R.string.invalid_email_error),
        label = {
            Text(stringResource(id = R.string.email_label))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
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
fun CurrencyTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onClick: (String) -> Unit,
) {
    SelectableTextField(
        modifier = modifier,
        state = state,
        label = stringResource(id = R.string.currency_label),
        placeholder = stringResource(id = R.string.select_currency_placeholder),
        trailingIcon = Icons.Default.ChevronRight,
        onClick = {
            onClick(state.value)
        }
    )
}

@Composable
fun BalanceTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onHelpClick: () -> Unit,
    onImeAction: () -> Unit = {},
) {
    AppTextField(
        modifier = modifier,
        state = state,
        error = stringResource(id = R.string.invalid_balance_error),
        label = {
            Text(stringResource(id = R.string.balance_label))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onImeAction()
            }
        ),
        trailingIcon = {
            IconButton(onClick = onHelpClick) {
                Icon(
                    imageVector = Icons.Default.Help,
                    contentDescription = stringResource(id = R.string.start_balance_desc),
                )
            }
        }
    )
}

@ExperimentalComposeUiApi
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    state: PasswordFieldState,
    onImeAction: () -> Unit = {},
) {
    ProtectedTextField(
        modifier = modifier,
        state = state,
        error = stringResource(id = R.string.invalid_password_error),
        label = {
            Text(stringResource(id = R.string.password_label))
        },
        keyboardActions = KeyboardActions(
            onNext = {
                onImeAction()
            }
        ),
    )
}

@ExperimentalComposeUiApi
@Composable
fun ConfirmPasswordTextField(
    modifier: Modifier = Modifier,
    state: ConfirmPasswordFieldState,
    onImeAction: () -> Unit = {},
) {
    ProtectedTextField(
        modifier = modifier,
        state = state,
        error = stringResource(id = R.string.invalid_confirm_password_error),
        label = {
            Text(stringResource(id = R.string.confirm_password_label))
        },
        imeAction = ImeAction.Next,
        keyboardActions = KeyboardActions(
            onNext = {
                onImeAction()
            }
        ),
    )
}

@Preview
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
private fun SignUpScreenPreview() {
    DucketAndroidTheme {
//        SignUpScreen(
//            appState = AppState(
//                scaffoldState = rememberScaffoldState(),
//                navHostController = rememberAnimatedNavController(),
//                resources = LocalContext.current.resources,
//                coroutineScope = rememberCoroutineScope(),
//            ),
//            viewModel = hiltViewModel(),
//        )
//        SignUpContent(
//            nameFieldState = NameFieldState(),
//            emailFieldState = EmailFieldState(),
//            currencyFieldState = CurrencyFieldState(),
//            balanceFieldState = BalanceFieldState(),
//            passwordFieldState = PasswordFieldState(),
//            confirmPasswordFieldState = ConfirmPasswordFieldState(PasswordFieldState()),
//            signUpButtonState = ButtonState(),
//            showStartingBalanceHelpDialog = false,
//            onSignUpClick = {},
//            onSelectCurrencyFieldClick = {},
//            onSignInLinkClick = {},
//            onStartingBalanceHelpClick = {},
//            onStartingBalanceHelpDialogDismiss = {},
//        )
    }
}

@Composable
fun rememberSignUpScreenState(
    nameFieldState: NameFieldState = rememberSaveable { NameFieldState() },
    emailFieldState: EmailFieldState = rememberSaveable { EmailFieldState() },
    currencyFieldState: CurrencyFieldState = rememberSaveable { CurrencyFieldState() },
    balanceFieldState: BalanceFieldState = rememberSaveable { BalanceFieldState() },
    passwordFieldState: PasswordFieldState = rememberSaveable { PasswordFieldState() },
    confirmPasswordFieldState: ConfirmPasswordFieldState = rememberSaveable { ConfirmPasswordFieldState(passwordFieldState) },
    signUpButtonState: ButtonState = rememberSaveable { ButtonState() },
) = remember(nameFieldState, emailFieldState, passwordFieldState, confirmPasswordFieldState, signUpButtonState) {
    SignUpScreenState(
        nameFieldState= nameFieldState,
        emailFieldState = emailFieldState,
        currencyFieldState = currencyFieldState,
        balanceFieldState = balanceFieldState,
        passwordFieldState = passwordFieldState,
        confirmPasswordFieldState = confirmPasswordFieldState,
        signUpButtonState = signUpButtonState,
    )
}

class SignUpScreenState(
    val nameFieldState: NameFieldState,
    val emailFieldState: EmailFieldState,
    val currencyFieldState: CurrencyFieldState,
    val balanceFieldState: BalanceFieldState,
    val passwordFieldState: PasswordFieldState,
    val confirmPasswordFieldState: ConfirmPasswordFieldState,
    val signUpButtonState: ButtonState,
) {
    init {
        // currencyFieldState.enabled = false
        signUpButtonState.enabled = nameFieldState.valid
                && emailFieldState.valid
                && passwordFieldState.valid
                && confirmPasswordFieldState.valid
                && currencyFieldState.valid
                && balanceFieldState.valid
    }

    fun signUpFormValid(): Boolean {
        return nameFieldState.valid
                && emailFieldState.valid
                && passwordFieldState.valid
                && confirmPasswordFieldState.valid
                && currencyFieldState.valid
                && balanceFieldState.valid
    }

    fun toggleControls(enabled: Boolean) {
        nameFieldState.enabled = enabled
        emailFieldState.enabled = enabled
        balanceFieldState.enabled = enabled
        passwordFieldState.enabled = enabled
        confirmPasswordFieldState.enabled = enabled
    }
}

@Composable
fun SegmentedProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
) {
    val segmentsCount by remember { mutableStateOf(3) }
    val segmentSpacing by remember { mutableStateOf(8.dp) }

    SegmentedProgressIndicator(
        modifier = modifier.height(4.dp),
        segmentsCount = segmentsCount,
        segmentColor = MaterialTheme.colors.onSurface.copy(0.1f),
        spacing = segmentSpacing,
        progress = progress,
        progressAnimationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing
        ),
    )
}

@Composable
private fun PreviousStepButton(
    modifier: Modifier = Modifier,
    state: ButtonState,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .height(56.dp)
            .clickable { !state.clickable },
        enabled = state.enabled,
        onClick = { onClick() },
        colors = ButtonDefaults.appSecondaryButtonColors(),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.appButtonElevation(),
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = Icons.Default.ArrowBack.name,
            )
        }
    }
}
