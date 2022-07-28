package io.ducket.android.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.ducket.android.presentation.navigation.NavArgKey.ARG_BUDGET_ID
import io.ducket.android.presentation.navigation.NavArgKey.ARG_CURRENCY_ISO_CODE
import io.ducket.android.presentation.navigation.NavArgKey.ARG_IS_TRANSFER
import io.ducket.android.presentation.navigation.NavArgKey.ARG_RECORD_ID
import io.ducket.android.presentation.navigation.NavArgKey.ARG_USER_ID


sealed class Destination(val route: String) {

    open fun route(vararg args: Pair<String, Any?>): String {
        var routeWithArgs = route

        args.forEach { (argKey, argValue) ->
            if (argValue != null) {
                NavType.inferFromValueType(argValue) // assert argument key value type
                routeWithArgs = routeWithArgs.replace("{${argKey}}", argValue.toString())
            }
        }

        return routeWithArgs
    }

    open fun navArgs(): List<NamedNavArgument> = emptyList()

    object Splash : Destination(
        route = "${Graph.Host.route}/splash"
    )

    object Welcome : Destination(
        route = "${Graph.Auth.route}/welcome"
    )

    object SignIn : Destination(
        route = "${Graph.Auth.route}/sign_in"
    )

    object SignUp : Destination(
        route = "${Graph.Auth.route}/sign_up"
    )

    object Home : Destination(
        route = "${TabGraph.Home.route}/home"
    )

    object Records : Destination(
        route = "${TabGraph.Records.route}/records"
    )

    object Budgets : Destination(
        route = "${TabGraph.Menu.route}/budgets"
    )

    object CurrencySelection : Destination(
        route = "currencySelection?currencyIsoCode={${ARG_CURRENCY_ISO_CODE}}"
    ) {
        override fun navArgs(): List<NamedNavArgument> = listOf(
            navArgument(ARG_CURRENCY_ISO_CODE) {
                type = NavType.StringType
                nullable = true
            },
        )
    }

    object Stats : Destination(
        route = "${TabGraph.Stats.route}/stats"
    )

    object Menu : Destination(
        route = "${TabGraph.Menu.route}/menu"
    )

    object Accounts : Destination(
        route = "${TabGraph.Menu.route}/accounts"
    )

    object Imports : Destination(
        route = "${TabGraph.Menu.route}/imports"
    )

    object UserDetails : Destination(
        route = "${TabGraph.Records.route}/user/{${ARG_USER_ID}}"
    ) {
        override fun navArgs(): List<NamedNavArgument> = listOf(
            navArgument(ARG_USER_ID) { type = NavType.IntType },
        )
    }

    object RecordDetails : Destination(
        route = "${TabGraph.Records.route}/record/{${ARG_RECORD_ID}}?isTransfer={${ARG_IS_TRANSFER}}"
    ) {

        override fun navArgs(): List<NamedNavArgument> = listOf(
            navArgument(ARG_RECORD_ID) { type = NavType.IntType },
            navArgument(ARG_IS_TRANSFER) { type = NavType.BoolType }
        )
    }

    object BudgetDetails : Destination(
        route = "${TabGraph.Menu.route}/budget/{${ARG_BUDGET_ID}}"
    ) {

        override fun navArgs(): List<NamedNavArgument> = listOf(
            navArgument(ARG_BUDGET_ID) { type = NavType.IntType },
        )
    }
}