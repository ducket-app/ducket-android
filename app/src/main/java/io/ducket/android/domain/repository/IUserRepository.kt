package io.ducket.android.domain.repository

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.detailed.UserDetails
import io.ducket.android.data.remote.dto.UserAuthDto
import io.ducket.android.data.remote.dto.UserCreateDto
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun createUser(payload: UserCreateDto): Flow<ResourceState<UserDetails?>>

    fun authUser(payload: UserAuthDto): Flow<ResourceState<UserDetails?>>

    fun getUser(): Flow<ResourceState<UserDetails?>>
}