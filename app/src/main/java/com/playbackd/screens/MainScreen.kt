package com.playbackd.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.playbackd.PlaybackdComposeEnvironment
import com.playbackd.controller.SessionManager
import com.playbackd.menu.OptionsMenu
import com.playbackd.navigation.AppNavigation
import com.playbackd.navigation.AppScreens
import com.playbackd.utilities.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, sessionManager: SessionManager, themeViewModel: ThemeViewModel) {
    val context = LocalContext.current
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    var showInternetDialog by remember { mutableStateOf(false) }
    var newBaseUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (!isInternetAvailable(context)) {
            showInternetDialog = true
        }
    }

    val title = when (currentRoute) {
        AppScreens.LoginScreen.route -> "Login"
        AppScreens.RegisterScreen.route -> "Register"
        AppScreens.HomeScreen.route -> "Inicio"
        AppScreens.AlbumDetailScreen.route -> "Album"
        AppScreens.AlbumReviewsScreen.route -> "Reviews"
        AppScreens.UserProfileScreen.route -> "Perfil"
        AppScreens.ListScreen.route -> "Lista"

        else -> "API Practice"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Black
                ),
                actions = { OptionsMenu(navController) }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(navController, sessionManager, themeViewModel)
        }
    }

    if (showInternetDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    PlaybackdComposeEnvironment.baseUrl = newBaseUrl
                    showInternetDialog = false
                }) {
                    Text("Guardar")
                }
            },
            title = { Text("Sin conexión") },
            text = {
                Column {
                    Text("No hay conexión a Internet. Puedes introducir manualmente una URL base del servidor.")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newBaseUrl,
                        onValueChange = { newBaseUrl = it },
                        label = { Text("Nueva URL") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
