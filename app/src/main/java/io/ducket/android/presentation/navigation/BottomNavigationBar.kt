package io.ducket.android.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Segment
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.utils.startDestination
import io.ducket.android.R
import io.ducket.android.presentation.screens.NavGraphs
import io.ducket.android.presentation.ui.theme.SpaceMedium
import io.ducket.android.presentation.ui.theme.SpaceSmall


enum class BottomBarDestination(
    val navGraph: NavGraphSpec,
    val icon: ImageVector,
    @StringRes val labelResId: Int
) {
    Home(NavGraphs.home, Icons.Outlined.Home, R.string.home_tab_title),
    Stats(NavGraphs.stats, Icons.Outlined.Analytics, R.string.stats_tab_title),
    Records(NavGraphs.records, Icons.Outlined.StickyNote2, R.string.records_tab_title),
    Menu(NavGraphs.menu, Icons.Outlined.Segment, R.string.menu_tab_title);

    companion object {
        val routes = values().map { it.navGraph.route }
        val startRoutes = values().map { it.navGraph.startRoute.route }
    }
}

@ExperimentalAnimationApi
@Composable
fun BottomNavigationBar(
    navHostController: NavHostController,
    onTabClick: (TabGraph) -> Unit,
) {
    val tabs = BottomBarDestination.values()
    val bottomBarRoutesWithoutFirst = BottomBarDestination.startRoutes.drop(n = 1)
    val isCurrentDestinationTop = navHostController.backQueue.last().destination.route in bottomBarRoutesWithoutFirst

    // TODO handle back press if current top destination is not start of current graph
    BackHandler(enabled = isCurrentDestinationTop) {
        navHostController.navigateTab(BottomBarDestination.values().first().navGraph)
    }

    BottomBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
    ) {
        tabs.forEach { tab ->
            // val selected = navHostController.currentDestination?.navGraph?.route == tab.navGraph.route
            val selected = navHostController.rootTabNavGraph.route == tab.navGraph.route

            BottomNavigationBarItem(
                selected = selected,
                label = stringResource(id = tab.labelResId),
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
                selectedContentColor = MaterialTheme.colors.secondary,
                contentIcon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.icon.name,
                    )
                },
                onClick = {
                    navHostController.navigateTab(tab.navGraph)
                }
            )
        }
    }
}

@Composable
fun BottomNavigationBarFabDem(
    navController: NavController,
) {
    val tabs = BottomBarDestination.values()
    val bottomBarRoutesWithoutFirst = BottomBarDestination.startRoutes.drop(n = 1)
    val isCurrentDestinationTop = navController.backQueue.last().destination.route in bottomBarRoutesWithoutFirst

    // TODO handle back press if current top destination is not start of current graph
    BackHandler(enabled = isCurrentDestinationTop) {
        navController.navigateTab(BottomBarDestination.values().first().navGraph)
    }

    BottomAppBar(
        modifier = Modifier
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)),
        cutoutShape = CircleShape,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    ) {
        BottomNavigation(
            modifier = Modifier.height(100.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp
        ) {
            tabs.forEachIndexed { i, tab ->
                if (i == tabs.count() / 2) {
                    Spacer(Modifier.weight(0.5f))
                }

                val selected = navController.rootTabNavGraph.route == tab.navGraph.route

                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.icon.name,
                        )
                    },
                    label = {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.secondary,
                        )
                    },
                    selected = selected,
                    alwaysShowLabel = false,
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled),
                    onClick = {
                        navController.navigateTab(tab.navGraph)
                    }
                )
            }
        }
    }
}



@Composable
fun BottomNavigationBarItem(
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

fun NavController.navigateTab(tabGraph: NavGraphSpec) {
    val isTabRouteOnBackStack = runCatching { getBackStackEntry(tabGraph.route) }.isSuccess
    val currentRoute = backQueue.last().destination.route

    if (currentRoute == tabGraph.startDestination.route) {
        // ignore navigation if current destination is start tab destination
        return
    }

    if (isTabRouteOnBackStack) {
        // when we click again on a bottom bar item and it was already selected
        // we want to pop the back stack until the start destination of this bottom bar item (if exist)

        val isTabStartRouteOnBackStack = runCatching { getBackStackEntry(tabGraph.startRoute.route) }.isSuccess

        if (isTabStartRouteOnBackStack) {
            popBackStack(tabGraph.route, false)
        } else {
            // the situation when the tab doesn't have its start destination on back stack.
            // it happens when the tab nested destination was triggered from another tab.
            navigate(tabGraph.startRoute.route) {
                popUpTo(tabGraph.route)
            }
        }
    }

    navigate(tabGraph.route) {
        // Pop up to tabs nav graph
        // to avoid building up a large stack of destinations
        // on the back stack as users select tabs
        popUpTo(NavGraphs.tabs.route) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // re-selecting the same item
        launchSingleTop = true
        // Restore state when re-selecting a previously selected item
        restoreState = true
    }
}

private val NavDestination.navGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

private val NavController.rootTabNavGraph: NavGraph
    get() = backQueue.map { it.destination }.first {
        it is NavGraph && it.route in BottomBarDestination.routes
    } as NavGraph