package com.playbackd.screens.album

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface AlbumDetailViewModelFactory {
    fun create(@Assisted("albumId") id: Int): AlbumDetailViewModel
}