package com.playbackd.screens.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playbackd.data.repositories.AlbumRepository
import com.playbackd.data.repositories.ListRepository
import com.playbackd.model.Album
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = ListViewModelFactory::class)
class ListViewModel @AssistedInject constructor(
    val listRepository: ListRepository, @Assisted("type") val type: String
) : ViewModel() {
    var state by mutableStateOf(ListState())
        private set

    init {
        getAlbums()
    }

    fun getAlbums() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            if (type == "listenlist") {
                listRepository.getListenList().onFailure {
                    state = state.copy(error = it.message, isLoading = false)
                }.onSuccess {
                    val albums: ArrayList<Album> = ArrayList<Album>()

                    it?.map { album ->
                        albums.add(
                            Album(
                                album.id,
                                album.name,
                                album.author,
                                album.genre,
                                album.releaseDate,
                                album.image
                            )
                        )
                    }

                    state = state.copy(listenList = albums, isLoading = false)
                }
            } else if (type == "played") {
                listRepository.getPlayedList().onFailure {
                    state = state.copy(error = it.message, isLoading = false)
                }.onSuccess {
                    val albums: ArrayList<Album> = ArrayList<Album>()

                    it?.map { album ->
                        albums.add(
                            Album(
                                album.id,
                                album.name,
                                album.author,
                                album.genre,
                                album.releaseDate,
                                album.image
                            )
                        )
                    }

                    state = state.copy(playedList = albums, isLoading = false)
                }
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}