package com.playbackd.utilities

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playbackd.controller.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(val sessionManager: SessionManager) : ViewModel() {
    val isDarkTheme = mutableStateOf(false)

    init {
        viewModelScope.launch {
            isDarkTheme.value = sessionManager.getThemePreference()
        }
    }

    fun toggleTheme() {
        val newTheme = !isDarkTheme.value
        isDarkTheme.value = newTheme

        viewModelScope.launch {
            sessionManager.saveThemePreference(newTheme)
        }
    }
}
