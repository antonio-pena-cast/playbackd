package com.playbackd.data.repositories

import com.playbackd.data.api.PlaybackdAPI
import com.playbackd.model.Album
import com.playbackd.model.AlbumResponse
import com.playbackd.model.AlbumsResponse
import javax.inject.Inject

class AlbumRepository @Inject constructor(val playbackdAPI: PlaybackdAPI) {
    suspend fun getAlbums(): Result<List<Album>> {
        val result = getAlbumsRemote()

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Get albums failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun getAlbumsRemote(): Result<AlbumsResponse> {
        return try {
            Result.success(playbackdAPI.getAlbums())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}