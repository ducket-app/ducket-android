package io.ducket.android.presentation.screens.auth.sign_in

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.data.remote.dto.user.UserAuth
import io.ducket.android.domain.interactors.AuthUserInteractor
import io.ducket.android.presentation.StateViewModel
import io.ducket.android.presentation.components.validator.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUserInteractor: AuthUserInteractor,
) : StateViewModel<SignInUiState>(SignInUiState()) {

//    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState())
//    private val _emailState = MutableStateFlow(InputFieldState())
//    private val _passwordState = MutableStateFlow(ProtectedInputFieldState())
//    private val _isLoading = MutableStateFlow(false)

//    val uiState: StateFlow<SignInUiState> = combine(
//        _emailState, _passwordState, _isLoading
//    ) { email, password, isLoading ->
//        val isFormValid = validateEmail(email.text).isValid
//                && validatePassword(password.text).isValid
//
//        SignInUiState(
//           isLoading = isLoading,
//           isFormValid = isFormValid,
//           email = email,
//           password = password,
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        initialValue = SignInUiState(),
//        started = SharingStarted.Eagerly
//    )

    private val _uiEvent = Channel<SignInUiEvent>()
    val uiEvent: Flow<SignInUiEvent> = _uiEvent.receiveAsFlow()

    init {
        stateFlow
            .filterNotNull()
            .onEach {
                updateState {
                    copy(isFormValid = isFormValid(
                        email = it.email.text,
                        password = it.password.text
                    ))
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnEmailChange -> {
                updateState {
                    copy(email = email.copy(
                        error = validateEmail(event.value).message,
                        text = event.value
                    ))
                }
            }
            is Event.OnPasswordChange -> {
                updateState {
                    copy(password = password.copy(
                        error = validatePassword(event.value).message,
                        text = event.value
                    ))
                }
            }
            is Event.OnPasswordVisibilityChange -> {
                updateState {
                    copy(password = password.copy(visible = event.visible))
                }
            }
            is Event.OnSignInClick -> {
                if (state.isFormValid) {
                    signIn(
                        email = state.email.text,
                        password = state.password.text
                    )
                }
            }
            is Event.OnSignUpClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SignInUiEvent.NavigateToSignUp)
                }
            }
            is Event.OnCloseClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SignInUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authUserInteractor(UserAuth(email, password)).collect {
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
                        _uiEvent.send(SignInUiEvent.NavigateToHome)
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        updateState {
                            copy(isLoading = false)
                        }
                        _uiEvent.send(SignInUiEvent.ShowMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun isFormValid(email: String, password: String): Boolean {
        return validateEmail(email).isValid && validatePassword(password).isValid
    }

    private fun validateEmail(value: String): ValidationResult = EmailValidator.validate(value)

    private fun validatePassword(value: String): ValidationResult = PasswordValidator.validate(value)
    
    sealed class Event {
        data class OnEmailChange(val value: String) : Event()
        data class OnPasswordChange(val value: String) : Event()
        data class OnPasswordVisibilityChange(val visible: Boolean) : Event()
        object OnSignInClick : Event()
        object OnSignUpClick : Event()
        object OnCloseClick : Event()
    }
}