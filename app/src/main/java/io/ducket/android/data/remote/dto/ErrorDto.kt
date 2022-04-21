package io.ducket.android.data.remote.dto

import androidx.compose.runtime.Immutable

@Immutable
data class ErrorDto(
    val status: ErrorStatusDto,
    val message: String,
    val timestamp: String,
)