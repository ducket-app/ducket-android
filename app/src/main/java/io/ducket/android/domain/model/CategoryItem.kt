package io.ducket.android.domain.model


import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import io.ducket.android.R

enum class CategoryItem(
    @DrawableRes val drawableId: Int,
    val color: Color,
) {
    HOUSING(R.drawable.ic_list_checkmark, Color(0xFFFFEB3B)),
    BUILDING_AND_REPAIR(R.drawable.ic_list_checkmark, Color(0xFFFFEB3B)),
    MORTGAGE(R.drawable.ic_list_checkmark, Color(0xFFFFEB3B)),
    RENT_PAYMENT(R.drawable.ic_list_checkmark, Color(0xFFFFEB3B)),
    FURNITURE_AND_DECOR(R.drawable.ic_list_checkmark, Color(0xFFFFEB3B)),

    UNCATEGORIZED(R.drawable.ic_list_checkmark, Color(0xFFBDBDBD));

    companion object {
        fun parse(string: String): CategoryItem {
            return try {
                valueOf(string)
            } catch (e: Exception) {
                UNCATEGORIZED
            }
        }
    }
}