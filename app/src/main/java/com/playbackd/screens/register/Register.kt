package com.playbackd.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.playbackd.utilities.UrlProviderViewModel
import com.playbackd.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    onReload: () -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel(),
    urlViewModel: UrlProviderViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.navigate(AppScreens.HomeScreen.route)
        }
    }

    state.error?.let {
        var showDialog by remember { mutableStateOf(true) }
        var newUrl by remember { mutableStateOf(urlViewModel.currentUrl) }

        if (showDialog) {
            AlertDialog(onDismissRequest = {
                showDialog = false
                viewModel.clearError()
            }, confirmButton = {
                Button(onClick = {
                    if (newUrl.isNotBlank()) {
                        urlViewModel.updateUrl(newUrl)
                    }
                    showDialog = false
                    viewModel.clearError()
                    onReload()
                }) {
                    Text("Aceptar")
                }
            }, dismissButton = {
                Button(onClick = {
                    showDialog = false
                    viewModel.clearError()
                }) {
                    Text("Cancelar")
                }
            }, title = { Text("Error") }, text = {
                Column {
                    Text("Se ha detectado un error, pruebe a usar otro correo electronico o " +
                                 "verifique que la dirección URL es la correcta")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newUrl,
                                      onValueChange = { newUrl = it },
                                      label = { Text("Nueva URL base") },
                                      singleLine = true
                    )
                }
            })
        }
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
        Column {
            Column(
                Modifier
                    .padding(20.dp, 5.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(value = username,
                                  onValueChange = { input ->
                                      username = input
                                  },
                                  label = { Text("Username") },
                                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                  modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(value = email, onValueChange = { input ->
                    email = input
                    errorMessage = if (input.matches(emailPattern.toRegex())) {
                        null
                    } else {
                        "Please enter a valid email address"
                    }
                }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ), isError = errorMessage != null, modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(value = password,
                                  onValueChange = { input ->
                                      password = input
                                  },
                                  visualTransformation = PasswordVisualTransformation(),
                                  label = { Text("Password") },
                                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                  modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                onClick = { viewModel.register(username, email, password) },
                Modifier
                    .padding(20.dp, 5.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("REGISTER")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Already have an account?")
                TextButton(onClick = {
                    navController.navigate(AppScreens.LoginScreen.route)
                }) {
                    Text("Click here to log in", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewRegister() {
    RegisterScreen(rememberNavController())
}
