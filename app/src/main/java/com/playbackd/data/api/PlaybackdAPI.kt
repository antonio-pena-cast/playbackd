package com.playbackd.data.api

import com.playbackd.model.AlbumResponse
import com.playbackd.model.ReviewResponse
import com.playbackd.model.AlbumsResponse
import com.playbackd.model.ListResponse
import com.playbackd.model.ListenListDTO
import com.playbackd.model.AddListResponse
import com.playbackd.model.LoginResponse
import com.playbackd.model.PlayedListDTO
import com.playbackd.model.RegisterResponse
import com.playbackd.model.UserDTO
import com.playbackd.model.UserListResponse
import com.playbackd.model.UserLogin
import com.playbackd.model.UserPasswordDTO
import com.playbackd.model.UserRegister
import com.playbackd.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("user/albums/{id}")
    suspend fun getCurrentAlbumList(@Path("id") id: Int): ListResponse

    @POST("listenlist")
    suspend fun addListenList(@Body data: ListenListDTO): AddListResponse

    @POST("played")
    suspend fun addPlayed(@Body data: PlayedListDTO): AddListResponse

    @PUT("user/albums/{id}")
    suspend fun updatePlayed(@Body data: PlayedListDTO, @Path("id") id: Int): AddListResponse

    @DELETE("played/{id}")
    suspend fun deletePlayed(@Path("id") id: Int): AddListResponse

    @DELETE("listenlist/{id}")
    suspend fun deleteListenList(@Path("id") id: Int): AddListResponse

    @GET("user")
    suspend fun getUser(): UserResponse

    @PUT("user")
    suspend fun updatePassword(@Body data: UserPasswordDTO): LoginResponse

    @PUT("user")
    suspend fun updateUser(@Body data: UserDTO): LoginResponse

    @GET("listenlist")
    suspend fun getListenList(): UserListResponse

    @GET("played")
    suspend fun getPlayedList(): UserListResponse
}
