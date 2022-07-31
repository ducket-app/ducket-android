package io.ducket.android.domain.repository

import io.ducket.android.common.ResourceState
import io.ducket.android.data.local.entity.detailed.UserDetails
import io.ducket.android.data.remote.dto.user.UserAuth
import io.ducket.android.data.remote.dto.user.UserCreate
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun createUser(payload: UserCreate): Flow<ResourceState<UserDetails?>>

    fun authUser(payload: UserAuth): Flow<ResourceState<UserDetails?>>

    fun getUser(): Flow<ResourceState<UserDetails?>>
}