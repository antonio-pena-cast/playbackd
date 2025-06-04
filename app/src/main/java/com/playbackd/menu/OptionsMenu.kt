package com.playbackd.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.playbackd.navigation.AppScreens

@Composable
fun OptionsMenu(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options", tint = Color.Black)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("Login") },
                onClick = {
                    navController.navigate(route = AppScreens.LoginScreen.route)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Registrarse") },
                onClick = {
                    navController.navigate(route = AppScreens.RegisterScreen.route)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Perfil") },
                onClick = {
                    navController.navigate(route = AppScreens.UserProfileScreen.route)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Inicio") },
                onClick = {
                    navController.navigate(route = AppScreens.HomeScreen.route)
                    expanded = false
                }
            )
        }
    }
}
