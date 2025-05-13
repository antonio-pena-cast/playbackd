package com.playbackd

sealed class PlaybackdComposeEnvironment(val baseUrl: String) {
    object Local: PlaybackdComposeEnvironment("http://10.0.2.2:8000/api/")
}