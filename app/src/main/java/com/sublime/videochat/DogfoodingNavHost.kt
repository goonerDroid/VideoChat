package com.sublime.videochat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppScreens.Login.route
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppScreens.Login.route){ backStackEntry ->
            //Add LoginScreen Here
        }
    }
}

enum class AppScreens(val route: String) {
    Login("login/{auto_log_in}"), ;

    fun routeWithArg(argValue: Any): String = when (this) {
        Login -> this.route.replace("{auto_log_in}", argValue.toString())
        else -> this.route
    }
}