package io.ducket.android.presentation.screens.auth.sign_in

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.data.remote.dto.UserAuthDto
import io.ducket.android.domain.interactors.AuthUserInteractor
import io.ducket.android.presentation.StateViewModel
import io.ducket.android.presentation.components.validator.*
import io.ducket.android.presentation.screens.InputFieldState
import io.ducket.android.presentation.screens.ProtectedInputFieldState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun onEvent(event: SignInScreenEvent) {
        when (event) {
            is SignInScreenEvent.OnEmailChange -> {
                updateState {
                    copy(email = email.copy(
                        error = validateEmail(event.value).message,
                        text = event.value
                    ))
                }
            }
            is SignInScreenEvent.OnPasswordChange -> {
                updateState {
                    copy(password = password.copy(
                        error = validatePassword(event.value).message,
                        text = event.value
                    ))
                }
            }
            is SignInScreenEvent.OnPasswordVisibilityChange -> {
                updateState {
                    copy(password = password.copy(visible = event.visible))
                }
            }
            is SignInScreenEvent.OnSignInClick -> {
                if (state.isFormValid) {
                    signIn(
                        email = state.email.text,
                        password = state.password.text
                    )
                }
            }
            is SignInScreenEvent.OnSignUpClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SignInUiEvent.NavigateToSignUp)
                }
            }
            is SignInScreenEvent.OnCloseClick -> {
                viewModelScope.launch {
                    _uiEvent.send(SignInUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authUserInteractor(UserAuthDto(email, password)).collect {
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

    private fun validateEmail(value: String): ValidationResult = EmailTextValidator.validate(value)

    private fun validatePassword(value: String): ValidationResult = PasswordTextValidator.validate(value)
}