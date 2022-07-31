package io.ducket.android.data.local

import androidx.room.TypeConverter
import io.ducket.android.data.remote.dto.account.AccountType
import io.ducket.android.domain.model.CategoryItem
import java.math.BigDecimal
import java.time.Instant

class Converters {

    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String {
        return input?.toString() ?: "0.00"
    }

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal {
        if (input == null || input.isEmpty()) return BigDecimal.ZERO
        return BigDecimal(input)
    }

    @TypeConverter
    fun categoryItemToString(input: CategoryItem?): String {
        return input?.name ?: CategoryItem.UNCATEGORIZED.name
    }

    @TypeConverter
    fun stringToCategoryItem(input: String?): CategoryItem {
        return CategoryItem.parse(input ?: "")
    }

    @TypeConverter
    fun timestampToInstant(input: Long?): Instant? {
        return input?.let { Instant.ofEpochMilli(input) }
    }

    @TypeConverter
    fun instantToTimestamp(input: Instant?): Long? {
        return input?.toEpochMilli()
    }

    @TypeConverter
    fun stringToAccountType(input: String) = enumValueOf<AccountType>(input)

    @TypeConverter
    fun accountTypeToString(input: AccountType) = input.name
}