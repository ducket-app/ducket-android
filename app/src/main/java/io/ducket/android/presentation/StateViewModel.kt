package io.ducket.android.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class StateViewModel<S : Any>(initialState: S) : ViewModel() {

    private val _stateFlow: MutableStateFlow<S> = MutableStateFlow(initialState)
    open val stateFlow: StateFlow<S> = _stateFlow.asStateFlow()

    protected val state: S get() = stateFlow.value

    protected fun updateState(reduce: S.() -> S) {
        val newState = state.reduce()
        _stateFlow.value = newState
    }
}