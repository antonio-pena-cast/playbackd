package com.playbackd.data.api

import com.playbackd.model.LoginResponse
import com.playbackd.model.RegisterResponse
import com.playbackd.model.UserLogin
import com.playbackd.model.UserRegister
import retrofit2.http.Body
import retrofit2.http.POST

interface PlaybackdAPI {
    @POST("login")
    suspend fun login(@Body credentials: UserLogin): LoginResponse

    @POST("register")
    suspend fun register(@Body credentials: UserRegister): RegisterResponse
}
