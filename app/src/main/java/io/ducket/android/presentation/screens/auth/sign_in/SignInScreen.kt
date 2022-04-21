package io.ducket.android.presentation.screens.auth.sign_in

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.ducket.android.R
import io.ducket.android.presentation.components.*
import io.ducket.android.presentation.navigation.AppSnackbarManager
import io.ducket.android.presentation.navigation.AppState
import io.ducket.android.presentation.navigation.Destination
import io.ducket.android.presentation.navigation.Graph
import io.ducket.android.presentation.states.*
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme
import io.ducket.android.presentation.ui.theme.SpaceMedium
import io.ducket.android.presentation.ui.theme.subtitle
import kotlinx.coroutines.flow.collect

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    scaffoldState: ScaffoldState,
    appSnackbarManager: AppSnackbarManager,
    navigateBack: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {
    val screenState = rememberSignInScreenState()
    val uiState by viewModel.uiState.collectAsState()

    screenState.signInButtonState.enabled = screenState.signInFormValid()

    LaunchedEffect(Unit) {
        viewModel.screenEvent.collect {
            if (it is SignInViewModel.ScreenEvent.ShowMessage) {
                appSnackbarManager.showMessage(it.text)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SignInAppBar(onCloseClick = navigateBack)
        },
    ) {
        when (uiState) {
            is SignInViewModel.UiState.Loading,
            is SignInViewModel.UiState.NotSignedIn -> {
                if (uiState is SignInViewModel.UiState.Loading) {
                    screenState.toggleControls(false)
                    LoadingDialog()
                } else {
                    screenState.toggleControls(true)
                }

                SignInContent(
                    emailFieldState = screenState.emailFieldState,
                    passwordFieldState = screenState.passwordFieldState,
                    signInButtonState = screenState.signInButtonState,
                    onSignUpLinkClick = navigateToSignUpScreen,
                    onSignInClick = {
                        viewModel.onSignIn(
                            screenState.emailFieldState.value,
                            screenState.passwordFieldState.value,
                        )
                    },
                )
            }
            is SignInViewModel.UiState.SignedIn -> {
                navigateToHomeScreen()
            }
        }
    }
}

@Composable
fun SignInAppBar(
    onCloseClick: () -> Unit,
) {
    ChildAppBar(
        title = stringResource(id = R.string.sign_in_title),
        elevation = 0.dp,
        onActionClick = onCloseClick,
        actionIcon = Icons.Default.Close,
    )
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SignInContent(
    emailFieldState: EmailFieldState,
    passwordFieldState: PasswordFieldState,
    signInButtonState: ButtonState,
    onSignInClick: () -> Unit,
    onSignUpLinkClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(SpaceMedium),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.align(Alignment.Start),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in_heading),
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = stringResource(id = R.string.sign_in_subheading),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.subtitle,
                )
            }

            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                EmailTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = emailFieldState,
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = passwordFieldState,
                    onImeAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()

                        onSignInClick()
                    }
                )
            }

            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.sign_in_button),
                    state = signInButtonState,
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()

                        onSignInClick()
                    },
                )

                Spacer(modifier = Modifier.height(SpaceMedium))

                ActionText(
                    text = stringResource(id = R.string.sign_in_helper),
                    link = stringResource(id = R.string.sign_up_title),
                    onActionClick = onSignUpLinkClick
                )
            }
        }
    }
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
@ExperimentalAnimationApi
@Preview
@Composable
private fun SignInContentPreview() {
    DucketAndroidTheme {
        SignInContent(
            emailFieldState = EmailFieldState(),
            passwordFieldState = PasswordFieldState(),
            signInButtonState = ButtonState(),
            onSignInClick = {},
            onSignUpLinkClick = {},
        )
    }
}

@Composable
fun rememberSignInScreenState(
    emailFieldState: EmailFieldState = rememberSaveable { EmailFieldState() },
    passwordFieldState: PasswordFieldState = rememberSaveable { PasswordFieldState() },
    signInButtonState: ButtonState = rememberSaveable { ButtonState() },
) = remember(emailFieldState, passwordFieldState, signInButtonState) {
    SignInScreenState(emailFieldState, passwordFieldState, signInButtonState)
}

class SignInScreenState(
    val emailFieldState: EmailFieldState,
    val passwordFieldState: PasswordFieldState,
    val signInButtonState: ButtonState,
) {

    fun signInFormValid(): Boolean {
        return emailFieldState.valid && passwordFieldState.valid
    }

    fun toggleControls(enabled: Boolean) {
        emailFieldState.enabled = enabled
        passwordFieldState.enabled = enabled
    }
}
