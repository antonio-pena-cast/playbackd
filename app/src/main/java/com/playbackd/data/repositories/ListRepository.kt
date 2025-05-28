package com.playbackd.data.repositories

import com.playbackd.data.api.PlaybackdAPI
import com.playbackd.model.UserListFullResponse
import com.playbackd.model.UserListResponse
import javax.inject.Inject

class ListRepository @Inject constructor(val playbackdAPI: PlaybackdAPI) {
    suspend fun getListenList(): Result<List<UserListFullResponse>?> {
        val result = getListenListRemote()

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Get listenlist failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun getListenListRemote(): Result<UserListResponse> {
        return try {
            Result.success(playbackdAPI.getListenList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPlayedList(): Result<List<UserListFullResponse>?> {
        val result = getPlayedListRemote()

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Get played failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun getPlayedListRemote(): Result<UserListResponse> {
        return try {
            Result.success(playbackdAPI.getPlayedList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}