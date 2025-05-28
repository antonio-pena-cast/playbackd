package com.playbackd.data.repositories

import com.playbackd.data.api.PlaybackdAPI
import com.playbackd.model.LoginResponse
import com.playbackd.model.User
import com.playbackd.model.UserDTO
import com.playbackd.model.UserPasswordDTO
import com.playbackd.model.UserResponse
import javax.inject.Inject

class UserRepository @Inject constructor(val playbackdAPI: PlaybackdAPI) {
    suspend fun getUser(): Result<User> {
        val result = getUserRemote()

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Get user failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun getUserRemote(): Result<UserResponse> {
        return try {
            Result.success(playbackdAPI.getUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassword(password: UserPasswordDTO): Result<String> {
        val result = updatePasswordRemote(password)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Update user password failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun updatePasswordRemote(password: UserPasswordDTO): Result<LoginResponse> {
        return try {
            Result.success(playbackdAPI.updatePassword(password))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: UserDTO): Result<String> {
        val result = updateUserRemote(user)

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("Update user failure"))
        }

        val data = result.getOrThrow()

        return Result.success(data.msg)
    }

    private suspend fun updateUserRemote(user: UserDTO): Result<LoginResponse> {
        return try {
            Result.success(playbackdAPI.updateUser(user))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}