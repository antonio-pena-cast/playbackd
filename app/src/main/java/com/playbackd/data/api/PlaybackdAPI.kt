package com.playbackd.data.api

import com.playbackd.model.AlbumResponse
import com.playbackd.model.ReviewResponse
import com.playbackd.model.AlbumsResponse
import com.playbackd.model.LoginResponse
import com.playbackd.model.RegisterResponse
import com.playbackd.model.UserLogin
import com.playbackd.model.UserRegister
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlaybackdAPI {
    @POST("login")
    suspend fun login(@Body credentials: UserLogin): LoginResponse

    @POST("register")
    suspend fun register(@Body credentials: UserRegister): RegisterResponse

    @GET("albums")
    suspend fun getAlbums(): AlbumsResponse

    @GET("albums/{id}")
    suspend fun getAlbum(@Path("id") id: Int): AlbumResponse

    @GET("albums/{id}/reviews")
    suspend fun getAlbumReviews(@Path("id") id: Int): ReviewResponse
}
