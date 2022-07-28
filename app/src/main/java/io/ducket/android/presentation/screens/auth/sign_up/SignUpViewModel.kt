package io.ducket.android.presentation.screens.auth.sign_up

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.data.remote.dto.UserCreateDto
import io.ducket.android.domain.interactors.CreateUserInteractor
import io.ducket.android.presentation.StateViewModel
import io.ducket.android.presentation.components.validator.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val createUserInteractor: CreateUserInteractor,
) : StateViewModel<SignUpUiState>(SignUpUiState()) {

    private val _uiEvent = Channel<SignUpUiEvent>()
    val uiEvent: Flow<SignUpUiEvent> = _uiEvent.receiveAsFlow()

    init {
        stateFlow
            .filterNotNull()
            .onEach {
                updateState {
                    copy(isFormValid = isFormValid())
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnNameChange -> {
                updateState {
                    copy(
                        name = name.copy(
                            error = validateName(event.value).message,
                            text = event.value
                        )
                    )
                }
            }
            is Event.OnEmailChange -> {
                updateState {
                    copy(
                        email = email.copy(
                            error = validateEmail(event.value).message,
                            text = event.value
                        )
                    )
                }
            }
            is Event.OnPasswordChange -> {
                updateState {
                    copy(
                        password = password.copy(
                            error = validatePassword(event.value).message,
                            text = event.value
                        )
                    )
                }
            }
            is Event.OnCurrencyChange -> {
                updateState {
                    copy(
                        currency = currency.copy(
                            error = validateCurrency(event.value).message,
                            text = event.value
                        )
                    )
                }
            }
            is Event.OnPasswordVisibilityChange -> {
                updateState {
                    copy(password = password.copy(visible = event.visible))
                }
            }
            is Event.OnSignInClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SignUpUiEvent.NavigateToSignIn)
                }
            }
            is Event.OnCurrencyClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SignUpUiEvent.NavigateToCurrencySelection(state.currency.text))
                }
            }
            is Event.OnLegalCheckedChange -> {
                updateState {
                    copy(legalCheckbox = event.value)
                }
            }
            is Event.OnContinueClick -> {
                if (state.isFormValid) {
                    signUp(
                        name = state.name.text,
                        email = state.email.text,
                        currency = state.currency.text,
                        password = state.password.text,
                    )
                }
            }
            is Event.OnCloseClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SignUpUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun signUp(name: String, email: String, currency: String, password: String) {
        viewModelScope.launch {
            val userCreateDto = UserCreateDto(name, email, password, currency)

            createUserInteractor(userCreateDto).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        updateState {
                            copy(isLoading = true)
                        }
                    }
                    is ResourceState.Success -> {
                        updateState {
                            copy(isLoading = false)
                        }
                        _uiEvent.send(SignUpUiEvent.NavigateToStartBalance)
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        updateState {
                            copy(isLoading = false)
                        }
                        _uiEvent.send(SignUpUiEvent.ShowMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        return listOf(validateName(state.name.text),
            validateEmail(state.email.text),
            validateCurrency(state.currency.text),
            validatePassword(state.password.text),
        ).all { it.isValid } && state.legalCheckbox
    }

    private fun validateName(value: String) = NameTextValidator.validate(value)

    private fun validateEmail(value: String) = EmailTextValidator.validate(value)

    private fun validatePassword(value: String) = PasswordTextValidator.validate(value)

    private fun validateCurrency(value: String) = CurrencyTextValidator.validate(value)

    sealed class Event {
        data class OnNameChange(val value: String) : Event()
        data class OnEmailChange(val value: String) : Event()
        data class OnPasswordChange(val value: String) : Event()
        data class OnPasswordVisibilityChange(val visible: Boolean) : Event()
        data class OnCurrencyChange(val value: String) : Event()
        data class OnLegalCheckedChange(val value: Boolean) : Event()
        object OnSignInClick : Event()
        object OnContinueClick : Event()
        object OnCurrencyClick : Event()
        object OnCloseClick : Event()
    }
}