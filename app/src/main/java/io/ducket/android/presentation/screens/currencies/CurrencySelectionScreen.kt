package io.ducket.android.presentation.screens.currencies

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
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
import io.ducket.android.R
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme
import io.ducket.android.presentation.ui.theme.SpaceMedium
import io.ducket.android.presentation.ui.theme.SpaceSmall

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun CurrencySelectionScreen(
    viewModel: CurrencySelectionViewModel,
    scaffoldState: ScaffoldState,
    navigateBack: (isoCode: String) -> Unit,
) {
    val screenState = rememberCurrencySelectionScreenState()
    val uiState = viewModel.screenState.observeAsState().value
    val selectedCurrency by viewModel.selectedCurrency

    Scaffold(
        topBar = {
            CurrencySelectionAppBar(
                showSearchAppBar = screenState.showSearch,
                searchText = screenState.searchText,
                onBackClick = {
                    navigateBack(selectedCurrency)
                },
                onSearchTextChange = {
                    // viewModel.searchTextState.value = it
                    screenState.onSearch(it)
                    // searchText = it

                    viewModel.searchCurrencies(it)
                },
                onSearchClick = {
                    viewModel.searchCurrencies(it)
                },
                onCloseSearchClick = {
                    screenState.onCloseSearch()
                    // showSearch = false
                    // viewModel.searchAppBarVisibilityState.value = false
                },
                onOpenSearchClick = {
                    screenState.onOpenSearch()
                    // showSearch = true
                    // viewModel.searchAppBarVisibilityState.value = true
                }
            )
        },
        content = {
            when (uiState) {
                is CurrencySelectionViewModel.UiState.Loaded -> {
                    CurrencySelectionContent(
                        currencyList = uiState.currencyList,
                        selectedCurrency = selectedCurrency,
                        onCurrencySelect = {
                            viewModel.selectedCurrency.value = it

                            navigateBack(it)
                        },
                    )
                }
                is CurrencySelectionViewModel.UiState.Loading -> {
                    CurrencySelectionLoadingContent()
                }
                is CurrencySelectionViewModel.UiState.Error -> {
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        scaffoldState.snackbarHostState.showSnackbar(uiState.msg)
                    }
                }
            }
        }
    )
}

@Composable
fun rememberCurrencySelectionScreenState(
    showSearch: Boolean = false,
    searchText: String = "",
) = rememberSaveable(saver = CurrencySelectionScreenState.Saver) {
    CurrencySelectionScreenState(
        showSearch = showSearch,
        searchText = searchText,
    )
}

class CurrencySelectionScreenState(
    showSearch: Boolean,
    searchText: String,
) {
    private var _showSearch by mutableStateOf(showSearch)
    private var _searchText by mutableStateOf(searchText)

    var showSearch: Boolean
        get() = _showSearch
        internal set(value) { _showSearch = value }

    var searchText: String
        get() = _searchText
        internal set(value) { _searchText = value }

    fun onCloseSearch() {
        showSearch = false
    }

    fun onOpenSearch() {
        showSearch = true
    }

    fun onSearch(query: String) {
        searchText = query
    }

    companion object {
        val Saver = listSaver<CurrencySelectionScreenState, Any>(
            save = {
                listOf(it.showSearch, it.searchText)
            },
            restore = {
                CurrencySelectionScreenState(
                    showSearch = it[0] as Boolean,
                    searchText = it[1] as String,
                )
            }
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun CurrencySelectionContent(
    currencyList: List<Currency>,
    selectedCurrency: String?,
    onCurrencySelect: (String) -> Unit,
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
                onCurrencySelect = onCurrencySelect
            )
        }
    }
}

@Composable
fun CurrencySelectionLoadingContent() {
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
    onCurrencySelect: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.surface,
        shape = RectangleShape,
        elevation = 1.dp,
        onClick = {
            onCurrencySelect(currencyIsoCode)
        }
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
                    tint = MaterialTheme.colors.primary,
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun CurrencySelectionAppBar(
    showSearchAppBar: Boolean,
    searchText: String,
    onBackClick: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onCloseSearchClick: () -> Unit,
    onOpenSearchClick: () -> Unit,
) {
    if (showSearchAppBar) {
        SearchCurrencySelectionAppBar(
            text = searchText,
            onTextChange = onSearchTextChange,
            onBackClick = onCloseSearchClick,
            onSearchClick = onSearchClick,
        )
    } else {
        DefaultCurrencySelectionAppBar(
            onBackClick = onBackClick,
            onSearchClick = onOpenSearchClick,
        )
    }
}

@Composable
fun DefaultCurrencySelectionAppBar(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = "Select currency")
        },
        actions = {
            IconButton(onClick = onSearchClick) {
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
        elevation = AppBarDefaults.TopAppBarElevation,
    )
}

@ExperimentalComposeUiApi
@Composable
fun SearchCurrencySelectionAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: (String) -> Unit,
) {
    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(Unit) {
        searchFocusRequester.requestFocus()
        onDispose { }
    }

    BackHandler(enabled = true) {
        onBackClick()
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
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.disabled),
                    text = stringResource(id = R.string.search_placeholder),
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
                        onBackClick()
                        onTextChange("")
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
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onTextChange("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = Icons.Filled.Clear.name,
                            tint = MaterialTheme.colors.onSurface,
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick(text)
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

@ExperimentalMaterialApi
@Preview
@Composable
private fun CurrencyItemPreview() {
    DucketAndroidTheme {
        CurrencyItem(
            currencyIsoCode = "Preview",
            currencyName = "Preview",
            selected = true,
            onCurrencySelect = {}
        )
    }
}
