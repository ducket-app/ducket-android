package io.ducket.android.presentation.components

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import io.ducket.android.R
import io.ducket.android.presentation.ui.theme.*

@Composable
fun AccountsIcon(
    modifier: Modifier = Modifier,
    tint: Color = Orange,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_credit_cards),
        tint = tint,
        contentDescription = "accounts",
    )
}

@Composable
fun BudgetsIcon(
    modifier: Modifier = Modifier,
    tint: Color = Green,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_wallet_coins),
        tint = tint,
        contentDescription = "budgets",
    )
}

@Composable
fun ImportsIcon(
    modifier: Modifier = Modifier,
    tint: Color = LightBlue,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_file_download),
        tint = tint,
        contentDescription = "imports",
    )
}

@Composable
fun GoalsIcon(
    modifier: Modifier = Modifier,
    tint: Color = Red,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_target_goal),
        tint = tint,
        contentDescription = "goals",
    )
}

@Composable
fun DiscountsIcon(
    modifier: Modifier = Modifier,
    tint: Color = Pink,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_discount),
        tint = tint,
        contentDescription = "discounts",
    )
}

@Composable
fun ShoppingListIcon(
    modifier: Modifier = Modifier,
    tint: Color = Yellow,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_list_checkmark),
        tint = tint,
        contentDescription = "shopping",
    )
}

@Composable
fun SettingsIcon(
    modifier: Modifier = Modifier,
    tint: Color = Purple,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_settings),
        tint = tint,
        contentDescription = "settings",
    )
}
