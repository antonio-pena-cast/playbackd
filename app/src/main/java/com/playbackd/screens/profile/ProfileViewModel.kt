package com.playbackd.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.playbackd.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set


}