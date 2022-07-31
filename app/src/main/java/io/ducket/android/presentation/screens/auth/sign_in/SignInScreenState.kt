package io.ducket.android.presentation.screens.auth.sign_in

import io.ducket.android.presentation.screens.InputFieldState
import io.ducket.android.presentation.screens.ProtectedInputFieldState

sealed class SignInUiEvent {
    class ShowMessage(val text: String) : SignInUiEvent()
    object NavigateToSignUp : SignInUiEvent()
    object NavigateToHome : SignInUiEvent()
    object NavigateBack : SignInUiEvent()
}

data class SignInUiState(
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val email: InputFieldState = InputFieldState(),
    val password: ProtectedInputFieldState = ProtectedInputFieldState(),
)