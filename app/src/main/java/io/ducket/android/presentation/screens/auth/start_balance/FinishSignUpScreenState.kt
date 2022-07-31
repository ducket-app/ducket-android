package io.ducket.android.presentation.screens.auth.start_balance

import io.ducket.android.presentation.screens.InputFieldState

sealed class FinishSignUpUiEvent {
    class ShowMessage(val text: String) : FinishSignUpUiEvent()
    object NavigateToHome : FinishSignUpUiEvent()
    object NavigateBack : FinishSignUpUiEvent()
}

data class FinishSignUpUiState(
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val currency: String = "",
    val accountName: InputFieldState = InputFieldState(),
    val startBalance: InputFieldState = InputFieldState(),
)