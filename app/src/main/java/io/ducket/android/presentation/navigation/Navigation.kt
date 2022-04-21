package io.ducket.android.presentation.navigation

import android.content.res.Resources
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import io.ducket.android.presentation.components.AppSnackbar
import io.ducket.android.presentation.navigation.NavArgKey.ARG_CURRENCY_ISO_CODE
import io.ducket.android.presentation.navigation.NavArgKey.ARG_RECORD_ID
import io.ducket.android.presentation.screens.accounts.AccountsScreen
import io.ducket.android.presentation.screens.budgets.BudgetsScreen
import io.ducket.android.presentation.screens.home.HomeScreen
import io.ducket.android.presentation.screens.menu.MenuScreen
import io.ducket.android.presentation.screens.records.RecordDetailsScreen
import io.ducket.android.presentation.screens.records.RecordsScreen
import io.ducket.android.presentation.screens.auth.sign_in.SignInScreen
import io.ducket.android.presentation.screens.auth.sign_up.SignUpScreen
import io.ducket.android.presentation.screens.auth.sign_up.SignUpViewModel
import io.ducket.android.presentation.screens.currencies.CurrencySelectionScreen
import io.ducket.android.presentation.screens.splash.LaunchScreen
import io.ducket.android.presentation.screens.stats.StatsScreen
import io.ducket.android.presentation.screens.welcome.WelcomeScreen
import io.ducket.android.presentation.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*


//@ExperimentalComposeUiApi
//@ExperimentalAnimationApi
//@Composable
//fun Navigation(navHostController: NavHostController) {
//    AnimatedNavHost(
//        navController = navHostController,
//        startDestination = Screen.LaunchScreen.route,
//    ) {
//        composable(Screen.LaunchScreen.route) {
//            LaunchScreen(navController = navHostController)
//        }
//        composable(
//            route = Screen.WelcomeScreen.route,
//            exitTransition = { _, _ ->
//                slideOutHorizontally(
//                    targetOffsetX = { -300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeOut(animationSpec = tween(300))
//            },
//            popEnterTransition = { _, _ ->
//                slideInHorizontally(
//                    initialOffsetX = { -300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeIn(animationSpec = tween(300))
//            }
//        ) {
//            WelcomeScreen(navController = navHostController)
//        }
//        composable(
//            route = Screen.SignInScreen.route,
//            enterTransition = { _, _ ->
//                slideInHorizontally(
//                    initialOffsetX = { 300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeIn(animationSpec = tween(300))
//            },
//            popExitTransition = { _, _ ->
//                slideOutHorizontally(
//                    targetOffsetX = { 300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeOut(animationSpec = tween(300))
//            },
//            popEnterTransition = { _, _ ->
//                slideInHorizontally(
//                    initialOffsetX = { -300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeIn(animationSpec = tween(300))
//            }
//        ) {
//            SignInScreen(navController = navHostController)
//        }
//        composable(
//            route = Screen.SignUpScreen.route,
//            enterTransition = { _, _ ->
//                slideInHorizontally(
//                    initialOffsetX = { 300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeIn(animationSpec = tween(300))
//            },
//            popExitTransition = { _, _ ->
//                slideOutHorizontally(
//                    targetOffsetX = { 300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeOut(animationSpec = tween(300))
//            }
//        ) {
//            SignUpScreen(navController = navHostController)
//        }
//    }
//}

data class Message(val id: Long, val text: String)

class AppSnackbarManager(private val resources: Resources) {
    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    fun showMessage(@StringRes messageTextId: Int) {
        showMessage(resources.getText(messageTextId).toString())
    }

