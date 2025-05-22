package com.playbackd.screens.album

import com.playbackd.model.Album
import com.playbackd.model.AlbumList
import com.playbackd.model.FullReview
import com.playbackd.model.ListFullResponse

data class AlbumDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val album: Album? = null,
    val albumReviews: List<FullReview>? = null,
    val msg: String? = null,
    val albumList: AlbumList? = null
)
