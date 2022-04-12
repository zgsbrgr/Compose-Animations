package com.zgsbrgr.compose.anim

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.navigation


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = "welcome") {
        composable(
            route = "welcome",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) {
            Welcome(navController)
        }
        composable(
            route = "main",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) {
            MainScreen(navController)
        }
        composable(
            route = "card",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) {
            CardMenu(navController)
        }
        composable(
            route = "list",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) {
            ListMenu(navController)
        }
        composable(
            route = "floatingmenu/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) { backStackEntry->
            FloatingCard(navController, itemId = backStackEntry.arguments?.getInt("itemId")!!)
        }

    }
}