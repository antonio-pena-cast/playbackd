package com.playbackd.screens.album

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.playbackd.R
import com.playbackd.model.Album
import com.playbackd.model.FullReview
import com.playbackd.navigation.AppScreens
import kotlin.math.ceil
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    navController: NavController,
    albumId: Int,
    viewModel: AlbumDetailViewModel = hiltViewModel<AlbumDetailViewModel, AlbumDetailViewModelFactory> {
        it.create(albumId)
    }
) {
    val state = viewModel.state
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    var album by remember { mutableStateOf<Album?>(null) }
    var albumReviews by remember { mutableStateOf<List<FullReview>?>(null) }
    var image by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var avgRating by remember { mutableDoubleStateOf(0.0) }
    var decodedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var rating by remember { mutableDoubleStateOf(0.0) }
    var review by remember { mutableStateOf("") }
    var playedButtonState by remember { mutableStateOf(false) }
    var listenListButtonState by remember { mutableStateOf(false) }
    var playedButtonBackground by remember { mutableStateOf<Color>(secondaryColor) }
    var listenListButtonBackground by remember { mutableStateOf<Color>(secondaryColor) }

    LaunchedEffect(state.album) {
        album = state.album

        album?.let {
            if (it.image != null) {
                image = it.image!!

                val imageBytes = Base64.decode(image, Base64.DEFAULT)
                decodedImage =
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
            }
            name = it.name
            author = it.author
            date = it.releaseDate.toString()
        }
    }

    LaunchedEffect(state.albumReviews) {
        albumReviews = state.albumReviews

        albumReviews?.let {
            val ratings = it.mapNotNull { it.review?.rating }

            if (ratings.isNotEmpty()) {
                val average = ratings.sum() / ratings.size
                avgRating = average
            }
        }
    }

    state.error?.let {
        AlertDialog(onDismissRequest = {
            viewModel.clearError()
        }, content = {
            Text(it)
        })
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (decodedImage != null) {
                        Image(
                            bitmap = decodedImage!!,
                            contentDescription = "Album Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.image_not_found),
                            contentDescription = "Image not found",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(text = name)
                        Text(text = author)
                        Text(text = date)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Rating: $avgRating / 5.0")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(AppScreens.AlbumReviewsScreen.route + "/" + albumId) },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("Reviews")
                }
            }

            FloatingActionButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Review")
            }

            //Modal para añadir album a lista
            if (showBottomSheet) {
                ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    playedButtonState = !playedButtonState

                                    playedButtonBackground = if (playedButtonState) {
                                        primaryColor
                                    } else {
                                        secondaryColor
                                    }
                                },
                                colors = IconButtonDefaults.iconButtonColors(containerColor = playedButtonBackground)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Botón Played",
                                    modifier = Modifier.size(40.dp),
                                )
                            }

                            IconButton(
                                onClick = {
                                    listenListButtonState = !listenListButtonState

                                    listenListButtonBackground = if (listenListButtonState) {
                                        primaryColor
                                    } else {
                                        secondaryColor
                                    }
                                },
                                colors = IconButtonDefaults.iconButtonColors(containerColor = listenListButtonBackground)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Botón ListenList",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            StarRatingBar(
                                rating = rating,
                                onRatingChanged = { rating = it },
                                modifier = Modifier.wrapContentWidth(),
                                starSize = 64.dp
                            )
                        }

                        OutlinedTextField(
                            value = review,
                            onValueChange = { review = it },
                            label = { Text("Review") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(vertical = 8.dp)
                        )

                        Button(
                            onClick = { showBottomSheet = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text("Send")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StarRatingBar(
    rating: Double,
    onRatingChanged: (Double) -> Unit,
    modifier: Modifier = Modifier,
    starSize: Dp = 64.dp,
    starCount: Int = 5,
) {
    Row(modifier = modifier) {
        for (i in 1..starCount) {
            val icon = when {
                i <= floor(rating) -> Icons.Default.Star
                i - 0.5 <= rating -> Icons.Default.StarHalf
                else -> Icons.Default.StarBorder
            }

            Icon(
                imageVector = icon,
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(starSize)
                    .clickable {
                        val currentStarValue = i.toDouble()
                        val newRating = when {
                            rating >= currentStarValue -> currentStarValue - 0.5
                            rating >= currentStarValue - 0.5 -> currentStarValue - 1
                            else -> currentStarValue
                        }.coerceIn(0.0, starCount.toDouble())
                        onRatingChanged(newRating)
                    },
                tint = if (i <= ceil(rating)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        }
    }
}