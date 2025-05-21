package com.playbackd.data.repositories

import com.playbackd.data.api.PlaybackdAPI
import com.playbackd.model.FullReview
import com.playbackd.model.ReviewResponse
import javax.inject.Inject

class ReviewRepository @Inject constructor(val playbackdAPI: PlaybackdAPI) {
    suspend fun getAlbumReviews(albumId: Int): Result<List<FullReview>> {
        val result = getAlbumReviewsRemote(albumId)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Get album reviews failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    suspend fun getAlbumReviewsRemote(albumId: Int): Result<ReviewResponse> {
        return try {
            Result.success(playbackdAPI.getAlbumReviews(albumId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}