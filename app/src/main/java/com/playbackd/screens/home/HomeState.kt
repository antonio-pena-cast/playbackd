package com.playbackd.screens.home

import com.playbackd.model.Album

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val albums: List<Album>? = null
)