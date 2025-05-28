package com.playbackd.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.playbackd.screens.album.AlbumDetailScreen
import com.playbackd.screens.home.HomeScreen
import com.playbackd.screens.login.LoginScreen
import com.playbackd.screens.profile.ProfileScreen
import com.playbackd.screens.register.RegisterScreen
import com.playbackd.screens.reviews.ReviewsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController)
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
        composable(route = AppScreens.UserProfile.route) {
            ProfileScreen(navController)
        }
    }
}