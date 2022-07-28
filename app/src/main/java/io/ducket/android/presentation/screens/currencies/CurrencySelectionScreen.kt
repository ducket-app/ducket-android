package io.ducket.android.presentation.screens.currencies

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import io.ducket.android.R
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.presentation.navigation.AppSnackbarManager
import io.ducket.android.presentation.navigation.AuthNavGraph
import io.ducket.android.presentation.navigation.FullScreenDialogTransitions
import io.ducket.android.presentation.navigation.TabsNavGraph
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme
import io.ducket.android.presentation.ui.theme.SpaceMedium
import io.ducket.android.presentation.ui.theme.SpaceSmall


@AuthNavGraph
@Destination(
    navArgsDelegate = CurrencySelectionScreenArgs::class,
    style = FullScreenDialogTransitions::class,
)
@Composable
fun CurrencySelectionScreen(
    viewModel: CurrencySelectionViewModel = hiltViewModel(),
    snackbarManager: AppSnackbarManager,
    resultNavigator: ResultBackNavigator<String>
) {
    val uiState by viewModel.stateFlow.collectAsState()
    var showSearchBar by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is CurrencySelectionUiEvent.ShowMessage -> {
                    snackbarManager.showMessage(it.text)
                }
                is CurrencySelectionUiEvent.NavigateBackWithResult -> {
                    resultNavigator.navigateBack(it.currency)
                }
                is CurrencySelectionUiEvent.NavigateBack -> {
                    resultNavigator.navigateBack()
                }
            }
        }
    }

    CurrencySelectionContent(
        state = uiState,
        showSearchBar = showSearchBar,
        onCurrencySelect = { viewModel.onEvent(CurrencySelectionViewModel.Event.OnSelect(it)) },
        onSearch = { viewModel.onEvent(CurrencySelectionViewModel.Event.OnSearch(it)) },
        onBackClick = { viewModel.onEvent(CurrencySelectionViewModel.Event.OnBack) },
        onCloseSearchClick = { showSearchBar = false },
        onOpenSearchClick = { showSearchBar = true }
    )
}

@Composable
fun CurrencySelectionContent(
    state: CurrencySelectionUiState,
    showSearchBar: Boolean,
    onCurrencySelect: (Long) -> Unit,
    onSearch: (String) -> Unit,
    onBackClick: () -> Unit,
    onCloseSearchClick: () -> Unit,
    onOpenSearchClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        if (showSearchBar) {
            CurrencySelectionSearchAppBar(
                searchText = state.searchQuery,
                onSearch = onSearch,
                onSearchClose = onCloseSearchClick,
            )
        } else {
            CurrencySelectionAppBar(
                onBackClick = onBackClick,
                onOpenSearch = onOpenSearchClick,
            )
        }

        if (state.isLoading) {
            CurrencySelectionLoading()
        } else {
            CurrencySelectionList(
                currencyList = state.currencyList,
                selectedCurrency = state.selectedCurrency,
                onCurrencySelect = onCurrencySelect,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrencySelectionList(
    currencyList: List<Currency>,
    selectedCurrency: String,
    onCurrencySelect: (Long) -> Unit,
) {
    LazyColumn {
        items(
            items = currencyList,
            key = { it.id }
        ) { currency ->
            CurrencyItem(
                currencyIsoCode = currency.isoCode,
                currencyName = currency.name,
                selected = currency.isoCode == selectedCurrency,
                onClick = {
                    onCurrencySelect(currency.id)
                }
            )
        }
    }
}

@Composable
fun CurrencySelectionLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.primary
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun CurrencyItem(
    currencyIsoCode: String,
    currencyName: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.surface,
        shape = RectangleShape,
        elevation = 1.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(SpaceMedium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = currencyIsoCode,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    modifier = Modifier.alpha(ContentAlpha.disabled),
                    text = currencyName,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = Icons.Default.Check.name,
                    tint = MaterialTheme.colors.secondary,
                )
            }
        }
    }
}

@Composable
fun CurrencySelectionAppBar(
    onBackClick: () -> Unit,
    onOpenSearch: () -> Unit,
) {
    TopAppBar(
        elevation = 2.dp,
        title = {
            Text(text = stringResource(id = R.string.select_currency_title))
        },
        actions = {
            IconButton(onClick = onOpenSearch) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    tint = MaterialTheme.colors.onSurface,
                    contentDescription = Icons.Filled.Search.name,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = Icons.Default.Close.name,
                    modifier = Modifier.padding(horizontal = SpaceSmall)
                )
            }
        },
        backgroundColor = MaterialTheme.colors.surface,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CurrencySelectionSearchAppBar(
    searchText: String,
    onSearch: (String) -> Unit,
    onSearchClose: () -> Unit,
) {
    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(Unit) {
        searchFocusRequester.requestFocus()
        onDispose { }
    }

    BackHandler(enabled = true) {
        onSearchClose()
        onSearch("")
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.surface,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(searchFocusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                },
            value = searchText,
            onValueChange = onSearch,
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.disabled),
                    text = stringResource(id = R.string.search_hint),
                    color = MaterialTheme.colors.onSurface,
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {
                        onSearchClose()
                        onSearch("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = Icons.Filled.ArrowBack.name,
                        tint = MaterialTheme.colors.onSurface,
                    )
                }
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onSearch("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            tint = MaterialTheme.colors.onSurface,
                            contentDescription = null,
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(searchText)
                },
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.onSurface,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
            )
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun CurrencyItemPreview() {
    DucketAndroidTheme {
        CurrencyItem(
            currencyIsoCode = "Title",
            currencyName = "Subtitle",
            selected = true,
            onClick = {}
        )
    }
}
