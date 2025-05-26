package com.playbackd.screens.album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playbackd.data.repositories.AlbumRepository
import com.playbackd.model.ListenListDTO
import com.playbackd.model.PlayedListDTO
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
        getCurrentAlbum()
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

    fun getCurrentAlbum() {
        viewModelScope.launch {
            albumRepository.getCurrentAlbum(albumId).onFailure {
                state = state.copy(error = it.message)
            }.onSuccess {
                state = state.copy(albumList = it?.list)
            }
        }
    }

    fun addListenList(listenList: ListenListDTO) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            albumRepository.addListenList(listenList).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(msg = it, isLoading = false)
            }
        }
    }

    fun addPlayed(played: PlayedListDTO) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            albumRepository.addPlayed(played).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(msg = it, isLoading = false)
            }
        }
    }

    fun updatePlayed(albumId: Int, played: PlayedListDTO) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            albumRepository.updatePlayed(albumId, played).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(msg = it, isLoading = false)
            }
        }
    }

    fun deletePlayed(albumId: Int) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            albumRepository.deletePlayed(albumId).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(msg = it, isLoading = false)
            }
        }
    }

    fun deleteListenList(albumId: Int) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            albumRepository.deleteListenList(albumId).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(msg = it, isLoading = false)
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}