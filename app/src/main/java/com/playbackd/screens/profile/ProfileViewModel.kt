package com.playbackd.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playbackd.data.repositories.UserRepository
import com.playbackd.model.UserDTO
import com.playbackd.model.UserPasswordDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            userRepository.getUser().onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(user = it, isLoading = false)
            }
        }
    }

    fun updatePassword(password: UserPasswordDTO) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            userRepository.updatePassword(password).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(msg = it, isLoading = false)
            }
        }
    }

    fun updateUser(user: UserDTO) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            userRepository.updateUser(user).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(msg = it, isLoading = false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            userRepository.logout().onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(success = it, isLoading = false)
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}