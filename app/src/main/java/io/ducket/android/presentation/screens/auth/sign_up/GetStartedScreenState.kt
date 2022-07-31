package io.ducket.android.presentation.screens.auth.sign_up

import io.ducket.android.data.remote.dto.user.UserInfo
import io.ducket.android.presentation.components.UiText
import io.ducket.android.presentation.screens.InputFieldState
import io.ducket.android.presentation.screens.ProtectedInputFieldState

sealed class GetStartedUiEvent {
    class ShowMessage(val text: String) : GetStartedUiEvent()
    class NavigateToCurrencySelection(val currency: String) : GetStartedUiEvent()
    class NavigateToStartBalance(val user: UserInfo) : GetStartedUiEvent()
    object NavigateToSignIn : GetStartedUiEvent()
    object NavigateBack : GetStartedUiEvent()
}

data class GetStartedUiState(
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val legalCheckbox: Boolean = false,
    val name: InputFieldState = InputFieldState(),
    val email: InputFieldState = InputFieldState(),
    val password: ProtectedInputFieldState = ProtectedInputFieldState(),
    val currency: InputFieldState = InputFieldState(),
)