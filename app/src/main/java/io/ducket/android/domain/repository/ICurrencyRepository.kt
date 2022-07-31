package io.ducket.android.domain.repository

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface ICurrencyRepository {

    fun getCurrencies(): Flow<ResourceState<List<CurrencyEntity>>>

    fun searchCurrencies(query: String): Flow<ResourceState<List<CurrencyEntity>>>
}