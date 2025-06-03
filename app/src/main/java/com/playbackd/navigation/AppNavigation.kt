package com.playbackd.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.playbackd.controller.SessionManager
import com.playbackd.screens.album.AlbumDetailScreen
import com.playbackd.screens.home.HomeScreen
import com.playbackd.screens.list.ListScreen
import com.playbackd.screens.login.LoginScreen
import com.playbackd.screens.profile.ProfileScreen
import com.playbackd.screens.register.RegisterScreen
import com.playbackd.screens.reviews.ReviewsScreen
import com.playbackd.utilities.ThemeViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    sessionManager: SessionManager,
    themeViewModel: ThemeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = if (sessionManager.getAuthToken() == null || sessionManager.getAuthToken() == "") {
            AppScreens.LoginScreen.route
        } else {
            AppScreens.HomeScreen.route
        }
    ) {
        composable("dummy") {
            LaunchedEffect(Unit) {
                navController.navigate(AppScreens.HomeScreen.route) {
                    popUpTo("dummy") { inclusive = true }
                }
            }
        }
        composable("dummyLogin") {
            LaunchedEffect(Unit) {
                navController.navigate(AppScreens.LoginScreen.route) {
                    popUpTo("dummyLogin") { inclusive = true }
                }
            }
        }
        composable("dummyRegister") {
            LaunchedEffect(Unit) {
                navController.navigate(AppScreens.RegisterScreen.route) {
                    popUpTo("dummyRegister") { inclusive = true }
                }
            }
        }
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController, onReload = {
                navController.navigate("dummyLogin") {
                    popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                }
            })
        }
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController, onReload = {
                navController.navigate("dummyRegister") {
                    popUpTo(AppScreens.RegisterScreen.route) { inclusive = true }
                }
            })
        }
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController, onReload = {
                navController.navigate("dummy") {
                    popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
                }
            })
        }
        composable(route = AppScreens.AlbumDetailScreen.route + "/{id}") {
            val id: Int = it.arguments?.getString("id")?.toInt() ?: 0
            AlbumDetailScreen(navController, id)
        }
        composable(route = AppScreens.AlbumReviewsScreen.route + "/{id}") {
            val id: Int = it.arguments?.getString("id")?.toInt() ?: 0
            ReviewsScreen(navController, type = "album", id)
        }
        composable(route = AppScreens.UserReviewsScreen.route + "/{id}") {
            val id: Int = it.arguments?.getString("id")?.toInt() ?: 0
            ReviewsScreen(navController, type = "user", id)
        }
        composable(route = AppScreens.UserProfileScreen.route) {
            ProfileScreen(navController, themeViewModel = themeViewModel)
        }
        composable(route = AppScreens.ListScreen.route + "/{type}") {
            val type: String = it.arguments?.getString("type") ?: "listenlist"
            ListScreen(navController, type)
        }
    }
}