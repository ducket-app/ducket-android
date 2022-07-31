package io.ducket.android.data.remote.dto

import androidx.compose.runtime.Immutable

@Immutable
data class ErrorResponse(
    val status: ErrorStatusResponse,
    val message: String,
    val timestamp: String,
)