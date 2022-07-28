package io.ducket.android.presentation.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.domain.interactors.GetAccountsInteractor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAccountsInteractor: GetAccountsInteractor,
) : ViewModel() {

    private val _screenState = MutableLiveData<HomeScreenState>()
    val screenState: LiveData<HomeScreenState> get() = _screenState

    init {
//        viewModelScope.launch {
//            getAccountsInteractor().collect {
//                when (it) {
//                    is ResourceState.Loading -> {
//                        _screenState.value = HomeScreenState.Loading
//                    }
//                    is ResourceState.Success -> {
//                        _screenState.value = HomeScreenState.Success(it.data)
//                    }
//                    is ResourceState.ConnectivityError,
//                    is ResourceState.AuthorizationError,
//                    is ResourceState.Error -> {
//                        _screenState.value = HomeScreenState.Error(it.msg)
//                        // _screenEvent.emit(ScreenEvent.ShowMessage(it.msg))
//                    }
//                }
//            }
//        }
    }
}