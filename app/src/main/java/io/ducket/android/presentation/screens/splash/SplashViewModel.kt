package io.ducket.android.presentation.screens.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.data.local.AppDataStore
import io.ducket.android.data.local.userPreferencesStored
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val datastore: AppDataStore,
) : ViewModel() {

    private val _authorized = MutableStateFlow<Boolean>(false)
    val authorized: StateFlow<Boolean> get() = _authorized

    init {
        viewModelScope.launch {
            _authorized.value = datastore.getUserPreferences().first().userPreferencesStored()
        }
    }
}