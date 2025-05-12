package com.playbackd

sealed class PlaybackdComposeEnvironment(val baseUrl: String) {
    object Local: PlaybackdComposeEnvironment("http://localhost:8000/api/")
}