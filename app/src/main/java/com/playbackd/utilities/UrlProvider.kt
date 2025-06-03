package com.playbackd.utilities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.playbackd.PlaybackdComposeEnvironment

class UrlProvider {
    var baseUrl: String by mutableStateOf(PlaybackdComposeEnvironment.baseUrl)
}