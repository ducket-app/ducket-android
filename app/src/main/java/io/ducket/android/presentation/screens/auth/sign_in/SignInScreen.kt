package io.ducket.android.presentation.screens.auth.sign_in

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import io.ducket.android.R
import io.ducket.android.presentation.components.*
import io.ducket.android.presentation.navigation.AppSnackbarManager
import io.ducket.android.presentation.navigation.AuthNavGraph
import io.ducket.android.presentation.navigation.OnLifecycleEvent
import io.ducket.android.presentation.screens.InputFieldState
import io.ducket.android.presentation.screens.ProtectedInputFieldState
import io.ducket.android.presentation.screens.destinations.SignUpScreenDestination
import io.ducket.android.presentation.screens.destinations.WelcomeScreenDestination
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme
import io.ducket.android.presentation.ui.theme.SpaceMedium
import io.ducket.android.presentation.ui.theme.SpaceSmall
import io.ducket.android.presentation.ui.theme.caption

sealed class SignInScreenEvent {
    data class OnEmailChange(val value: String) : SignInScreenEvent()
    data class OnPasswordChange(val value: String) : SignInScreenEvent()
    data class OnPasswordVisibilityChange(val visible: Boolean) : SignInScreenEvent()
    object OnSignInClick : SignInScreenEvent()
    object OnSignUpClick : SignInScreenEvent()
    object OnCloseClick : SignInScreenEvent()
}

fun Modifier.clearFocusOnTapOutside(focusManager: FocusManager): Modifier = pointerInput(Unit) {
    detectTapGestures(onTap = {
        focusManager.clearFocus()
    })
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

@AuthNavGraph
@Destination
@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    snackbarManager: AppSnackbarManager,
    navController: NavController,
) {
    val uiState by viewModel.stateFlow.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is SignInUiEvent.ShowMessage -> {
                    snackbarManager.showMessage(it.text)
                }
                is SignInUiEvent.NavigateToHome -> {
                    // TODO
                }
                is SignInUiEvent.NavigateToSignUp -> {
                    navController.navigate(SignUpScreenDestination) {
                        popUpTo(WelcomeScreenDestination.route)
                    }
                }
                is SignInUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    SignInContent(
        state = uiState,
        scrollState = scrollState,
        onCloseClick = {
            viewModel.onEvent(SignInScreenEvent.OnCloseClick)
        },
        onSignUpLinkClick = {
            viewModel.onEvent(SignInScreenEvent.OnSignUpClick)
        },
        onSignInClick = {
            viewModel.onEvent(SignInScreenEvent.OnSignInClick)
        },
        onEmailChange = {
            viewModel.onEvent(SignInScreenEvent.OnEmailChange(it))
        },
        onPasswordChange = {
            viewModel.onEvent(SignInScreenEvent.OnPasswordChange(it))
        },
        onPasswordVisibilityChange = {
            viewModel.onEvent(SignInScreenEvent.OnPasswordVisibilityChange(it))
        }
    )
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SignInContent(
    state: SignInUiState,
    scrollState: ScrollState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onCloseClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpLinkClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.isLoading) {
        LoadingDialog()
    }

    Column(modifier = Modifier.clearFocusOnTapOutside(focusManager)) {
        SignInAppBar(onCloseClick = onCloseClick)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(
                    start = SpaceMedium,
                    end = SpaceMedium,
                    bottom = SpaceMedium,
                ),
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
                        color = MaterialTheme.colors.caption,
                    )
                }

                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    EmailTextField(
                        modifier = Modifier.fillMaxWidth(),
                        state = state.email,
                        onValueChange = onEmailChange,
                        onImeAction = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )

                    PasswordTextField(
                        modifier = Modifier.fillMaxWidth(),
                        state = state.password,
                        onValueChange = onPasswordChange,
                        onVisibilityChange = onPasswordVisibilityChange,
                        onImeAction = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onSignInClick()
                        },
                    )
                }

                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(SpaceMedium),
                ) {
                    SignInButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.isFormValid,
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()

                            onSignInClick()
                        }
                    )

                    ActionText(
                        text = stringResource(id = R.string.sign_in_helper),
                        link = stringResource(id = R.string.sign_in_helper_link),
                        onActionClick = onSignUpLinkClick
                    )
                }
            }
        }
    }
}

@Composable
fun SignInAppBar(onCloseClick: () -> Unit) {
    AppBar(
        elevation = 0.dp,
        navigationButton = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = Icons.Default.Close.name,
                    modifier = Modifier.padding(horizontal = SpaceSmall)
                )
            }
        }
    )
}

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    AppPrimaryButton(
        modifier = modifier,
        text = stringResource(id = R.string.sign_in_button),
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    state: InputFieldState,
    onValueChange: (String) -> Unit,
    onImeAction: () -> Unit
) {
    TextField(
        modifier = modifier,
        label = stringResource(id = R.string.email_label),
        value = state.text,
        error = state.error?.asString(),
        onValueChange = onValueChange,
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
fun PasswordTextField(
    modifier: Modifier = Modifier,
    state: ProtectedInputFieldState,
    onValueChange: (String) -> Unit,
    onVisibilityChange: (Boolean) -> Unit,
    onImeAction: () -> Unit,
) {
    val transformation = if (state.visible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    val iconImage = if (transformation == VisualTransformation.None) {
        Icons.Filled.Visibility
    } else {
        Icons.Filled.VisibilityOff
    }

    TextField(
        modifier = modifier,
        value = state.text,
        error = state.error?.asString(),
        label = stringResource(id = R.string.password_label),
        trailingIcon = iconImage,
        onTrailingIconClick = {
            onVisibilityChange(!state.visible)
        },
        onClearFocus = {
            onVisibilityChange(false)
        },
        onValueChange = {
            onValueChange(it)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onImeAction()
            }
        ),
        visualTransformation = transformation
    )

    OnLifecycleEvent { owner, event ->
        if (event == Lifecycle.Event.ON_PAUSE) {
            onVisibilityChange(false)
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview
@Composable
private fun SignInContentPreview() {
    DucketAndroidTheme {
        SignInContent(
            state = SignInUiState(),
            scrollState = rememberScrollState(),
            onCloseClick = {},
            onEmailChange = {},
            onPasswordVisibilityChange = {},
            onPasswordChange = {},
            onSignInClick = {},
            onSignUpLinkClick = {},
        )
    }
}