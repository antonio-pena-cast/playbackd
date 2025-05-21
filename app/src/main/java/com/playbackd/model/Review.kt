package com.playbackd.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import kotlin.collections.List

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

data class FullReview(
    val id: Int,
    var name: String,
    var email: String,
    @SerializedName("pivot")
    var review: Review?
)

data class ReviewResponse(
    val msg: List<FullReview>
)
