package com.playbackd.screens.reviews

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ReviewsViewModelFactory {
    fun create(@Assisted("id") id: Int) : ReviewsViewModel
}