    fun showMessage(messageText: String) {
        _messages.update { currentMessages ->
            currentMessages + Message(
                id = UUID.randomUUID().mostSignificantBits,
                text = messageText
            )
        }
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navHostController: NavHostController = rememberAnimatedNavController(),
    appSnackbarManager: AppSnackbarManager = AppSnackbarManager(LocalContext.current.resources),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navHostController, appSnackbarManager, coroutineScope) {
    AppState(scaffoldState, navHostController, appSnackbarManager, coroutineScope)
}

class AppState(
    val scaffoldState: ScaffoldState,
    val navHostController: NavHostController,
    val appSnackbarManager: AppSnackbarManager,
    coroutineScope: CoroutineScope
) {
    val bottomAppBarTabs = listOf(TabGraph.Home, TabGraph.Records, TabGraph.Stats, TabGraph.Menu)
    val bottomAppBarTabsRoutes = bottomAppBarTabs.map { it.route }

    val previousBackStackEmpty: Boolean
        get() = navHostController.previousBackStackEntry?.destination == null

    val currentRoute: String?
        get() = navHostController.currentDestination?.route

    val currentNavGraph: String?
        get() = navHostController.currentDestination?.hostNavGraph?.route

    val showBottomAppBar: Boolean
        @Composable get() = navHostController.currentBackStackEntryAsState().value?.destination?.hostNavGraph?.route in bottomAppBarTabsRoutes

    init {
        coroutineScope.launch {
            appSnackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    // Display the snackbar on the screen. `showSnackbar` is a function
                    // that suspends until the snackbar disappears from the screen
                    scaffoldState.snackbarHostState.showSnackbar(message.text, "OK", )
                    // Once the snackbar is gone or dismissed, notify the SnackbarManager
                    appSnackbarManager.setMessageShown(message.id)
                }
            }
        }
    }

    // Navigation logic

    fun goBack() {
        navHostController.popBackStack()
    }

    fun goFromCurrencySelectionScreen(currencyCode: String) {
        navHostController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(ARG_CURRENCY_ISO_CODE, currencyCode)

        navHostController.popBackStack()
    }

    fun goToCurrencySelectionScreen(currencyCode: String) {
        navHostController.navigate(
            Destination.CurrencySelection.route(
                ARG_CURRENCY_ISO_CODE to currencyCode
            )
        )
    }

    fun goToSignInScreen() {
        navHostController.navigate(Destination.SignIn.route)
    }

    fun goToSignUpScreen() {
        navHostController.navigate(Destination.SignUp.route)
    }

    fun goToHomeScreen() {
        navHostController.navigate(Graph.Main.route) {
            popUpTo(Graph.Host.route)
        }
    }

    fun goToTab(tabGraph: TabGraph) {
        val currentRoute = navHostController.currentBackStackEntry?.destination?.route
        val navGraph = navHostController.currentBackStackEntry?.destination?.hostNavGraph
        val navGraphStartDestination = navGraph?.startDestinationRoute
        val navGraphRoute = navGraph?.route

        navHostController.backQueue.forEach { println(it.destination.route) }

        val jumpToStart = navGraphRoute == tabGraph.route && currentRoute != navGraphStartDestination
        val changeTab = navGraphRoute != tabGraph.route

        if (jumpToStart || changeTab) {

            navHostController.navigate(tabGraph.route) {
                // Pop up to the start destination of the bottom navigation graph
                // to avoid building up a large stack of destinations
                // on the back stack as users select tabs
                popUpTo(Graph.Main.route) {
                    saveState = changeTab
                }
                // Avoid multiple copies of the same destination when
                // re-selecting the same item
                launchSingleTop = true
                // Restore state when re-selecting a previously selected item
                restoreState = changeTab
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun RootScreen() {
    val appState = rememberAppState()
    val bottomNavigationState = remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = appState.scaffoldState,
        snackbarHost = {
            AppSnackbar(
                snackbarHostState = appState.scaffoldState.snackbarHostState,
                onDismiss = { appState.scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = appState.showBottomAppBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                content = {
                    BackHandler(enabled = appState.bottomAppBarTabsRoutes.contains(appState.currentNavGraph)
                            && !appState.bottomAppBarTabs.first().belongsTo(appState.currentRoute)
                            && appState.previousBackStackEmpty
                    ) {
                        appState.goToTab(appState.bottomAppBarTabs.first())
                    }

                    BottomNavigationBar(
                        tabs = appState.bottomAppBarTabs,
                        currentRoute = appState.currentRoute!!,
                        onTabClick = appState::goToTab
                    )
                }
            )
        }
    ) { innerPaddingValues ->
        AnimatedNavHost(
            modifier = Modifier.padding(innerPaddingValues),
            navController = appState.navHostController,
            route = Graph.Host.route,
            startDestination = Destination.Launch.route,
            enterTransition = { fadeIn(animationSpec = tween(200)) },
            exitTransition = { fadeOut(animationSpec = tween(200)) },
            popEnterTransition = { fadeIn(animationSpec = tween(200)) },
            popExitTransition = { fadeOut(animationSpec = tween(200)) },
        ) {
            composable(
                route = Destination.Launch.route,
            ) {
                bottomNavigationState.value = false
                LaunchScreen(navController = appState.navHostController)
            }

            navigation(
                route = Graph.Auth.route,
                startDestination = Destination.Welcome.route
            ) {
                composable(
                    route = Destination.Welcome.route
                ) {
                    bottomNavigationState.value = false
                    WelcomeScreen(
                        onGoToSignInClick = {
                            appState.navHostController.navigate(Destination.SignIn.route)
                        },
                        onGoToSignUpClick = {
                            appState.navHostController.navigate(Destination.SignUp.route)
                        }
                    )
                }

                composable(
                    route = Destination.CurrencySelection.route,
                    arguments = Destination.CurrencySelection.navArgs(),
                    enterTransition = { slideInVerticalTransition() },
                    popExitTransition = { slideOutVerticalTransition() },
                ) { backStackEntry ->
                    val signUpViewModel: SignUpViewModel? = appState.navHostController.previousBackStackEntry?.let { hiltViewModel(it) }

                    CurrencySelectionScreen(
                        viewModel = hiltViewModel(),
                        scaffoldState = appState.scaffoldState,
                        navigateBack = {
                            // appState.navHostController.previousBackStackEntry?.savedStateHandle?.set(ARG_CURRENCY_ISO_CODE, it)
                            signUpViewModel?.onCurrencySelect(it)
                            appState.navHostController.popBackStack()
                        }
                    )
                }

                composable(
                    route = Destination.SignIn.route,
                    enterTransition = { slideInHorizontalTransition() },
                    popExitTransition = { slideOutHorizontalTransition() },
                ) {
                    SignInScreen(
                        viewModel = hiltViewModel(),
                        scaffoldState = appState.scaffoldState,
                        appSnackbarManager = appState.appSnackbarManager,
                        navigateBack = {
                            appState.navHostController.popBackStack()
                        },
                        navigateToSignUpScreen = {
                            appState.navHostController.navigate(Destination.SignUp.route) {
                                popUpTo(Destination.Welcome.route)
                            }
                        },
                        navigateToHomeScreen = {
                            appState.navHostController.navigate(Graph.Main.route) {
                                popUpTo(Graph.Host.route)
                            }
                        }
                    )
                }

                composable(
                    route = Destination.SignUp.route,
                    enterTransition = { slideInHorizontalTransition() },
                    popExitTransition = { slideOutHorizontalTransition() },
                    popEnterTransition = { fadeIn(animationSpec = tween(200)) },
                ) { backStackEntry ->
                    SignUpScreen(
                        viewModel = hiltViewModel(backStackEntry),
                        scaffoldState = appState.scaffoldState,
                        appSnackbarManager = appState.appSnackbarManager,
                        navigateBack = {
                            appState.navHostController.popBackStack()
                        },
                        navigateToHomeScreen = {
                            appState.navHostController.navigate(Graph.Main.route) {
                                popUpTo(Graph.Host.route)
                            }
                        },
                        navigateToCurrencySelectionScreen = {
                            appState.navHostController.navigate(
                                Destination.CurrencySelection.route(
                                    ARG_CURRENCY_ISO_CODE to it
                                )
                            )
                        },
                        navigateToSignInScreen = {
                            appState.navHostController.navigate(Destination.SignIn.route) {
                                popUpTo(Destination.Welcome.route)
                            }
                        }
                    )
                }
            }

            navigation(
                route = Graph.Main.route,
                startDestination = TabGraph.Home.route,
            ) {
                navigation(
                    route = TabGraph.Home.route,
                    startDestination = Destination.Home.route
                ) {
                    composable(
                        route = Destination.Home.route,
                    ) {
                        bottomNavigationState.value = true
                        HomeScreen(
                            scaffoldState = scaffoldState,
                        )
                    }
                }

                navigation(
                    route = TabGraph.Records.route,
                    startDestination = Destination.Records.route
                ) {
                    composable(
                        route = Destination.Records.route,
                        arguments = Destination.Records.navArgs(),
                    ) {
                        bottomNavigationState.value = true
                        RecordsScreen(
                            navController = appState.navHostController,
                            onRecordClick = {
                                appState.navHostController.navigate(
                                    Destination.RecordDetails.route(ARG_RECORD_ID to 1)
                                )
                            }
                        )
                    }

                    composable(
                        route = Destination.RecordDetails.route
                    ) {
                        bottomNavigationState.value = true
                        RecordDetailsScreen(
                            navController = appState.navHostController,
                        )
                    }
                }

                navigation(
                    route = TabGraph.Stats.route,
                    startDestination = Destination.Stats.route,
                ) {
                    composable(
                        route = Destination.Stats.route
                    ) {
                        bottomNavigationState.value = true
                        StatsScreen(navController = appState.navHostController)
                    }
                }

                navigation(
                    route = TabGraph.Menu.route,
                    startDestination = Destination.Menu.route
                ) {
                    composable(
                        route = Destination.Menu.route
                    ) {
                        bottomNavigationState.value = true
                        MenuScreen()
                    }

                    composable(
                        route = Destination.Accounts.route
                    ) {
                        bottomNavigationState.value = true
                        AccountsScreen(navController = appState.navHostController)
                    }

                    composable(
                        route = Destination.Budgets.route
                    ) {
                        bottomNavigationState.value = true
                        BudgetsScreen(navController = appState.navHostController)
                    }
                }
            }
        }

//        DefaultAppSnackbar(
//            snackbarHostState = appState.scaffoldState.snackbarHostState,
//            onDismiss = { appState.scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() }
//        )
    }
}

@ExperimentalAnimationApi
@Composable
fun BottomNavigationBar(
    tabs: List<TabGraph>,
    currentRoute: String,
//    navController: NavHostController,
//    visible: Boolean,
    onTabClick: (TabGraph) -> Unit,
) {
//    val items = listOf(
//        TabGraph.Home,
//        TabGraph.Records,
//        TabGraph.Stats,
//        TabGraph.Menu,
//    )

//    val firstTab = items.first()
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//    val currentNavGraph = navBackStackEntry?.destination?.hostNavGraph?.route
//    val isPrevBackStackEmpty = navController.previousBackStackEntry?.destination == null
//
//    val jumpToFirstTab = items.map { it.route }.contains(currentNavGraph)
//            && !firstTab.belongsTo(currentRoute)
//            && isPrevBackStackEmpty
    // && items.map { it.belongsTo(currentRoute) }.any()

//    BackHandler(enabled = jumpToFirstTab) {
//        navController.navigateTab(firstTab)
//    }

//    AnimatedVisibility(
//        visible = visible,
//        enter = slideInVertically(initialOffsetY = { it }),
//        exit = slideOutVertically(targetOffsetY = { it }),
//        content = {
//
//        }
//    )

    BottomBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
    ) {
        tabs.forEach { tab ->
            BubbleNavigationItem(
                selected = tab.belongsTo(currentRoute),
                label = stringResource(id = tab.labelResId),
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
                selectedContentColor = MaterialTheme.colors.primary,
                contentIcon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.icon.name,
                    )
                },
                onClick = {
                    onTabClick(tab)
                }
            )
//                    BottomNavigationItem(
//                        icon = {
//                            Icon(
//                                imageVector = if (selected) tab.activeIcon else tab.idleIcon,
//                                contentDescription = tab.activeIcon.name,
//                            )
//                        },
//                        label = {
//                            Text(
//                                text = "•",
//                                style = MaterialTheme.typography.h5,
//                                color = MaterialTheme.colors.primary,
//                            )
//                        },
//                        alwaysShowLabel = false,
//                        selected = selected,
//                        selectedContentColor = MaterialTheme.colors.primary,
//                        unselectedContentColor = MaterialTheme.colors.subtitle,
//                        onClick = {
//                            onBottomTabClick(tab)
//                        }
//                    )
        }
    }
}

