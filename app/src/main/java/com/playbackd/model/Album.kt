package com.playbackd.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import kotlin.collections.List

data class Album(
    val id: Int,
    var name: String,
    var author: String,
    var genre: String,
    @SerializedName("release_date")
    var releaseDate: LocalDate?,
    var image: String?
)

data class AlbumReview(
    val id: Int,
    var name: String,
    var email: String,
    @SerializedName("pivot")
    var review: Review?
)

data class AlbumResponse(
    val msg: Album
)

data class AlbumsResponse(
    val msg: List<Album>
)

data class AlbumReviewResponse(
    val msg: List<AlbumReview>
)

data class AlbumDTO(
    var name: String,
    var author: String,
    var genre: String,
    @SerializedName("release_date")
    var releaseDate: String,
    var image: String?
)
