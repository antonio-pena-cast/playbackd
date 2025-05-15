package com.playbackd.screens.album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playbackd.data.repositories.AlbumRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = AlbumDetailViewModelFactory::class)
class AlbumDetailViewModel @AssistedInject constructor(
    @Assisted("albumId") val albumId: Int, val albumRepository: AlbumRepository
) : ViewModel() {
    var state by mutableStateOf(AlbumDetailState())
        private set

    init {
        getAlbum()
        getAlbumReviews()
    }

    fun getAlbum() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            albumRepository.getAlbum(albumId).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(album = it, isLoading = false)
            }
        }
    }

    fun getAlbumReviews() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            albumRepository.getAlbumReviews(albumId).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(albumReviews = it, isLoading = false)
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}