package com.zgsbrgr.compose.anim

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(navController)
        }
        composable(route = "cards") {
            CardMenu()
        }
        composable(route = "list") {
            ListMenu()
        }
        composable(route = "floatingmenu/{menuIcon}", arguments = listOf(navArgument("menuIcon") { type = NavType.IntType })) { backStackEntry->
            FloatingCard(navController, menuIcon = backStackEntry.arguments?.getInt("menuIcon")!!)
        }

    }
}