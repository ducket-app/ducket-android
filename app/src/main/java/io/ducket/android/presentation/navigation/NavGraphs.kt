package io.ducket.android.presentation.navigation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class AuthNavGraph(val start: Boolean = false)

@RootNavGraph
@NavGraph
annotation class TabsNavGraph(val start: Boolean = false)

@TabsNavGraph(start = true)
@NavGraph
annotation class HomeNavGraph(val start: Boolean = false)

@TabsNavGraph
@NavGraph
annotation class StatsNavGraph(val start: Boolean = false)

@TabsNavGraph
@NavGraph
annotation class RecordsNavGraph(val start: Boolean = false)

@TabsNavGraph
@NavGraph
annotation class MenuNavGraph(val start: Boolean = false)

