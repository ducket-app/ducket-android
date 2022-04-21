package io.ducket.android.domain.interactors

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.detailed.UserDetails
import io.ducket.android.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserInteractor @Inject constructor(
    private val userRepository: IUserRepository
) : Interactor() {

    operator fun invoke(userId: String): Flow<ResourceState<UserDetails>> = flow {
        // emit(query { userRepository.getUserDetails(userId).toModel() })
    }
}