package com.playbackd.data.repositories

import com.playbackd.controller.SessionManager
import com.playbackd.data.api.PlaybackdAPI
import com.playbackd.model.LoginResponse
import com.playbackd.model.UserLogin
import com.playbackd.model.UserRegister
import javax.inject.Inject

class AuthRepository @Inject constructor(val playbackdAPI: PlaybackdAPI, val sessionManager: SessionManager) {
    suspend fun login(email: String, password: String): Result<Boolean> {
        val result = loginRemote(UserLogin(email, password))

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("The login failed"))
        }

        val data = result.getOrThrow()

        sessionManager.saveAuthToken(data.msg)

        return Result.success(true)
    }

    private suspend fun loginRemote(user: UserLogin): Result<LoginResponse> {
        return try {
            Result.success(playbackdAPI.login(user))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<Boolean> {
        val result = registerRemote(UserRegister(username, email, password))

        if (result.isFailure) {
            return Result.failure(result.exceptionOrNull() ?: Exception("The register failed"))
        }

        val data = result.getOrThrow()

        sessionManager.saveAuthToken(data.msg)

        return Result.success(true)
    }

    private suspend fun registerRemote(user: UserRegister): Result<LoginResponse> {
        return try {
            Result.success(playbackdAPI.register(user))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
