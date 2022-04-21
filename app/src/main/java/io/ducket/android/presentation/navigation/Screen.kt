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

    object Main : Graph(route = "main_graph")

//    object Home : Graph("home_graph"), NavTab {
//        override val desc = "Home"
//        override val activeIcon = Icons.Filled.Dashboard
//        override val idleIcon = Icons.Outlined.Dashboard
//    }
//
//    object Records : Graph("records_graph"), NavTab {
//        override val desc = "Records"
//        override val activeIcon = Icons.Filled.Article
//        override val idleIcon = Icons.Outlined.Article
//    }
//
//    object Budgets : Graph("budgets_graph"), NavTab {
//        override val desc = "Budgets"
//        override val activeIcon = Icons.Filled.Calculate
//        override val idleIcon = Icons.Outlined.Calculate
//    }
//
//    object Stats : Graph("stats_graph"), NavTab {
//        override val desc = "Budgets"
//        override val activeIcon = Icons.Filled.Poll
//        override val idleIcon = Icons.Outlined.Poll
//    }
//
//    object Menu : Graph("menu_graph"), NavTab {
//        override val desc = "Budgets"
//        override val activeIcon = Icons.Filled.PermContactCalendar
//        override val idleIcon = Icons.Outlined.PermContactCalendar
//    }
}

//sealed class Screen(
//    private val name: String,
//    private val graph: Graph,
//    open val args: List<NamedNavArgument> = emptyList(),
//) {
//    open val route: String get() = "${graph.route}/$name"
//
//    object Launch : Screen(name = "launch_screen", graph = Graph.Host)
//    object Welcome : Screen(name = "welcome_screen", graph = Graph.Auth)
//    object SignIn : Screen(name = "sign_in_screen", graph = Graph.Auth)
//    object SignUp : Screen(name = "sign_up_screen", graph = Graph.Auth)
//    object Home : Screen(name = "home_screen", graph = Graph.Home)
//    object Records : Screen(name = "records_screen", graph = Graph.Records)
//    object Budgets : Screen(name = "budgets_screen", graph = Graph.Budgets)
//    object Stats : Screen(name = "stats_screen", graph = Graph.Stats)
//    object Menu : Screen(name = "menu_screen", graph = Graph.Menu)
//    object Accounts : Screen(name = "accounts_screen", graph = Graph.Menu)
//    object UserDetails : Screen(name = "user_details_screen", graph = Graph.Menu)
//    object Import : Screen(name = "import_screen", graph = Graph.Menu)
//
//    object RecordDetails : Screen(
//        name = "record_details_screen",
//        graph = Graph.Records,
//        args = listOf(
//            navArgument("id") { type = NavType.LongType }
//        ),
//    ) {
//        override val route get() = "${super.route}/{id}"
//
//        fun resolveRoute(id: Long) = "${super.route}/$id"
//    }
//
//    object BudgetDetails : Screen(
//        name = "budget_details_screen",
//        graph = Graph.Budgets,
//        args = listOf(
//            navArgument("id") { type = NavType.LongType }
//        ),
//    ) {
//        override val route get() = "${super.route}/{id}"
//
//        fun resolveRoute(id: Long) = "${super.route}/$id"
//    }
//}

//sealed class Tab(val route: String, val label: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
//    object HomeTab : Tab("home_tab", "Home", Icons.Filled.Dashboard, Icons.Outlined.Dashboard)
//    object RecordsTab : Tab("records_tab", "Records", Icons.Filled.Article, Icons.Outlined.Article)
//    object BudgetsTab : Tab("budgets_tab", "Budgets", Icons.Filled.Calculate, Icons.Outlined.Calculate)
//    object StatsTab : Tab("stats_tab", "Stats", Icons.Filled.Poll, Icons.Outlined.Poll)
//    object MenuTab : Tab("menu_tab", "Menu", Icons.Filled.PermContactCalendar, Icons.Outlined.PermContactCalendar)
//
//    fun belongsTo(nestedRoute: String?): Boolean {
//        return nestedRoute?.startsWith(this.route) == true
//    }
//}
//
//sealed class NestedScreen(val route: String) {
//
//}
//
//sealed class Screen(val route: String) {
//    object LaunchScreen : Screen("launch_screen")
//    object WelcomeScreen : Screen("welcome_screen")
//    object SignInScreen : Screen("sign_in_screen")
//    object SignUpScreen : Screen("sign_up_screen")
//
//    object HomeScreen : Screen("home_screen")
//    object RecordListScreen : Screen("record_list_screen")
//    object RecordDetailsScreen : Screen("record_details_screen")
//    object RecordCreateScreen : Screen("record_create_screen")
//    object BudgetListScreen : Screen("budget_list_screen")
//    object StatsScreen : Screen("stats_screen")
//    object MenuScreen : Screen("menu_screen")
//    object AccountListScreen : Screen("account_list_screen")
//}