package io.ducket.android.domain.interactors

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.detailed.UserDetails
import io.ducket.android.data.remote.dto.user.UserCreate
import io.ducket.android.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserInteractor @Inject constructor(
    private val userRepository: IUserRepository
) : Interactor() {

    operator fun invoke(payload: UserCreate): Flow<ResourceState<UserDetails?>> = userRepository.createUser(payload)
}