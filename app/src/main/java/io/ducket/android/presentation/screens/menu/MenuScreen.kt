package io.ducket.android.presentation.screens.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ducket.android.R
import io.ducket.android.presentation.ui.theme.*

@Composable
fun MenuScreen(
    // viewModel: SignInViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .drawWithContent {
                        // hack to remove elevation shadow
                        clipRect {
                            this@drawWithContent.drawContent()
                        }
                    },
                elevation = AppBarDefaults.TopAppBarElevation,
                color = MaterialTheme.colors.surface,
            ) {
                Row(
                    modifier = Modifier.padding(start = SpaceMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.menu_tab_title),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        },
        content = {
            MenuContent(
                name = "Preview",
                email = "preview@test.com",
                recordsCount = 100,
                followersCount = 1,
                followingCount = 2,
                onAccountsClick = {},
                onBudgetsClick = {},
                onImportsClick = {},
                onGoalsClick = {},
                onLoyaltyCardsClick = {},
                onShoppingListsClick = {},
            )
        }
    )
}

@Preview
@Composable
private fun UserSummaryPreview() {
    DucketAndroidTheme(isPreview = true) {
        MenuUserSummary(
            recordsCount = 324,
            followersCount = 1,
            followingCount = 2,
        )
    }
}

@Composable
fun MenuUserSummary(
    recordsCount: Int,
    followersCount: Int,
    followingCount: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(SpaceMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        UserSummaryColumn(
            modifier = Modifier.weight(1f),
            title = recordsCount.toString(),
            labelResId = R.string.records_label,
        )

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        UserSummaryColumn(
            modifier = Modifier.weight(1f),
            title = followersCount.toString(),
            labelResId = R.string.followers_label,
        )

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        UserSummaryColumn(
            modifier = Modifier.weight(1f),
            title = followingCount.toString(),
            labelResId = R.string.following_label,
        )
    }
}

@Composable
fun UserSummaryColumn(
    modifier: Modifier = Modifier,
    title: String,
    @StringRes labelResId: Int,
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )

        Text(
            text = stringResource(id = labelResId),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
fun MenuContent(
    name: String,
    email: String,
    recordsCount: Int,
    followersCount: Int,
    followingCount: Int,
    onAccountsClick: () -> Unit,
    onBudgetsClick: () -> Unit,
    onImportsClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onLoyaltyCardsClick: () -> Unit,
    onShoppingListsClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colors.background)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MenuHeader(
            name = name,
            email = email,
            recordsCount = recordsCount,
            followersCount = followersCount,
            followingCount = followingCount,
        )

        MenuGroup(
            title = stringResource(id = R.string.feature_label),
        ) {
            MenuClickableItem(
                titleResId = R.string.accounts_title,
                drawableResId = R.drawable.ic_credit_cards,
                drawableTint = Orange,
                onClick = onAccountsClick,
            )
            MenuClickableItem(
                titleResId = R.string.budgets_title,
                drawableResId = R.drawable.ic_wallet_coins,
                drawableTint = Green,
                onClick = onBudgetsClick,
            )
            MenuClickableItem(
                titleResId = R.string.imports_title,
                drawableResId = R.drawable.ic_file_download,
                drawableTint = LightBlue,
                onClick = onImportsClick,
            )
            MenuClickableItem(
                titleResId = R.string.goals_title,
                drawableResId = R.drawable.ic_target_goal,
                drawableTint = Red,
                onClick = onGoalsClick,
            )
            MenuClickableItem(
                titleResId = R.string.loyalty_cards_title,
                drawableResId = R.drawable.ic_discount,
                drawableTint = Pink,
                onClick = onLoyaltyCardsClick,
            )
            MenuClickableItem(
                titleResId = R.string.shopping_lists_title,
                drawableResId = R.drawable.ic_list_checkmark,
                drawableTint = Yellow,
                onClick = onShoppingListsClick,
            )
        }

        MenuGroup(
            title = stringResource(id = R.string.preferences_label),
        ) {
            MenuSelectableItem(
                value = "Default",
                titleResId = R.string.theme_label,
                drawableResId = R.drawable.ic_brush,
                onClick = {}
            )
            MenuSelectableItem(
                value = "English",
                titleResId = R.string.language_label,
                drawableResId = R.drawable.ic_language,
                onClick = {}
            )
            MenuSelectableItem(
                value = "PLN",
                titleResId = R.string.settlement_currency_label,
                drawableResId = R.drawable.ic_banknote,
                onClick = {}
            )
            MenuClickableItem(
                titleResId = R.string.change_password_label,
                drawableResId = R.drawable.ic_edit_password,
                onClick = {}
            )
            MenuClickableItem(
                titleResId = R.string.import_rules_label,
                drawableResId = R.drawable.ic_magic,
                onClick = {}
            )
            MenuClickableItem(
                titleResId = R.string.labels_label,
                drawableResId = R.drawable.ic_labels,
                onClick = {}
            )
            MenuClickableItem(
                titleResId = R.string.categories_label,
                drawableResId = R.drawable.ic_paragraph,
                onClick = {}
            )
            MenuSwitchableItem(
                value = true,
                titleResId = R.string.notifications_label,
                drawableResId = R.drawable.ic_bell,
                onCheck = {}
            )
            MenuSwitchableItem(
                value = true,
                titleResId = R.string.two_factor_auth_label,
                drawableResId = R.drawable.ic_fingerprint,
                onCheck = {}
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(56.dp)
                .background(MaterialTheme.colors.background)
        )
    }
}

