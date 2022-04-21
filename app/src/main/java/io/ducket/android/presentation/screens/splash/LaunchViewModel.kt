package io.ducket.android.presentation.screens.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ducket.android.data.local.AppDataStore
import io.ducket.android.data.local.userPreferencesStored
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val datastore: AppDataStore,
) : ViewModel() {

    private val _authState = MutableLiveData<Boolean>()
    val authState: LiveData<Boolean> get() = _authState

    init {
        viewModelScope.launch {
            _authState.value = datastore.getUserPreferences().first().userPreferencesStored()
        }
    }
}