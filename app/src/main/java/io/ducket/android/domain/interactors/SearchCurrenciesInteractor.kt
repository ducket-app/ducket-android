package io.ducket.android.domain.interactors

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.domain.repository.ICurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCurrenciesInteractor @Inject constructor(
    private val currencyRepository: ICurrencyRepository
) : Interactor() {

    operator fun invoke(query: String): Flow<ResourceState<List<Currency>>> = currencyRepository.searchCurrencies(query)
}