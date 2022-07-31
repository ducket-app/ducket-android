package io.ducket.android.presentation.navigation

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.*
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.NestedNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.ducket.android.presentation.components.AppSnackbar
import io.ducket.android.presentation.screens.NavGraphs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

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
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    appSnackbarManager: AppSnackbarManager = AppSnackbarManager(LocalContext.current.resources),
) = remember(scaffoldState, navHostController, coroutineScope, appSnackbarManager) {
    AppState(
        scaffoldState = scaffoldState,
        navHostController = navHostController,
        snackbarManager = appSnackbarManager,
        coroutineScope = coroutineScope,
    )
}

class AppState(
    val scaffoldState: ScaffoldState,
    val navHostController: NavHostController,
    val snackbarManager: AppSnackbarManager,
    coroutineScope: CoroutineScope
) {
    val bottomBarDestinations = BottomBarDestination.values()
    val bottomBarStartRoutes = bottomBarDestinations.map { it.navGraph.startRoute.route }

    val showBottomNavBar: Boolean
        @Composable get() = navHostController.currentBackStackEntryAsState().value?.destination?.route in bottomBarStartRoutes

    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    // Display the snackbar on the screen. `showSnackbar` is a function
                    // that suspends until the snackbar disappears from the screen
                    scaffoldState.snackbarHostState.showSnackbar(message.text, "OK", )
                    // Once the snackbar is gone or dismissed, notify the SnackbarManager
                    snackbarManager.setMessageShown(message.id)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
object FullScreenDialogTransitions : DestinationStyle.Animated {

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return null
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return null
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return slideOutVerticalTransition()
    }

    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return slideInVerticalTransition()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
fun HostNavScreen() {
    val appState = rememberAppState()
    val bottomSheetNavigator = rememberBottomSheetNavigator()

    val engine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
            enterTransition = { fadeInTransition() },
            exitTransition = { fadeOutTransition() },
            popEnterTransition = { fadeInTransition() },
            popExitTransition = { fadeOutTransition() },
        ),
        defaultAnimationsForNestedNavGraph = mapOf(
            NavGraphs.auth to NestedNavGraphDefaultAnimations(
                enterTransition = { slideInHorizontalTransition() },
                popExitTransition = { slideOutHorizontalTransition() },
                exitTransition = null,
                popEnterTransition = null,
            )
        )
    )

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(16.dp),
    ) {
        Scaffold(
            scaffoldState = appState.scaffoldState,
            snackbarHost = {
                AppSnackbar(
                    snackbarHostState = appState.scaffoldState.snackbarHostState,
                    onDismiss = {
                        appState.scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                )
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = appState.showBottomNavBar,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    content = {
                        BottomNavigationBarFabDem(appState.navHostController)
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            floatingActionButton = {
                AnimatedVisibility(
                    visible = appState.showBottomNavBar,
                    enter = scaleIn(initialScale = 0.2f),
                    exit = scaleOut(),
                    content = {
                        FloatingActionButton(
                            shape = CircleShape,
                            onClick = {

                            },
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSecondary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                            )
                        }
                    }
                )
            }
        ) {
            DestinationsNavHost(
                // modifier = Modifier.padding(it),
                navGraph = NavGraphs.root,
                navController = appState.navHostController,
                engine = engine,
                dependenciesContainerBuilder = {
                    dependency(appState.snackbarManager)
                }
            )
        }
    }
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.fadeOutTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(150))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.fadeInTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(150))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.slideOutVerticalTransition(): ExitTransition {
    return slideOutVertically(
        targetOffsetY = { 300 },
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearEasing
        )
    ) + fadeOut(animationSpec = tween(150))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.slideOutHorizontalTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { 300 },
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearEasing
        )
    ) + fadeOut(animationSpec = tween(150))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.slideInVerticalTransition(): EnterTransition {
    return slideInVertically(
        initialOffsetY = { 300 },
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearEasing
        )
    ) + fadeIn(animationSpec = tween(150))
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.slideInHorizontalTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { 300 },
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearOutSlowInEasing
        )
    ) + fadeIn(animationSpec = tween(150))
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}
