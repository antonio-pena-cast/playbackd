package com.playbackd.screens.album

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Build.VERSION_CODES
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.playbackd.R
import com.playbackd.model.Album
import com.playbackd.model.AlbumList
import com.playbackd.model.FullReview
import com.playbackd.model.ListenListDTO
import com.playbackd.model.PlayedListDTO
import com.playbackd.navigation.AppScreens
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.ceil
import kotlin.math.floor

@RequiresApi(VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    navController: NavController,
    albumId: Int,
    viewModel: AlbumDetailViewModel = hiltViewModel<AlbumDetailViewModel, AlbumDetailViewModelFactory> {
        it.create(albumId)
    },
) {
    val state = viewModel.state
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    var album by remember { mutableStateOf<Album?>(null) }
    var albumReviews by remember { mutableStateOf<List<FullReview>?>(null) }
    var name by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var avgRating by remember { mutableDoubleStateOf(0.0) }
    var decodedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    var albumList by remember { mutableStateOf<AlbumList?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var rating by remember { mutableDoubleStateOf(0.0) }
    var review by remember { mutableStateOf("") }
    var playedButtonState by remember { mutableStateOf(false) }
    var listenListButtonState by remember { mutableStateOf(false) }
    var playedButtonBackground by remember { mutableStateOf<Color>(Color.Transparent) }
    var listenListButtonBackground by remember { mutableStateOf<Color>(Color.Transparent) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var basePlayedState by remember { mutableStateOf(false) }
    var baseListenListState by remember { mutableStateOf(false) }

    LaunchedEffect(state.album) {
        album = state.album

        album?.let {
            name = it.name
            author = it.author
            date = it.releaseDate.toString()

            it.image?.let { base64Image ->
                decodedImage = try {
                    val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)

                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
                } catch (e: Exception) {
                    null
                }
            } ?: run {
                decodedImage = null
            }
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

    LaunchedEffect(state.albumList) {
        albumList = state.albumList

        albumList?.let {
            if (it.rating != null) {
                rating = it.rating!!
            }

            if (it.review != null) {
                review = it.review!!
            }

            if (it.date != null) {
                selectedDate = it.date
            }

            if (it.type == "played") {
                basePlayedState = true
                playedButtonState = true
                playedButtonBackground = primaryColor
            } else {
                baseListenListState = true
                listenListButtonState = true
                listenListButtonBackground = primaryColor
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    state.error?.let {
        AlertDialog(onDismissRequest = { viewModel.clearError() }, confirmButton = {
            Button(onClick = {
                viewModel.clearError()
            }) {
                Text("Aceptar")
            }
        }, title = { Text("Error") }, text = {
            Column {
                Text("Se ha producido un error al contactar con el servidor, comprueba que dispones de conexión a Internet y que estás logeado")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Código de error: $it")
            }
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
                                .size(400.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.image_not_found),
                            contentDescription = "Image not found",
                            modifier = Modifier
                                .size(400.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(text = author, fontSize = 20.sp)
                        Text(text = date, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Rating: $avgRating / 5.0")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(AppScreens.AlbumReviewsScreen.route + "/" + albumId) },
                    modifier = Modifier.align(Alignment.Start).fillMaxWidth()
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
                            Box(
                                modifier = Modifier
                                    .height(110.dp)
                                    .width(150.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 50.dp,
                                            bottomEnd = 50.dp,
                                            bottomStart = 0.dp
                                        )
                                    )
                                    .background(color = playedButtonBackground)
                                    .clickable {
                                        if (listenListButtonState) {
                                            listenListButtonState = false
                                            listenListButtonBackground = Color.Transparent
                                        }

                                        playedButtonState = !playedButtonState

                                        playedButtonBackground = if (playedButtonState) {
                                            primaryColor
                                        } else {
                                            Color.Transparent
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.botonplayed),
                                    contentDescription = "Botón Played",
                                    modifier = Modifier.size(200.dp),
                                    tint = Color.Unspecified
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (playedButtonState) {
                                        playedButtonState = false
                                        playedButtonBackground = Color.Transparent
                                    }

                                    listenListButtonState = !listenListButtonState

                                    listenListButtonBackground = if (listenListButtonState) {
                                        primaryColor
                                    } else {
                                        Color.Transparent
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

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Fecha: ${selectedDate?.format(dateFormatter) ?: "Sin seleccionar"}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Button(
                            onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Seleccionar fecha")
                        }

                        Button(
                            onClick = {
                                showBottomSheet = false

                                if (baseListenListState) {
                                    if (!listenListButtonState) {
                                        viewModel.deleteListenList(albumId)
                                    }
                                }

                                if (!basePlayedState) {
                                    if (playedButtonState) {
                                        viewModel.addPlayed(
                                            PlayedListDTO(
                                                albumId, review, rating, selectedDate?.format(
                                                    dateFormatter
                                                )
                                            )
                                        )
                                    } else if (listenListButtonState) {
                                        viewModel.addListenList(ListenListDTO(albumId))
                                    }
                                } else {
                                    if (playedButtonState) {
                                        viewModel.updatePlayed(
                                            albumId, PlayedListDTO(
                                                albumId, review, rating, selectedDate?.format(
                                                    dateFormatter
                                                )
                                            )
                                        )
                                    } else {
                                        viewModel.deletePlayed(albumId)
                                        review = ""
                                        rating = 0.0
                                        selectedDate = null
                                    }
                                }
                            }, modifier = Modifier
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
    val primaryColor = MaterialTheme.colorScheme.primary
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Row(modifier = modifier) {
        for (i in 1..starCount) {
            val icon = when {
                i <= floor(rating) -> Icons.Default.Star
                i - 0.5 <= rating -> Icons.Default.StarHalf
                else -> Icons.Default.StarBorder
            }

            val isFilled = i <= ceil(rating)
            val tintColor = if (isFilled) primaryColor else borderColor

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
                tint = tintColor
            )
        }
    }
}