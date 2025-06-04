package com.playbackd.screens.reviews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playbackd.data.repositories.ReviewRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReviewsViewModelFactory::class)
class ReviewsViewModel @AssistedInject constructor(
    @Assisted("id") val id: Int,
    val reviewRepository: ReviewRepository
) : ViewModel() {
    var state by mutableStateOf(ReviewsState())
        private set

    init {
        getAlbumReviews()
    }

    fun getAlbumReviews() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            reviewRepository.getAlbumReviews(id).onFailure {
                state = state.copy(error = it.message, isLoading = false)
            }.onSuccess {
                state = state.copy(reviews = it, isLoading = false)
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}