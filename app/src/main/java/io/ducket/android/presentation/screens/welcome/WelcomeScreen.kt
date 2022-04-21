package io.ducket.android.presentation.screens.welcome

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.ducket.android.R
import io.ducket.android.presentation.states.ButtonState
import io.ducket.android.presentation.components.PrimaryButton
import io.ducket.android.presentation.components.SecondaryButton
import io.ducket.android.presentation.navigation.AppState
import io.ducket.android.presentation.ui.theme.SpaceMedium

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun WelcomeScreen(
    onGoToSignUpClick: () -> Unit,
    onGoToSignInClick: () -> Unit,
) {
    val signUpButtonState = remember { ButtonState() }
    val signInButtonState = remember { ButtonState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(SpaceMedium),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpaceMedium),
            ) {
                SecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.login_button),
                    state = signInButtonState,
                    onClick = onGoToSignInClick,
                )

                PrimaryButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.get_started_button),
                    state = signUpButtonState,
                    onClick = onGoToSignUpClick,
                )
            }
        }
    }
}

