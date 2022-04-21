package io.ducket.android.data.local

import androidx.room.TypeConverter
import io.ducket.android.domain.model.CategoryItem
import java.math.BigDecimal

class Converters {

    @TypeConverter
    fun bigDecimalToDouble(input: BigDecimal?): Double {
        return input?.toDouble() ?: 0.0
    }

    @TypeConverter
    fun doubleToBigDecimal(input: Double?): BigDecimal {
        if (input == null) return BigDecimal.ZERO
        return BigDecimal.valueOf(input) ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun categoryItemToString(input: CategoryItem?): String {
        return input?.name ?: CategoryItem.UNCATEGORIZED.name
    }

    @TypeConverter
    fun stringToCategoryItem(input: String?): CategoryItem {
        return CategoryItem.parse(input ?: "")
    }
}