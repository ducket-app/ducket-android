package io.ducket.android.presentation.screens.auth.start_balance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.data.remote.dto.account.AccountCreate
import io.ducket.android.data.remote.dto.account.AccountType
import io.ducket.android.data.remote.dto.user.UserCreate
import io.ducket.android.domain.interactors.CreateUserInteractor
import io.ducket.android.presentation.StateViewModel
import io.ducket.android.presentation.components.validator.AccountNameValidator
import io.ducket.android.presentation.components.validator.NumberValidator
import io.ducket.android.presentation.screens.navArgs
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishSignUpViewModel @Inject constructor(
    private val createUserInteractor: CreateUserInteractor,
    private val savedStateHandle: SavedStateHandle,
) : StateViewModel<FinishSignUpUiState>(FinishSignUpUiState()) {

    private val userInfo = savedStateHandle.navArgs<FinishSignUpScreenArgs>().userInfo

    private val _uiEvent = Channel<FinishSignUpUiEvent>()
    val uiEvent: Flow<FinishSignUpUiEvent> = _uiEvent.receiveAsFlow()

    init {
        updateState {
            copy(
                currency = userInfo.currency,
                accountName = state.accountName.copy(text = "Cash in ${userInfo.currency}")
            )
        }

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
            is Event.OnAccountNameChange -> {
                updateState {
                    copy(
                        accountName = accountName.copy(
                            error = validateAccountName(event.value).message,
                            text = event.value
                        )
                    )
                }
            }
            is Event.OnStartBalanceChange -> {
                updateState {
                    copy(
                        startBalance = startBalance.copy(
                            error = validateStartBalance(event.value).message,
                            text = event.value
                        )
                    )
                }
            }
            is Event.OnFinishClick -> {
                if (state.isFormValid) {
                    signUp(
                        newUser = UserCreate(
                            name = userInfo.name,
                            email = userInfo.email,
                            password = userInfo.password,
                            currency = userInfo.currency,
                            defaultAccount = AccountCreate(
                                name = state.accountName.text,
                                type = AccountType.CASH,
                                startBalance = state.startBalance.text.toBigDecimal(),
                                currency = userInfo.currency,
                                notes = null,
                            ),
                        )
                    )
                }
            }
            is Event.OnSkipClick -> {
                signUp(
                    newUser = UserCreate(
                        name = userInfo.name,
                        email = userInfo.email,
                        password = userInfo.password,
                        currency = userInfo.currency,
                        defaultAccount = null,
                    )
                )
            }
            is Event.OnBackClick -> {
                viewModelScope.launch {
                    _uiEvent.send(FinishSignUpUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun signUp(newUser: UserCreate) {
        viewModelScope.launch {
            createUserInteractor(newUser).collect {
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
                        _uiEvent.send(FinishSignUpUiEvent.NavigateToHome)
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        updateState {
                            copy(isLoading = false)
                        }
                        _uiEvent.send(FinishSignUpUiEvent.ShowMessage(it.msg))
                    }
                }
            }
//            createUserInteractor(userCreateDto).flatMapLatest {
//                if (it is ResourceState.Success) {
//                    getAccountInteractor.invoke(13)
//                } else flowOf(it)
//            }.collect {
//                when (it) {
//                    is ResourceState.Loading -> {
//                        updateState { copy(isLoading = true) }
//                    }
//                    is ResourceState.Success -> {
//                        updateState { copy(isLoading = false) }
//                        val accountId = (it.data as AccountDetails).account.id
//                        _uiEvent.send(SignUpUiEvent.NavigateToStartBalance(accountId))
//                    }
//                    is ResourceState.ConnectivityError,
//                    is ResourceState.AuthorizationError,
//                    is ResourceState.Error -> {
//                        updateState { copy(isLoading = false) }
//                        _uiEvent.send(SignUpUiEvent.ShowMessage(it.msg))
//                    }
//                }
//            }
        }
    }

    private fun isFormValid(): Boolean {
        return listOf(
            validateAccountName(state.accountName.text),
            validateStartBalance(state.startBalance.text),
        ).all { it.isValid }
    }

    private fun validateAccountName(value: String) = AccountNameValidator.validate(value)

    private fun validateStartBalance(value: String) = NumberValidator.validate(value)

    sealed class Event {
        data class OnAccountNameChange(val value: String) : Event()
        data class OnStartBalanceChange(val value: String) : Event()
        object OnBackClick : Event()
        object OnSkipClick : Event()
        object OnFinishClick : Event()
    }
}