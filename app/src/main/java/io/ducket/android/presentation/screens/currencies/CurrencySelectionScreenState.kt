package io.ducket.android.presentation.screens.currencies

import io.ducket.android.data.local.entity.Currency

sealed class CurrencySelectionUiEvent {
    class ShowMessage(val text: String) : CurrencySelectionUiEvent()
    class NavigateBackWithResult(val currency: String) : CurrencySelectionUiEvent()
    object NavigateBack : CurrencySelectionUiEvent()
}

data class CurrencySelectionUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCurrency: String = "",
    val currencyList: List<Currency> = listOf(),
)