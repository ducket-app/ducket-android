package io.ducket.android.presentation.screens.auth.sign_up

import io.ducket.android.presentation.screens.InputFieldState
import io.ducket.android.presentation.screens.ProtectedInputFieldState

sealed class SignUpUiEvent {
    class ShowMessage(val text: String) : SignUpUiEvent()
    class NavigateToCurrencySelection(val currency: String) : SignUpUiEvent()
    object NavigateToSignIn : SignUpUiEvent()
    object NavigateToStartBalance : SignUpUiEvent()
    object NavigateBack : SignUpUiEvent()
}

data class SignUpUiState(
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val legalCheckbox: Boolean = false,
    val name: InputFieldState = InputFieldState(),
    val email: InputFieldState = InputFieldState(),
    val password: ProtectedInputFieldState = ProtectedInputFieldState(),
    val currency: InputFieldState = InputFieldState(),
)