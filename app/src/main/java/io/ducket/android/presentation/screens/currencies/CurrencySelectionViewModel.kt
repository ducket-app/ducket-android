package io.ducket.android.presentation.screens.currencies

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.domain.interactors.GetCurrenciesInteractor
import io.ducket.android.domain.interactors.SearchCurrenciesInteractor
import io.ducket.android.presentation.navigation.NavArgKey
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCurrenciesInteractor: GetCurrenciesInteractor,
    private val searchCurrenciesInteractor: SearchCurrenciesInteractor,
) : ViewModel() {

    private val _screenState = MutableLiveData<UiState>()
    val screenState: LiveData<UiState> get() = _screenState

    val selectedCurrency: MutableState<String> = mutableStateOf(savedStateHandle.get(NavArgKey.ARG_CURRENCY_ISO_CODE) ?: "")

    init {
        getCurrencies()
    }

    fun getCurrencies() {
        viewModelScope.launch {
            getCurrenciesInteractor().collect {
                when (it) {
                    is ResourceState.Loading -> {
                        _screenState.value = UiState.Loading
                    }
                    is ResourceState.Success -> {
                        _screenState.value = UiState.Loaded(it.data)
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        _screenState.value = UiState.Error(it.msg)
                    }
                }
            }
        }
    }

    fun searchCurrencies(query: String) {
        viewModelScope.launch {
            searchCurrenciesInteractor(query).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        _screenState.value = UiState.Loading
                    }
                    is ResourceState.Success -> {
                        _screenState.value = UiState.Loaded(it.data)
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        _screenState.value = UiState.Error(it.msg)
                    }
                }
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Loaded(val currencyList: List<Currency>) : UiState()
        data class Error(val msg: String) : UiState()
    }
}