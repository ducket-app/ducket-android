package io.ducket.android.domain.repository

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.detailed.AccountDetails
import kotlinx.coroutines.flow.Flow

interface IAccountRepository {

    fun getAccount(id: Long): Flow<ResourceState<AccountDetails?>>

    fun getAccounts(): Flow<ResourceState<List<AccountDetails>>>
}