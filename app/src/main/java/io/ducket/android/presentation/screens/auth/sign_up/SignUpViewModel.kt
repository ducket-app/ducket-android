package io.ducket.android.presentation.screens.auth.sign_up

import android.app.Application
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.data.remote.dto.UserCreateDto
import io.ducket.android.domain.interactors.CreateUserInteractor
import io.ducket.android.presentation.navigation.NavArgKey
import io.ducket.android.presentation.navigation.NavArgKey.ARG_CURRENCY_ISO_CODE
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createUserInteractor: CreateUserInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.NotSignedUp)
    val uiState: StateFlow<UiState> get() = _uiState

    private val _screenEvent = Channel<ScreenEvent>()
    val screenEvent: Flow<ScreenEvent> = _screenEvent.receiveAsFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _showStartingBalanceHelpDialog = MutableStateFlow<Boolean>(false)
    val showStartingBalanceHelpDialog: StateFlow<Boolean> get() = _showStartingBalanceHelpDialog

    private val _selectedCurrency = MutableStateFlow<String>("")
    val selectedCurrency: StateFlow<String> get() = _selectedCurrency

    fun onCurrencySelect(isoCode: String) {
        _selectedCurrency.value = isoCode
    }

    fun onSignUp(name: String, email: String, currencyCode: String, password: String) {
        viewModelScope.launch {
            val userCreateDto = UserCreateDto(name, email, password, currencyCode)

            createUserInteractor(userCreateDto).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        _uiState.value = UiState.Loading
                    }
                    is ResourceState.Success -> {
                        _loading.emit(false)
                        _uiState.value = UiState.SignedUp
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        _uiState.value = UiState.NotSignedUp
                        _screenEvent.send(ScreenEvent.ShowMessage(it.msg))
                    }
                }
            }
        }
    }

    fun onStartingBalanceHelpClick() {
        _showStartingBalanceHelpDialog.value = true
    }

    fun onStartingBalanceHelpDialogDismiss() {
        _showStartingBalanceHelpDialog.value = false
    }

    sealed class ScreenEvent {
        class ShowMessage(val text: String) : ScreenEvent()
    }

    sealed class UiState {
        object NotSignedUp : UiState()
        object Loading : UiState()
        object SignedUp : UiState()
    }
}