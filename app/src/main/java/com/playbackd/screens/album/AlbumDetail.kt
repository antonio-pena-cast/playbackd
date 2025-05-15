package com.playbackd.screens.album

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.playbackd.R
import com.playbackd.model.Album
import com.playbackd.model.AlbumReview
import com.playbackd.model.Review

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
    var album by remember { mutableStateOf<Album?>(null) }
    var albumReviews by remember { mutableStateOf<List<AlbumReview>?>(null) }
    var image by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var rating by remember { mutableDoubleStateOf(0.0) }
    var decodedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(state.album) {
        album = state.album

        album?.let {
            if (it.image != null) {
                image = it.image!!

                val imageBytes = Base64.decode(image, Base64.DEFAULT)
                decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
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
                rating = average
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

                Text(text = "Rating: $rating",)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {  },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("Reviews")
                }
            }

            FloatingActionButton(
                onClick = {  },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Review")
            }
        }
    }
}