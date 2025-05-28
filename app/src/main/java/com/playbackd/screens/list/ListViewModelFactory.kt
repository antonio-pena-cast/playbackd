package com.playbackd.screens.list

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ListViewModelFactory {
    fun create(@Assisted("type") type: String): ListViewModel
}