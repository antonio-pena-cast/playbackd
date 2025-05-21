package com.playbackd.screens.album

import com.playbackd.model.Album
import com.playbackd.model.FullReview

data class AlbumDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val album: Album? = null,
    val albumReviews: List<FullReview>? = null
)
