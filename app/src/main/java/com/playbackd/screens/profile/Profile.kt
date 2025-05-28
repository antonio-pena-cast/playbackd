package com.playbackd.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.playbackd.R
import com.playbackd.model.User
import com.playbackd.model.UserDTO
import com.playbackd.model.UserPasswordDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController, viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var user by remember { mutableStateOf<User?>(null) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var showUserNameDialog by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    var showEmailDialog by remember { mutableStateOf(false) }
    var newEmail by remember { mutableStateOf("") }

    LaunchedEffect(state.user) {
        user = state.user

        user?.let {
            username = it.name
            email = it.email
        }
    }

    state.error?.let {
        AlertDialog(onDismissRequest = {
            viewModel.clearError()
        }, content = {
            Text(it)
        })
    }

    if (showPasswordDialog) {
        newPassword = ""

        AlertDialog(onDismissRequest = { showPasswordDialog = false }, confirmButton = {
            Button(onClick = {
                showPasswordDialog = false
                viewModel.updatePassword(UserPasswordDTO(newPassword))
            }) {
                Text("Cambiar")
            }
        }, dismissButton = {
            TextButton(onClick = { showPasswordDialog = false }) {
                Text("Cancelar")
            }
        }, title = { Text("Cambiar Contraseña") }, text = {
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nueva Contraseña") },
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisibility) "Visible" else "Invisible"

                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(imageVector = image, "")
                    }
                },
            )
        })
    }

    if (showUserNameDialog) {
        newUsername = username

        AlertDialog(onDismissRequest = { showUserNameDialog = false }, confirmButton = {
            Button(onClick = {
                showUserNameDialog = false
                username = newUsername
                viewModel.updateUser(UserDTO(newUsername, email))
            }) {
                Text("Cambiar")
            }
        }, dismissButton = {
            TextButton(onClick = { showUserNameDialog = false }) {
                Text("Cancelar")
            }
        }, title = { Text("Cambiar Nombre de Usuario") }, text = {
            OutlinedTextField(
                value = newUsername,
                onValueChange = { newUsername = it },
                label = { Text("Nuevo Nombre de Usuario") },
                singleLine = true
            )
        })
    }

    if (showEmailDialog) {
        newEmail = email

        AlertDialog(onDismissRequest = { showEmailDialog = false }, confirmButton = {
            Button(onClick = {
                showEmailDialog = false
                email = newEmail
                viewModel.updateUser(UserDTO(username, newEmail))
            }) {
                Text("Cambiar")
            }
        }, dismissButton = {
            TextButton(onClick = { showEmailDialog = false }) {
                Text("Cancelar")
            }
        }, title = { Text("Cambiar Email") }, text = {
            OutlinedTextField(
                value = newEmail,
                onValueChange = { newEmail = it },
                label = { Text("Nuevo Email") },
                singleLine = true
            )
        })
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
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

            TextButton(
                onClick = { showUserNameDialog = true },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
            ) {
                Text(text = username, style = MaterialTheme.typography.titleLarge)
            }
            TextButton(
                onClick = { showEmailDialog = true },
                modifier = Modifier.align(Alignment.Start),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
            ) {
                Text(text = email, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showPasswordDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Cambiar Contraseña")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("played_albums")
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Played")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("listenlist_albums")
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver ListenList")
            }
        }
    }
}