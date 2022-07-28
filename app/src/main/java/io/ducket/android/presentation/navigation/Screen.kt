package io.ducket.android.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.ducket.android.R


//// TODO replace with TabGraph : Graph
//sealed interface NavTab {
//    val desc: String
//    val activeIcon: ImageVector
//    val idleIcon: ImageVector
//}

sealed class TabGraph(route: String, @StringRes val labelResId: Int, val icon: ImageVector): Graph(route) {
    object Home : TabGraph(route = "home_graph", labelResId = R.string.home_tab_title, icon = Icons.Outlined.Home)
    object Records : TabGraph(route = "records_graph", labelResId = R.string.records_tab_title, icon = Icons.Outlined.StickyNote2)
    object Stats : TabGraph(route = "stats_graph", labelResId = R.string.stats_tab_title, icon = Icons.Outlined.Analytics)
    object Menu : TabGraph(route = "menu_graph", labelResId = R.string.menu_tab_title, icon = Icons.Outlined.Segment)
}

sealed class Graph(val route: String) {

    fun belongsTo(screen: String?): Boolean {
        return screen?.startsWith(this.route) == true
    }

    object Host : Graph(route = "host_graph")

    object Auth : Graph(route = "auth_graph")

    object Tabs : Graph(route = "tabs_graph")

}