package com.playbackd.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.playbackd.model.User

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var user by remember { mutableStateOf<User?>(null) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }

    LaunchedEffect(state.user) {
        user = state.user

        user?.let {
            username = it.name
            email = it.email
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = username, style = MaterialTheme.typography.titleLarge)
        Text(text = email, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { showPasswordDialog = true }) {
            Text("Change Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("played_albums")
        }) {
            Text("See Played")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("listenlist_albums")
        }) {
            Text("See ListenList")
        }
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        //onChangePassword(newPassword)
                        showPasswordDialog = false
                    }
                ) {
                    Text("Change")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Change Password") },
            text = {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    singleLine = true
                )
            }
        )
    }
}