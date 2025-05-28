package com.playbackd.screens.list

import com.playbackd.model.Album
import com.playbackd.model.UserListFullResponse

data class ListState(
    val isLoading: Boolean = false,
    val error: String? = null,
    var listenList: List<Album>? = null,
    var playedList: List<Album>? = null
)
