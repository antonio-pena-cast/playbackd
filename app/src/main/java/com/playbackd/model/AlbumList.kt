package com.playbackd.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class AlbumList(
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("album_id")
    var albumId: Int,
    var type: String,
    var review: String?,
    var rating: Double?,
    var date: LocalDate?
)

data class ListFullResponse(
    val id: Int,
    var name: String,
    var email: String,
    @SerializedName("pivot")
    var list: AlbumList?
)

data class ListResponse(
    val msg: ListFullResponse?
)

data class UserListFullResponse(
    val id: Int,
    var name: String,
    var author: String,
    var genre: String,
    @SerializedName("release_date")
    var releaseDate: LocalDate?,
    var image: String?,
    @SerializedName("pivot")
    var list: AlbumList?
)

data class UserListResponse(
    val msg: List<UserListFullResponse>?
)

data class AddListResponse(
    val msg: String
)

data class PlayedListDTO(
    var albumId: Int,
    var review: String?,
    var rating: Double?,
    var date: String?,
)

data class ListenListDTO(
    var albumId: Int
)
