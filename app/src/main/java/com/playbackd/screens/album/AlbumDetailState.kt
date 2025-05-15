package com.playbackd.screens.album

import com.playbackd.model.Album
import com.playbackd.model.AlbumReview

data class AlbumDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val album: Album? = null,
    val albumReviews: List<AlbumReview>? = null
)
