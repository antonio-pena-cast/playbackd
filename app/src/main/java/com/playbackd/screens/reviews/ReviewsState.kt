package com.playbackd.screens.reviews

import com.playbackd.model.FullReview
import com.playbackd.model.Review

data class ReviewsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val reviews: List<FullReview>? = null
)
