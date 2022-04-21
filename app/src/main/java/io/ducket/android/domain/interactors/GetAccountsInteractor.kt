package io.ducket.android.domain.interactors

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.detailed.AccountDetails
import io.ducket.android.domain.repository.IAccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountsInteractor@Inject constructor(
    private val accountRepository: IAccountRepository
) : Interactor() {

    operator fun invoke(): Flow<ResourceState<List<AccountDetails>>> = accountRepository.getAccounts()
}