@Composable
fun RowScope.BubbleNavigationItem(
    label: String,
    selected: Boolean,
    unselectedContentColor: Color,
    selectedContentColor: Color,
    contentIcon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    // val ripple = rememberRipple(bounded = false, color = MaterialTheme.colors.primary)
    val backgroundColor = if (selected) selectedContentColor.copy(0.2f) else Color.Transparent
    val contentColor = if (selected) selectedContentColor else unselectedContentColor

    Box(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = true,
                role = Role.Tab,
                // interactionSource = remember { MutableInteractionSource() },
                // indication = ripple
            )
            .background(backgroundColor)
            .animateContentSize(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(SpaceSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomBarTransition(
                activeColor = contentColor,
                inactiveColor = unselectedContentColor,
                selected = selected,
            ) {
                Box {
                    contentIcon()
                }
            }

            AnimatedVisibility(
                modifier = Modifier.padding(start = SpaceSmall),
                visible = selected,
                enter = fadeIn(tween(durationMillis = 50))
                        + expandHorizontally(animationSpec = tween(durationMillis = 50)),
                exit = shrinkHorizontally(animationSpec = tween(durationMillis = 50))
                        + fadeOut(tween(durationMillis = 50)),
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.body1,
                    color = contentColor,
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = BottomNavigationDefaults.Elevation,
    content: @Composable RowScope.() -> Unit
) {
    Card(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
                .wrapContentHeight()
                //.height(56.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween,
            content = content
        )
    }
}

@Composable
private fun BottomBarTransition(
    activeColor: Color,
    inactiveColor: Color,
    selected: Boolean,
    content: @Composable (animationProgress: Float) -> Unit
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(
            durationMillis = 100,
            easing = FastOutSlowInEasing,
        )
    )

    val color = lerp(inactiveColor, activeColor, animationProgress)

    CompositionLocalProvider(
        LocalContentColor provides color.copy(alpha = 1f),
        LocalContentAlpha provides color.alpha,
    ) {
        content(animationProgress)
    }
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.appEnterTransition(
    origin: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val originNavGraph = origin.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we do not use animation
    if (originNavGraph.id != targetNavGraph.id) {
        return EnterTransition.None
    }
    // Otherwise we're in the same nav graph, we use slide
    return fadeIn(animationSpec = tween(300))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.slideOutVerticalTransition(): ExitTransition {
    return slideOutVertically(
        targetOffsetY = { 300 },
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    ) + fadeOut(animationSpec = tween(400))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.slideOutHorizontalTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { 300 },
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        )
    ) + fadeOut(animationSpec = tween(300))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.slideInVerticalTransition(): EnterTransition {
    return slideInVertically(
        initialOffsetY = { 300 },
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    ) + fadeIn(animationSpec = tween(400))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.slideInHorizontalTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { 300 },
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        )
    ) + fadeIn(animationSpec = tween(300))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.appExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return ExitTransition.None
    }
    // Otherwise we're in the same nav graph, we use slide
    return fadeOut(animationSpec = tween(300))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.appPopEnterTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(300))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.appPopExitTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(300))
}

fun NavController.navigateTab(tabGraph: Graph) {
    val currentRoute = this.currentBackStackEntry?.destination?.route
    val navGraph = this.currentBackStackEntry?.destination?.hostNavGraph
    val navGraphStartDestination = navGraph?.startDestinationRoute
    val navGraphRoute = navGraph?.route

    this.backQueue.forEach { println(it.destination.route) }

    val jumpToStart = navGraphRoute == tabGraph.route && currentRoute != navGraphStartDestination
    val changeTab = navGraphRoute != tabGraph.route

    if (jumpToStart || changeTab) {

        this.navigate(tabGraph.route) {
            // Pop up to the start destination of the bottom navigation graph
            // to avoid building up a large stack of destinations
            // on the back stack as users select tabs
            popUpTo(Graph.Main.route) {
                saveState = changeTab
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = changeTab
        }
    }
}