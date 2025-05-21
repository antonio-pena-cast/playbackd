package com.playbackd.screens.reviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.playbackd.model.FullReview
import com.playbackd.navigation.AppScreens

@Composable
fun ReviewsScreen(
    navController: NavController,
    type: String,
    id: Int,
    viewModel: ReviewsViewModel = hiltViewModel<ReviewsViewModel, ReviewsViewModelFactory> {
        it.create(id)
    }
) {
    val state = viewModel.state
    val reviews = remember { mutableStateListOf<FullReview?>(null) }

    LaunchedEffect(state.reviews) {
        val fetchedReviews = state.reviews

        fetchedReviews?.let {
            reviews.clear()
            reviews.addAll(it)
        }
    }

    Column {
        LazyColumn {
            items(reviews) { review ->
                if (review != null) {
                    ReviewCard(review = review, type = type, navController = navController)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ReviewCard(review: FullReview, type: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = review.name,
                     color = MaterialTheme.colorScheme.primary,
                     modifier = Modifier.clickable {
                         if (type == "album") {
                             navController.navigate(AppScreens.AlbumDetailScreen.route + "/" + review.review?.userId)
                         } else { //TODO: Navigate to user detail
                         }
                     })
                Text("⭐ ${review.review?.rating}")
            }
            Spacer(modifier = Modifier.height(4.dp))
            review.review?.review?.let { Text(text = it) }
        }
    }
}
