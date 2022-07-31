package io.ducket.android.presentation.screens.auth.sign_up

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.ScrollState
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import io.ducket.android.R
import io.ducket.android.presentation.components.*
import io.ducket.android.presentation.components.segments.SegmentedProgressIndicator
import io.ducket.android.presentation.navigation.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import io.ducket.android.common.extensions.clearFocusOnTapOutside
import io.ducket.android.presentation.screens.InputFieldState
import io.ducket.android.presentation.screens.ProtectedInputFieldState
import io.ducket.android.presentation.screens.destinations.CurrencySelectionScreenDestination
import io.ducket.android.presentation.screens.destinations.FinishSignUpScreenDestination
import io.ducket.android.presentation.screens.destinations.SignInScreenDestination
import io.ducket.android.presentation.screens.destinations.WelcomeScreenDestination
import io.ducket.android.presentation.ui.theme.*

@ExperimentalComposeUiApi
@AuthNavGraph
@Destination
@Composable
fun GetStartedScreen(
    viewModel: GetStartedViewModel = hiltViewModel(),
    navController: NavController,
    snackbarManager: AppSnackbarManager,
    result: ResultRecipient<CurrencySelectionScreenDestination, String>
) {
    val uiState by viewModel.stateFlow.collectAsState()
    val scrollState = rememberScrollState()

    result.onNavResult { res ->
        if (res is NavResult.Value) {
            viewModel.onEvent(GetStartedViewModel.Event.OnCurrencyChange(res.value))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is GetStartedUiEvent.ShowMessage -> {
                    snackbarManager.showMessage(it.text)
                }
                is GetStartedUiEvent.NavigateToSignIn -> {
                    navController.navigate(SignInScreenDestination) {
                        popUpTo(WelcomeScreenDestination.route)
                    }
                }
                is GetStartedUiEvent.NavigateToStartBalance -> {
                    navController.navigate(FinishSignUpScreenDestination(it.user))
                }
                is GetStartedUiEvent.NavigateToCurrencySelection -> {
                    navController.navigate(CurrencySelectionScreenDestination(it.currency))
                }
                is GetStartedUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    GetStartedContent(
        state = uiState,
        scrollState = scrollState,
        onNameChange = { viewModel.onEvent(GetStartedViewModel.Event.OnNameChange(it)) },
        onEmailChange = { viewModel.onEvent(GetStartedViewModel.Event.OnEmailChange(it)) },
        onCurrencyChange = { viewModel.onEvent(GetStartedViewModel.Event.OnCurrencyChange(it)) },
        onPasswordChange = { viewModel.onEvent(GetStartedViewModel.Event.OnPasswordChange(it)) },
        onPasswordVisibilityChange = { viewModel.onEvent(GetStartedViewModel.Event.OnPasswordVisibilityChange(it)) },
        onCurrencyClick = { viewModel.onEvent(GetStartedViewModel.Event.OnCurrencyClick) },
        onContinueClick = { viewModel.onEvent(GetStartedViewModel.Event.OnContinueClick) },
        onCloseClick = { viewModel.onEvent(GetStartedViewModel.Event.OnCloseClick) },
        onSignInClick = { viewModel.onEvent(GetStartedViewModel.Event.OnSignInClick) },
        onLegalCheckedChange = { viewModel.onEvent(GetStartedViewModel.Event.OnLegalCheckedChange(it)) },
        onTermsLinkClick = {
            // TODO
        },
        onPrivacyLinkClick = {
            // TODO
        }
    )
}

@Composable
fun GetStartedAppBar(
    onCloseClick: () -> Unit,
) {
    AppBar(
        elevation = 0.dp,
        navigationButton = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = Icons.Default.Close.name,
                )
            }
        }
    )
}

@Composable
fun NameTextField(
    modifier: Modifier = Modifier,
    state: InputFieldState,
    onValueChange: (String) -> Unit,
    onImeAction: () -> Unit
) {
    TextField(
        modifier = modifier,
        label = stringResource(id = R.string.name_label),
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
fun SettlementCurrencyTextField(
    modifier: Modifier = Modifier,
    state: InputFieldState,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
) {
    TextField(
        modifier = modifier,
        label = stringResource(id = R.string.currency_label),
        hint = stringResource(id = R.string.select_currency_hint),
        value = state.text,
        error = state.error?.asString(),
        enabled = false,
        onValueChange = onValueChange,
        onClick = onClick,
        onTrailingIconClick = onClick,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
        ),
        trailingIcon = Icons.Default.ChevronRight,
    )
}

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    state: InputFieldState,
    onValueChange: (String) -> Unit,
    onImeAction: () -> Unit,
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

    OnLifecycleEvent { owner, event ->
        if (event == Lifecycle.Event.ON_PAUSE) {
            onVisibilityChange(false)
        }
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
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LegalCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsLinkClick: () -> Unit,
    onPrivacyLinkClick: () -> Unit,
) {
    val termsLink = stringResource(id = R.string.legal_terms)
    val privacyLink = stringResource(id = R.string.legal_privacy)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpaceSmall),
        verticalAlignment = Alignment.Top,
    ) {
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            Checkbox(
                modifier = Modifier.padding(2.dp),
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    uncheckedColor =  MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                )
            )
        }

        HyperlinkText(
            fullText = stringResource(id = R.string.legal),
            linkText = listOf(termsLink, privacyLink),
            onLinkClick = { link ->
                when (link) {
                    termsLink -> onTermsLinkClick()
                    privacyLink -> onPrivacyLinkClick()
                }
            }
        )
    }
}


