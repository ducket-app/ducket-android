package io.ducket.android.domain.repository

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.Currency
import kotlinx.coroutines.flow.Flow

interface ICurrencyRepository {

    fun getCurrencies(): Flow<ResourceState<List<Currency>>>

    fun searchCurrencies(query: String): Flow<ResourceState<List<Currency>>>
}