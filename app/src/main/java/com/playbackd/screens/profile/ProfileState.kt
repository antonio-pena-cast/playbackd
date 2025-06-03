package com.playbackd.screens.profile

import com.playbackd.model.User

data class ProfileState(
    val isLoading: Boolean = false,
    val error: String? = null,
    var user: User? = null,
    var msg: String? = null,
    var success: Boolean = false,
)
