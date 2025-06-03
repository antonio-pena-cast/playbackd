package com.playbackd.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen: AppScreens("login_screen")
    object RegisterScreen: AppScreens("register_screen")
    object HomeScreen: AppScreens("home_screen")
    object AlbumDetailScreen: AppScreens("album_detail_screen")
    object AlbumReviewsScreen: AppScreens("album_reviews_screen")
    object UserReviewsScreen: AppScreens("user_reviews_screen")
    object UserProfileScreen: AppScreens("user_profile_screen")
    object ListScreen: AppScreens("list_screen")
}