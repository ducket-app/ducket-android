package io.ducket.android.data.remote.dto

data class ProgressDto(
    val progress: Double,
    val recordsCount: Int,
    val spendingCap: Double,
    val spent: Double,
)