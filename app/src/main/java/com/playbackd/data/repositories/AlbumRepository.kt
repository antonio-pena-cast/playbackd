package com.playbackd.data.repositories

import com.playbackd.data.api.PlaybackdAPI
import com.playbackd.model.Album
import com.playbackd.model.AlbumResponse
import com.playbackd.model.FullReview
import com.playbackd.model.ReviewResponse
import com.playbackd.model.AlbumsResponse
import com.playbackd.model.ListFullResponse
import com.playbackd.model.ListResponse
import com.playbackd.model.ListenListDTO
import com.playbackd.model.AddListResponse
import com.playbackd.model.PlayedListDTO
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

    suspend fun getAlbum(albumId: Int): Result<Album> {
        val result = getAlbumRemote(albumId)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Get album failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun getAlbumRemote(albumId: Int): Result<AlbumResponse> {
        return try {
            Result.success(playbackdAPI.getAlbum(albumId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumReviews(albumId: Int): Result<List<FullReview>> {
        val result = getAlbumReviewsRemote(albumId)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Get album reviews failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun getAlbumReviewsRemote(albumId: Int): Result<ReviewResponse> {
        return try {
            Result.success(playbackdAPI.getAlbumReviews(albumId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentAlbum(albumId: Int): Result<ListFullResponse?> {
        val result = getCurrentAlbumRemote(albumId)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Get current album failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun getCurrentAlbumRemote(albumId: Int): Result<ListResponse> {
        return try {
            Result.success(playbackdAPI.getCurrentAlbumList(albumId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addListenList(listenList: ListenListDTO): Result<String> {
        val result = addListenListRemote(listenList)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Add album to listen list failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun addListenListRemote(listenList: ListenListDTO): Result<AddListResponse> {
        return try {
            Result.success(playbackdAPI.addListenList(listenList))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addPlayed(playedListDTO: PlayedListDTO): Result<String> {
        val result = addPlayedRemote(playedListDTO)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Add album to played failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun addPlayedRemote(playedListDTO: PlayedListDTO): Result<AddListResponse> {
        return try {
            Result.success(playbackdAPI.addPlayed(playedListDTO))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
