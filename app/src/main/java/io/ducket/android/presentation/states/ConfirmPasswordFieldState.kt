package io.ducket.android.presentation.states

class ConfirmPasswordFieldState(
    private val passwordState: PasswordFieldState,
    override val validator: (String) -> Boolean = { passwordState.valid && passwordState.value == it },
    override val maxLength: Int = passwordState.maxLength,
) : PasswordFieldState()