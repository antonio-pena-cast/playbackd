package com.playbackd.model

import com.google.gson.annotations.SerializedName

data class List(
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("album_id")
    var albumId: Int,
    var type: String,
    var review: String,
    var rating: Double
)

data class ListResponse(
    val msg: kotlin.collections.List<List>
)

data class ListsResponse(
    val msg: kotlin.collections.List<List>
)

data class ListDTO(
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("album_id")
    var albumId: Int,
    var type: String,
    var review: String,
    var rating: Double
)
