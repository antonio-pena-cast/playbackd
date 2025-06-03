package com.playbackd.screens.login;

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playbackd.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val authRepository: AuthRepository) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            authRepository.login(email, password).onFailure {
                state = state.copy(error = it.message, isLoading = false, success = false)
            }.onSuccess {
                state = state.copy(success = it, isLoading = false)
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}