@Composable
fun MenuUserInfo(
    name: String,
    email: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(SpaceMedium),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = name.split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .reduce { acc, s -> acc + s }
                    .uppercase(),
                color = MaterialTheme.colors.surface,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        Column(
            modifier = Modifier
                .padding(start = SpaceMedium)
                .align(Alignment.CenterVertically)
                .weight(weight = 1f)
        ) {
            Text(
                text = name,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = email,
                    style = MaterialTheme.typography.body1
                )
            }
        }

        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            imageVector = Icons.Default.ArrowForwardIos,
            tint = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
            contentDescription = "",
        )
    }
}

@Composable
fun MenuHeader(
    name: String,
    email: String,
    recordsCount: Int,
    followersCount: Int,
    followingCount: Int,
) {
    Surface(
        elevation = 2.dp
    ) {
        Column {
            MenuUserInfo(
                name = name,
                email = email,
            )
            MenuUserSummary(
                recordsCount = recordsCount,
                followersCount = followersCount,
                followingCount = followingCount,
            )
        }
    }
}

@Composable
fun MenuGroup(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MaterialTheme.colors.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colors.onBackground.copy(ContentAlpha.disabled),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )

        content()
    }
}

@Composable
fun MenuClickableItem(
    @StringRes titleResId: Int,
    @DrawableRes drawableResId: Int,
    drawableTint: Color = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        elevation = 1.dp
    ) {
        MenuItemLayout(
            titleResId = titleResId,
            drawableResId = drawableResId,
            drawableTint = drawableTint,
        ) {
            Icon(
                modifier = Modifier.wrapContentSize(),
                imageVector = Icons.Default.ChevronRight,
                tint = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
                contentDescription = "",
            )
        }
    }
}

@Composable
fun MenuSwitchableItem(
    value: Boolean,
    @StringRes titleResId: Int,
    @DrawableRes drawableResId: Int,
    drawableTint: Color = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
    onCheck: (Boolean) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        elevation = 1.dp
    ) {
        MenuItemLayout(
            titleResId = titleResId,
            drawableResId = drawableResId,
            drawableTint = drawableTint,
        ) {
            Switch(
                modifier = Modifier.wrapContentSize(),
                checked = value,
                onCheckedChange = onCheck,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary,
                    uncheckedThumbColor = MaterialTheme.colors.surface,
                )
            )
        }
    }
}

@Composable
fun MenuSelectableItem(
    value: String,
    @StringRes titleResId: Int,
    @DrawableRes drawableResId: Int,
    drawableTint: Color = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
    onClick: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(value)
            },
        elevation = 1.dp
    ) {
        MenuItemLayout(
            titleResId = titleResId,
            drawableResId = drawableResId,
            drawableTint = drawableTint,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpaceSmall),
            ) {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.disabled),
                    text = value,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Icon(
                    modifier = Modifier.wrapContentSize(),
                    imageVector = Icons.Default.ChevronRight,
                    tint = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
private fun MenuItemLayout(
    @StringRes titleResId: Int,
    @DrawableRes drawableResId: Int,
    drawableTint: Color,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(SpaceMedium)
                .fillMaxWidth()
                .height(24.dp),
            horizontalArrangement = Arrangement.spacedBy(SpaceMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = drawableResId),
                tint = drawableTint,
                contentDescription = "",
            )

            Text(
                modifier = Modifier.weight(weight = 1f),
                text = stringResource(id = titleResId),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            content()
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
                .padding(start = SpaceMedium, end = SpaceMedium)
        )
    }
}

@Preview
@Composable
private fun UserHeaderPreview() {
    DucketAndroidTheme(isPreview = true) {
        MenuHeader(
            name = "Test Preview",
            email = "preview@test.com",
            recordsCount = 100,
            followersCount = 1,
            followingCount = 2,
        )
    }
}

@Preview
@Composable
private fun MenuScreenPreview() {
    DucketAndroidTheme(isPreview = true) {
        MenuScreen()
    }
}

@Preview
@Composable
private fun MenuItemPreview() {
    DucketAndroidTheme(isPreview = true) {
        MenuClickableItem(
            titleResId = R.string.accounts_title,
            drawableResId = R.drawable.ic_credit_cards,
            drawableTint = Orange,
            onClick = { },
        )
    }
}
//
//@Preview
//@Composable
//private fun MenuConfigurationItemPreview() {
//    DucketAndroidTheme(isPreview = true) {
//        MenuSelectableItem(
//            titleResId = R.string.theme_label,
//            drawableResId = R.drawable.ic_brush,
//            value = "Preview",
//            onClick = {},
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun MenuConfigurationSwitchableItemPreview() {
//    DucketAndroidTheme(isPreview = true) {
//        MenuSwitchableItem(
//            titleResId = R.string.notifications_label,
//            drawableResId = R.drawable.ic_bell,
//            value = true,
//            onCheck = {}
//        )
//    }
//}