@Composable
fun ContinueButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    AppPrimaryButton(
        modifier = modifier,
        text = stringResource(id = R.string.continue_button),
        enabled = enabled,
        onClick = onClick,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GetStartedContent(
    state: GetStartedUiState,
    scrollState: ScrollState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onCloseClick: () -> Unit,
    onCurrencyClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSignInClick: () -> Unit,
    onLegalCheckedChange: (Boolean) -> Unit,
    onTermsLinkClick: () -> Unit,
    onPrivacyLinkClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.isLoading) {
        LoadingDialog()
    }

//        if (showStartingBalanceHelpDialog) {
//            AlertDialog(
//                title = {
//                    Text(
//                        text = stringResource(id = R.string.start_balance_help_heading),
//                        style = MaterialTheme.typography.h6,
//                    )
//                },
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.start_balance_help_helper),
//                        style = MaterialTheme.typography.body1,
//                    )
//                },
//                onDismissRequest = onStartingBalanceHelpDialogDismiss,
//                confirmButton = {
//                    TextButton(onClick = onStartingBalanceHelpDialogDismiss) {
//                        Text(
//                            text = stringResource(id = R.string.got_it_button),
//                            style = MaterialTheme.typography.subtitle2,
//                        )
//                    }
//                }
//            )
//        }

    Column(
        modifier = Modifier.clearFocusOnTapOutside(focusManager)
    ) {
        GetStartedAppBar(onCloseClick = onCloseClick)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(
                    start = SpaceMedium,
                    end = SpaceMedium,
                    bottom = SpaceMedium,
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.get_started_title),
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Start,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.get_started_subtitle),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.caption,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(SpaceMedium))

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(SpaceSmall),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        NameTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = state.name,
                            onValueChange = onNameChange,
                            onImeAction = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )

                        EmailTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = state.email,
                            onValueChange = onEmailChange,
                            onImeAction = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )

                        SettlementCurrencyTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = state.currency,
                            onValueChange = onCurrencyChange,
                            onClick = onCurrencyClick
                        )

                        PasswordTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = state.password,
                            onValueChange = onPasswordChange,
                            onVisibilityChange = onPasswordVisibilityChange,
                            onImeAction = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )

                        LegalCheckbox(
                            modifier = Modifier.fillMaxWidth(),
                            checked = state.legalCheckbox,
                            onCheckedChange = onLegalCheckedChange,
                            onTermsLinkClick = onTermsLinkClick,
                            onPrivacyLinkClick = onPrivacyLinkClick,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(SpaceMedium))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(SpaceMedium),
                ) {
                    ContinueButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.isFormValid,
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()

                            onContinueClick()
                        },
                    )

                    ActionText(
                        text = stringResource(id = R.string.sign_up_helper),
                        link = stringResource(id = R.string.sign_up_helper_link),
                        onActionClick = onSignInClick,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GetStartedContentPreview() {
    DucketAndroidTheme {
        GetStartedContent(
            state = GetStartedUiState(),
            scrollState = rememberScrollState(),
            onNameChange = {},
            onEmailChange = {},
            onCurrencyChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onCloseClick = {},
            onCurrencyClick = {},
            onContinueClick = {},
            onSignInClick = {},
            onLegalCheckedChange = {},
            onTermsLinkClick = {},
            onPrivacyLinkClick = {},
        )
    }
}

//@Composable
//fun PasswordRequirementsColumn(
//    modifier: Modifier = Modifier,
//    satisfiedRequirements: List<PasswordRequirement>
//) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(SpaceSmall)
//    ) {
//        PasswordRequirement.values().forEach { requirement ->
//            Requirement(
//                message = requirement.label.asString(),
//                satisfied = satisfiedRequirements.contains(requirement)
//            )
//        }
//    }
//}

//@Composable
//fun Requirement(
//    modifier: Modifier = Modifier,
//    message: String,
//    satisfied: Boolean
//) {
//    val tint = if (satisfied) MaterialTheme.colors.success
//    else MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
//
//    val icon = if (satisfied) Icons.Default.Check else Icons.Default.Close
//
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(SpaceSmall),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            modifier = Modifier.size(16.dp),
//            imageVector = icon,
//            contentDescription = null,
//            tint = tint
//        )
//        Text(
//            text = message,
//            style = MaterialTheme.typography.subtitle2,
//            color = tint,
//        )
//    }
//}

//@Composable
//fun StartBalanceTextField(
//    modifier: Modifier = Modifier,
//) {
//    TextField(
//        modifier = modifier,
//        label = stringResource(id = R.string.name_label),
//        value = "",
//        error = null,
//        onValueChange = {
//
//        },
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Number,
//            imeAction = ImeAction.Next,
//        ),
//        keyboardActions = KeyboardActions(
//            onNext = {
//                // onImeAction()
//            }
//        ),
//        trailingIcon = Icons.Default.Help
//    )
//}

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
