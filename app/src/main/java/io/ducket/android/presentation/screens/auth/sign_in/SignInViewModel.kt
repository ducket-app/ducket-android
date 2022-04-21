package io.ducket.android.presentation.screens.auth.sign_in

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.data.remote.dto.UserAuthDto
import io.ducket.android.domain.interactors.AuthUserInteractor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUserInteractor: AuthUserInteractor,
) : ViewModel() {

//    private val _screenState = MutableStateFlow<SignInScreenState>(SignInScreenState.NotSignedIn)
//    val screenState: MutableStateFlow<SignInScreenState> get() = _screenState

//   private val _screenEvent = MutableSharedFlow<ScreenEvent>()
//    val screenEvent: SharedFlow<ScreenEvent> get() = _screenEvent.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.NotSignedIn)
    val uiState: MutableStateFlow<UiState> get() = _uiState

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: MutableStateFlow<Boolean> get() = _loading

    private val _screenEvent = Channel<ScreenEvent>()
    val screenEvent: Flow<ScreenEvent> = _screenEvent.receiveAsFlow()

    fun onSignIn(email: String, password: String) {
        viewModelScope.launch {
            authUserInteractor(UserAuthDto(email, password)).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        _uiState.value = UiState.Loading
                    }
                    is ResourceState.Success -> {
                        _loading.emit(false)
                        _uiState.value = UiState.SignedIn
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        _uiState.value = UiState.NotSignedIn
                        _screenEvent.send(ScreenEvent.ShowMessage(it.msg))
                    }
                }
            }
        }
    }

    sealed class ScreenEvent {
        class ShowMessage(val text: String) : ScreenEvent()
    }

    sealed class UiState {
        object NotSignedIn : UiState()
        object Loading : UiState()
        object SignedIn : UiState()
    }
}