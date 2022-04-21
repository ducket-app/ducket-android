package io.ducket.android.presentation.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import io.ducket.android.data.local.entity.detailed.AccountDetails
import io.ducket.android.presentation.screens.records.CardCaption
import io.ducket.android.presentation.screens.records.CardIcon
import io.ducket.android.presentation.screens.records.CardTitle
import io.ducket.android.presentation.screens.records.Record
import io.ducket.android.presentation.ui.theme.subtitle
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@ExperimentalPagerApi
@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState = viewModel.screenState.observeAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 4.dp,
            )
        },
        content = {
            when(uiState) {
                is HomeScreenState.Success -> {
                    HomeScreenContent(
                        accounts = uiState.accounts
                    )
                }
                is HomeScreenState.Loading -> {
                    HomeScreenLoading()
                }
                is HomeScreenState.Error -> {
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        scaffoldState.snackbarHostState.showSnackbar(uiState.msg)
                    }
                }
            }
        }
    )
}

@Composable
fun HomeScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.primary
        )
    }
}

@ExperimentalPagerApi
@Composable
fun HomeScreenContent(
    accounts: List<AccountDetails>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DashboardCardHeader(
            title = "Accounts balance",
            action = "View all",
        )
        Spacer(modifier = Modifier.height(16.dp))
        AccountsBalanceCard(
            accounts = accounts
        )
        // AccountsSummary()
        // (text = "Home screen", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun DashboardCardHeader(
    title: String,
    action: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground,
        )
        Text(
            text = action,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondary,
        )
    }
}

@ExperimentalPagerApi
@Composable
fun AccountsSummaryOptionRow() {
    val options = listOf("All", "Millennium", "ING", "Cash", "Belarusbank", "Cash EUR", "PKO Bank")

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val selectedOptionIdx = pagerState.currentPage
    val rowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // var selectedIndex by remember { mutableStateOf(-1) }

    val onSelectOption = { idx: Int ->
        coroutineScope.launch {
            pagerState.animateScrollToPage(idx)
        }
    }

    LaunchedEffect(selectedOptionIdx) {
        rowState.animateScrollToItem(selectedOptionIdx - 1)
    }
}

@Composable
fun AccountsBalanceCard(
    accounts: List<AccountDetails>,
) {
    val filtersScrollState = rememberScrollState()
    var personalSelected by remember { mutableStateOf(true) }
    var followingSelected by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 24.dp,
                    bottom = 24.dp
                ),
        ) {
            AccountsBalanceCardHeader(
                accounts = accounts
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.horizontalScroll(filtersScrollState),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BalanceFilter()

                BalanceInstantFilter(
                    selected = personalSelected,
                    text = "Personal",
                    icon = Icons.Filled.AccountCircle,
                    onClick = {
                        personalSelected = !personalSelected
                    }
                )

                BalanceInstantFilter(
                    selected = followingSelected,
                    text = "Following",
                    icon = Icons.Filled.SupervisedUserCircle,
                    onClick = {
                        followingSelected = !followingSelected
                    }
                )
            }
        }
    }
}

@Composable
fun AccountSummaryRecords() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Records",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.subtitle,
            )
            Text(
                text = "View all",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Record(
            title = "Salary",
            caption = "Wynogradzenie za 02/2022",
            secondaryTitle = "zl 9,040.00",
            secondaryCaption = "01.02.2022",
            icon = Icons.Filled.Paid,
        )
    }
}

@Composable
fun AccountsVisibilityColumn() {
    val columnState = rememberLazyListState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Accounts (4)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.subtitle,
            )
            Text(
                text = "Toggle visiblity",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            state = columnState,
            modifier = Modifier.height(104.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(items = listOf("Millennium bank", "IGN", "Cash", "PKO Bank")) { item ->
                AccountPreview(
                    title = item,
                    caption = "12,312.12",
                    icon = Icons.Filled.AccountBalance,
                )
            }
        }
    }
}

@Composable
fun BalanceFilter(
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val color = if (selected)
        MaterialTheme.colors.secondary.copy(0.1f)
    else MaterialTheme.colors.background

    val actionColor = if (selected)
        MaterialTheme.colors.secondary
    else MaterialTheme.colors.subtitle

    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .clickable {
                onClick()
            }
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Filled.Tune,
            contentDescription = Icons.Filled.Tune.name,
            tint = actionColor,
        )
    }
}

@Composable
fun BalanceInstantFilter(
    selected: Boolean = false,
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
) {
    val color = if (selected)
        MaterialTheme.colors.secondary.copy(0.1f)
    else MaterialTheme.colors.background

    val actionColor = if (selected)
        MaterialTheme.colors.secondary
    else MaterialTheme.colors.subtitle

    Row(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .clickable {
                onClick()
            }
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon,
                contentDescription = icon.name,
                tint = actionColor,
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body2,
            color = actionColor,
        )
    }
}

@Composable
fun AccountPreview(
    title: String,
    caption: String,
    icon: ImageVector,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(RoundedCornerShape(8.dp))
        .clickable {

        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CardIcon(
                icon = icon,
                desc = "category_icon",
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                CardTitle(
                    text = title,
                )
                CardCaption(
                    text = caption,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Filled.Visibility,
                contentDescription = "desc",
                tint = MaterialTheme.colors.subtitle,
            )
        }
    }
}

@Composable
fun AccountSummaryButton(
    text: String = "Add record",
    icon: ImageVector = Icons.Outlined.Add
) {
    val backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.05f)

    Row(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(
                start = 14.dp,
                end = 14.dp,
                top = 6.dp,
                bottom = 6.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = icon,
            contentDescription = icon.name,
            tint = MaterialTheme.colors.secondaryVariant,
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondaryVariant,
        )
    }
}

@Composable
fun AccountsBalanceCardHeader(
    accounts: List<AccountDetails>
) {
    val balance = accounts.map { it.account.balance }.sumOf { it }
    val balanceFormat = DecimalFormat("#,##0.00")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = balanceFormat.format(balance),
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h4,
            )
            Text(
                text = "Accounts • ${accounts.size}",
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary,
            )
        }
        RoundedIcon(
            icon = Icons.Outlined.AccountBalance
        )
    }
}

@Composable
fun RoundedIcon(
    icon: ImageVector,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.background)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            tint = MaterialTheme.colors.subtitle,
        )
    }
}

@Composable
fun AccountsSummaryOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (selected)
        MaterialTheme.colors.surface
    else MaterialTheme.colors.background

    val textColor = if (selected)
        MaterialTheme.colors.onSurface
    else MaterialTheme.colors.onSurface.copy(alpha = 0.5f)

    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        color = textColor,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(size = 8.dp))
            .clickable {
                onClick()
            }
            .background(backgroundColor)
            .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 8.dp),
    )
}