package com.playbackd.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playbackd.data.repositories.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val albumRepository: AlbumRepository) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        getAlbums()
    }

    fun getAlbums() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            albumRepository.getAlbums().onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(albums = it, isLoading = false)
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}