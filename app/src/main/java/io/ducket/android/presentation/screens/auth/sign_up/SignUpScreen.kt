package io.ducket.android.presentation.screens.auth.sign_up

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
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
import io.ducket.android.presentation.states.*
import io.ducket.android.presentation.components.*
import io.ducket.android.presentation.components.segments.SegmentedProgressIndicator
import io.ducket.android.presentation.navigation.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import io.ducket.android.presentation.screens.InputFieldState
import io.ducket.android.presentation.screens.ProtectedInputFieldState
import io.ducket.android.presentation.screens.destinations.CurrencySelectionScreenDestination
import io.ducket.android.presentation.screens.destinations.SignInScreenDestination
import io.ducket.android.presentation.screens.destinations.WelcomeScreenDestination
import io.ducket.android.presentation.ui.theme.*

@ExperimentalComposeUiApi
@AuthNavGraph
@Destination
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController,
    snackbarManager: AppSnackbarManager,
    result: ResultRecipient<CurrencySelectionScreenDestination, String>
) {
    val uiState by viewModel.stateFlow.collectAsState()
    val scrollState = rememberScrollState()

    result.onNavResult { res ->
        if (res is NavResult.Value) {
            viewModel.onEvent(SignUpViewModel.Event.OnCurrencyChange(res.value))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is SignUpUiEvent.ShowMessage -> {
                    snackbarManager.showMessage(it.text)
                }
                is SignUpUiEvent.NavigateToSignIn -> {
                    navController.navigate(SignInScreenDestination) {
                        popUpTo(WelcomeScreenDestination.route)
                    }
                }
                is SignUpUiEvent.NavigateToStartBalance -> {
                    // TODO
                }
                is SignUpUiEvent.NavigateToCurrencySelection -> {
                    navController.navigate(CurrencySelectionScreenDestination(it.currency))
                }
                is SignUpUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    SignUpContent(
        state = uiState,
        scrollState = scrollState,
        onNameChange = { viewModel.onEvent(SignUpViewModel.Event.OnNameChange(it)) },
        onEmailChange = { viewModel.onEvent(SignUpViewModel.Event.OnEmailChange(it)) },
        onCurrencyChange = { viewModel.onEvent(SignUpViewModel.Event.OnCurrencyChange(it)) },
        onPasswordChange = { viewModel.onEvent(SignUpViewModel.Event.OnPasswordChange(it)) },
        onPasswordVisibilityChange = { viewModel.onEvent(SignUpViewModel.Event.OnPasswordVisibilityChange(it)) },
        onCurrencyClick = { viewModel.onEvent(SignUpViewModel.Event.OnCurrencyClick) },
        onContinueClick = { viewModel.onEvent(SignUpViewModel.Event.OnContinueClick) },
        onCloseClick = { viewModel.onEvent(SignUpViewModel.Event.OnCloseClick) },
        onSignInClick = { viewModel.onEvent(SignUpViewModel.Event.OnSignInClick) },
        onLegalCheckedChange = { viewModel.onEvent(SignUpViewModel.Event.OnLegalCheckedChange(it)) },
        onTermsLinkClick = {
            // TODO
        },
        onPrivacyLinkClick = {
            // TODO
        }
    )
}

@Composable
fun SignUpAppBar(
    onCloseClick: () -> Unit,
) {
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

//@OptIn(
//    ExperimentalPagerApi::class,
//    ExperimentalComposeUiApi::class,
//)
//@Composable
//fun SignUpContent(
//    onCurrencyFieldClick: () -> Unit
//) {
//    val steps = SignUpStep.values()
//    val scrollState = rememberScrollState()
//    val pagerState = rememberPagerState(pageCount = steps.size)
//    val coroutineScope = rememberCoroutineScope()
//
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusManager = LocalFocusManager.current
//
//    val name = rememberSaveable { mutableStateOf("") }
//
//    val changeStep: (Int) -> Unit = { nextPage ->
//        keyboardController?.hide()
//        focusManager.clearFocus()
//
//        coroutineScope.launch {
//            if (nextPage in 0 until pagerState.pageCount) {
//                pagerState.animateScrollToPage(
//                    page = nextPage,
//                    animationSpec = tween(durationMillis = 250)
//                )
//            }
//        }
//    }
//
//    Column {
//        SignUpAppBar(onCloseClick = {})
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colors.surface)
//                .padding(SpaceMedium),
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(scrollState),
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.SpaceBetween,
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Crossfade(
//                        targetState = pagerState.targetPage,
//                        animationSpec = tween(durationMillis = 250)
//                    ) { page ->
//                        Text(
//                            modifier = Modifier.fillMaxWidth(),
//                            text = steps[page].title.asString(),
//                            textAlign = TextAlign.Start,
//                            style = MaterialTheme.typography.h4
//                        )
//                    }
//
//                    Text(
//                        modifier = Modifier.fillMaxWidth(),
//                        text = stringResource(
//                            R.string.step_of,
//                            pagerState.targetPage + 1,
//                            pagerState.pageCount),
//                        textAlign = TextAlign.Start,
//                        style = MaterialTheme.typography.h5,
//                        color = MaterialTheme.colors.caption,
//                    )
//
//                    Spacer(Modifier.height(SpaceMedium))
//
//                    HorizontalPager(
//                        modifier = Modifier.fillMaxSize(),
//                        state = pagerState,
//                        dragEnabled = true,
//                        verticalAlignment = Alignment.Top,
//                        itemSpacing = SpaceMedium,
//                    ) { page ->
//                        Card(
//                            elevation = 0.dp,
//                            modifier = Modifier
//                                .graphicsLayer {
//                                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
//
//                                    lerp(
//                                        start = 0.8f,
//                                        stop = 1f,
//                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                                    ).also { scale ->
//                                        scaleX = scale
//                                        scaleY = scale
//                                    }
//
//                                    alpha = lerp(
//                                        start = 0.7f,
//                                        stop = 1f,
//                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                                    )
//                                }
//                        ) {
//                            when (SignUpStep.values()[page]) {
//                                SignUpStep.PERSONAL_DATA -> {
//                                    SignUpFirstStep(
//                                        name = name,
//                                    )
//                                }
//                                SignUpStep.PASSWORD_DATA -> {
//                                    SignUpSecondStep()
//                                }
//                                SignUpStep.BALANCE_DATA -> {
//                                    SignUpThirdStep(
//                                        onCurrencyFieldClick = {
//                                            onCurrencyFieldClick()
//                                        }
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//                Column(
//                    modifier = Modifier.wrapContentSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(SpaceMedium),
//                ) {
//                    SignUpActionButtons(
//                        onBackClick = {
//                            changeStep(pagerState.currentPage - 1)
//                        },
//                        onContinueClick = {
//                            changeStep(pagerState.currentPage + 1)
//                        }
//                    )
//
//                    ActionText(
//                        text = stringResource(id = R.string.sign_up_helper),
//                        link = stringResource(id = R.string.sign_in_title),
//                        onActionClick = {
//                            // onSignInClick()
//                        },
//                    )
//                }
//            }
//        }
//    }
//}

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LegalCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsLinkClick: () -> Unit,
    onPrivacyLinkClick: () -> Unit,
) {
    val termsLink = stringResource(id = R.string.signup_legal_terms)
    val privacyLink = stringResource(id = R.string.signup_legal_privacy)

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
            fullText = stringResource(id = R.string.signup_legal),
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

@Preview
@Composable
fun SignUpPersonalStepPreview() {
    DucketAndroidTheme {
        SignUpContent(
            state = SignUpUiState(),
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpContent(
    state: SignUpUiState,
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
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
        SignUpAppBar(onCloseClick = onCloseClick)

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
                        text = stringResource(id = R.string.sign_up_title),
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Start,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.sign_up_personal_step_subtitle),
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

//@Composable
//fun NameTextField(
//    modifier: Modifier = Modifier,
//    state: NameFieldState,
//    onImeAction: () -> Unit = {},
//) {
//    AppTextField(
//        modifier = modifier,
//        state = state,
//        error = stringResource(id = R.string.invalid_name_error),
//        label = {
//            Text(stringResource(id = R.string.name_label))
//        },
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Text,
//            imeAction = ImeAction.Next,
//        ),
//        keyboardActions = KeyboardActions(
//            onNext = {
//                onImeAction()
//            }
//        ),
//    )
//}
//
//@Composable
//fun EmailTextField(
//    modifier: Modifier = Modifier,
//    state: TextFieldState,
//    onImeAction: () -> Unit = {},
//) {
//    AppTextField(
//        modifier = modifier,
//        state = state,
//        error = stringResource(id = R.string.invalid_email_error),
//        label = {
//            Text(stringResource(id = R.string.email_label))
//        },
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Email,
//            imeAction = ImeAction.Next,
//        ),
//        keyboardActions = KeyboardActions(
//            onNext = {
//                onImeAction()
//            }
//        ),
//    )
//}
//
//@Composable
//fun CurrencyTextField(
//    modifier: Modifier = Modifier,
//    state: TextFieldState,
//    onClick: (String) -> Unit,
//) {
//    AppReadonlyTextField(
//        modifier = modifier,
//        state = state,
//        label = stringResource(id = R.string.currency_label),
//        placeholder = stringResource(id = R.string.select_currency_placeholder),
//        trailingIcon = Icons.Default.ChevronRight,
//        onClick = {
//            onClick(state.value)
//        }
//    )
//}
//
//@Composable
//fun BalanceTextField(
//    modifier: Modifier = Modifier,
//    state: TextFieldState,
//    onHelpClick: () -> Unit,
//    onImeAction: () -> Unit = {},
//) {
//    AppTextField(
//        modifier = modifier,
//        state = state,
//        error = stringResource(id = R.string.invalid_number_error),
//        label = {
//            Text(stringResource(id = R.string.balance_label))
//        },
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Number,
//            imeAction = ImeAction.Next,
//        ),
//        keyboardActions = KeyboardActions(
//            onNext = {
//                onImeAction()
//            }
//        ),
//        trailingIcon = {
//            IconButton(onClick = onHelpClick) {
//                Icon(
//                    imageVector = Icons.Default.Help,
//                    contentDescription = stringResource(id = R.string.start_balance_desc),
//                )
//            }
//        }
//    )
//}
//
//@ExperimentalComposeUiApi
//@Composable
//fun PasswordTextField(
//    modifier: Modifier = Modifier,
//    state: PasswordFieldState,
//    onImeAction: () -> Unit = {},
//) {
//    AppProtectedTextField(
//        modifier = modifier,
//        state = state,
//        error = stringResource(id = R.string.invalid_password_error),
//        label = {
//            Text(stringResource(id = R.string.password_label))
//        },
//        keyboardActions = KeyboardActions(
//            onNext = {
//                onImeAction()
//            }
//        ),
//    )
//}
//
//@ExperimentalComposeUiApi
//@Composable
//fun ConfirmPasswordTextField(
//    modifier: Modifier = Modifier,
//    state: ConfirmPasswordFieldState,
//    onImeAction: () -> Unit = {},
//) {
//    AppProtectedTextField(
//        modifier = modifier,
//        state = state,
//        error = stringResource(id = R.string.invalid_confirm_password_error),
//        label = {
//            Text(stringResource(id = R.string.confirm_password_label))
//        },
//        imeAction = ImeAction.Next,
//        keyboardActions = KeyboardActions(
//            onNext = {
//                onImeAction()
//            }
//        ),
//    )
//}

//@Preview
//@ExperimentalComposeUiApi
//@ExperimentalAnimationApi
//@ExperimentalPagerApi
//@Composable
//private fun SignUpScreenPreview() {
//    DucketAndroidTheme {
////        SignUpScreen(
////            appState = AppState(
////                scaffoldState = rememberScaffoldState(),
////                navHostController = rememberAnimatedNavController(),
////                resources = LocalContext.current.resources,
////                coroutineScope = rememberCoroutineScope(),
////            ),
////            viewModel = hiltViewModel(),
////        )
////        SignUpContent(
////            nameFieldState = NameFieldState(),
////            emailFieldState = EmailFieldState(),
////            currencyFieldState = CurrencyFieldState(),
////            balanceFieldState = BalanceFieldState(),
////            passwordFieldState = PasswordFieldState(),
////            confirmPasswordFieldState = ConfirmPasswordFieldState(PasswordFieldState()),
////            signUpButtonState = ButtonState(),
////            showStartingBalanceHelpDialog = false,
////            onSignUpClick = {},
////            onSelectCurrencyFieldClick = {},
////            onSignInLinkClick = {},
////            onStartingBalanceHelpClick = {},
////            onStartingBalanceHelpDialogDismiss = {},
////        )
//    }
//}

@Composable
fun rememberSignUpScreenState(
    nameFieldState: NameFieldState = rememberSaveable { NameFieldState() },
    emailFieldState: EmailFieldState = rememberSaveable { EmailFieldState() },
    currencyFieldState: CurrencyFieldState = rememberSaveable { CurrencyFieldState() },
    balanceFieldState: BalanceFieldState = rememberSaveable { BalanceFieldState() },
    passwordFieldState: PasswordFieldState = rememberSaveable { PasswordFieldState() },
    confirmPasswordFieldState: ConfirmPasswordFieldState = rememberSaveable {
        ConfirmPasswordFieldState(
            passwordFieldState
        )
    },
    signUpButtonState: ButtonState = rememberSaveable { ButtonState() },
) = remember(
    nameFieldState,
    emailFieldState,
    passwordFieldState,
    confirmPasswordFieldState,
    signUpButtonState
) {
    SignUpScreenState(
        nameFieldState = nameFieldState,
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
