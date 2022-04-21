package io.ducket.android.data.remote.dto

data class BudgetDto(
    val id: Long,
    val amount: Double,
    val category: CategoryDto,
    val account: List<AccountDto>,
    val currency: CurrencyDto,
    val isClosed: Boolean,
    val name: String,
    val period: PeriodDto,
    val progress: ProgressDto,
    val modifiedAt: String,
    val createdAt: String,
)