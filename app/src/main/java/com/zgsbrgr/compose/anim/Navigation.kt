package com.zgsbrgr.compose.anim


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


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
            Welcome(onNavigation = {
                navController.navigate(it)
            })
        }
        composable(
            route = "main",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) {
            MainScreen(onNavigation = {
                navController.navigate(it)
            })
        }
        composable(
            route = "card/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) { backStackEntry->
            CardMenu(categoryId = backStackEntry.arguments?.getInt("categoryId")!!, onNavigation = {
                it?.let { route->
                    navController.navigate(route)
                }?: run {
                    navController.navigateUp()
                }
            })
        }
        composable(
            route = "list/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) { backStackEntry->
            ListMenu(categoryId = backStackEntry.arguments?.getInt("categoryId")!!, onNavigation = {
                it?.let { route->
                    navController.navigate(route)
                }?: run {
                    navController.navigateUp()
                }
            })
        }
        composable(
            route = "floatingmenu/{categoryName}/{itemId}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType }, navArgument("itemId") { type = NavType.IntType }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None}
        ) { backStackEntry->
            FloatingCard(categoryName = backStackEntry.arguments?.getString("categoryName")!!, itemId = backStackEntry.arguments?.getInt("itemId")!!, onNavigation = {
                it?.let { route->
                    navController.navigate(route)
                }?: run {
                    navController.navigateUp()
                }
            })
        }

    }
}