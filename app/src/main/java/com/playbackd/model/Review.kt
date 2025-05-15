package com.playbackd.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Review(
    @SerializedName("album_id")
    val albumId: Int?,
    @SerializedName("user_id")
    val userId: Int?,
    var type: String?,
    var review: String?,
    var rating: Double?,
    var date: LocalDate?
)
