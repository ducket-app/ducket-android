package io.ducket.android.presentation.screens.home

import io.ducket.android.data.local.entity.detailed.AccountDetails


sealed class HomeScreenState {
    object Loading : HomeScreenState()
    data class Success(val accounts: List<AccountDetails>) : HomeScreenState()
    data class Error(val msg: String) : HomeScreenState()
}
