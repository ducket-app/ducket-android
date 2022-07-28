package io.ducket.android.presentation.screens.currencies

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.common.ResourceState
import io.ducket.android.domain.interactors.GetCurrenciesInteractor
import io.ducket.android.domain.interactors.SearchCurrenciesInteractor
import io.ducket.android.presentation.StateViewModel
import io.ducket.android.presentation.screens.navArgs
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCurrenciesInteractor: GetCurrenciesInteractor,
    private val searchCurrenciesInteractor: SearchCurrenciesInteractor,
) : StateViewModel<CurrencySelectionUiState>(CurrencySelectionUiState()) {

    private val _uiEvent = Channel<CurrencySelectionUiEvent>()
    val uiEvent: Flow<CurrencySelectionUiEvent> = _uiEvent.receiveAsFlow()

    init {
        updateState {
            copy(selectedCurrency = savedStateHandle.navArgs<CurrencySelectionScreenArgs>().currency)
        }

        getCurrencies()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnSearch -> {
                updateState {
                    copy(searchQuery = event.query)
                }
                searchCurrencies(event.query)
            }
            is Event.OnSelect -> {
                viewModelScope.launch {
                    val currency = state.currencyList.firstOrNull { it.id == event.id }!!.isoCode
                    _uiEvent.send(CurrencySelectionUiEvent.NavigateBackWithResult(currency))
                }
            }
            is Event.OnBack -> {
                viewModelScope.launch {
                    _uiEvent.send(CurrencySelectionUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun getCurrencies() {
        viewModelScope.launch {
            getCurrenciesInteractor.invoke().collect {
                when (it) {
                    is ResourceState.Loading -> {
                        updateState { copy(isLoading = true) }
                    }
                    is ResourceState.Success -> {
                        updateState { copy(isLoading = false, currencyList = it.data) }
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        updateState { copy(isLoading = false) }
                        _uiEvent.send(CurrencySelectionUiEvent.ShowMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun searchCurrencies(query: String) {
        viewModelScope.launch {
            searchCurrenciesInteractor(query).collect {
                when (it) {
                    is ResourceState.Loading -> {
                        updateState { copy(isLoading = true) }
                    }
                    is ResourceState.Success -> {
                        updateState { copy(isLoading = false, currencyList = it.data) }
                    }
                    is ResourceState.ConnectivityError,
                    is ResourceState.AuthorizationError,
                    is ResourceState.Error -> {
                        updateState { copy(isLoading = false) }
                        _uiEvent.send(CurrencySelectionUiEvent.ShowMessage(it.msg))
                    }
                }
            }
        }
    }

    sealed class Event {
        data class OnSearch(val query: String) : Event()
        data class OnSelect(val id: Long) : Event()
        object OnBack : Event()
    }
}