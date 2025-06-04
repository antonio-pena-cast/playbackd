package com.playbackd.screens.home

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.playbackd.R
import com.playbackd.model.Album
import com.playbackd.navigation.AppScreens
import com.playbackd.utilities.UrlProviderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onReload: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    urlViewModel: UrlProviderViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val search = remember { mutableStateOf("") }
    val list = remember { mutableStateListOf<Album>() }
    val listFiltered = remember { mutableStateListOf<Album>() }

    LaunchedEffect(state.albums) {
        if (state.albums != null) {
            list.clear()
            listFiltered.clear()
            list.addAll(state.albums)
            listFiltered.addAll(state.albums)
        }
    }

    LaunchedEffect(search.value) {
        listFiltered.clear()
        listFiltered.addAll(list.filter {
            it.name.contains(search.value, ignoreCase = true)
        })
    }

    state.error?.let {
        var showDialog by remember { mutableStateOf(true) }
        var newUrl by remember { mutableStateOf(urlViewModel.currentUrl) }

        if (showDialog) {
            AlertDialog(onDismissRequest = {
                showDialog = false
                viewModel.clearError()
            }, confirmButton = {
                Button(onClick = {
                    if (newUrl.isNotBlank()) {
                        urlViewModel.updateUrl(newUrl)
                    }
                    showDialog = false
                    viewModel.clearError()
                    onReload()
                }) {
                    Text("Aceptar")
                }
            }, dismissButton = {
                Button(onClick = {
                    showDialog = false
                    viewModel.clearError()
                }) {
                    Text("Cancelar")
                }
            }, title = { Text("Error de conexión") }, text = {
                Column {
                    Text("No se pudo conectar al servidor.")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newUrl,
                                      onValueChange = { newUrl = it },
                                      label = { Text("Nueva URL base") },
                                      singleLine = true
                    )
                }
            })
        }
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
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            SearchAlbum(search)

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
                                      modifier = Modifier.fillMaxSize(),
                                      horizontalArrangement = Arrangement.spacedBy(10.dp),
                                      verticalItemSpacing = 10.dp,
                                      content = {
                                          items(listFiltered) { item ->
                                              AlbumItem(navController, item)
                                          }
                                      })
        }
    }
}

@Composable
fun SearchAlbum(search: MutableState<String>) {
    Column(
        Modifier
            .padding(20.dp, 5.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(value = search.value,
                              onValueChange = { search.value = it },
                              label = { Text("Busca por nombre...") },
                              keyboardOptions = KeyboardOptions(
                                  keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
                              ),
                              keyboardActions = KeyboardActions(onSearch = {}),
                              maxLines = 1,
                              singleLine = true,
                              modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AlbumItem(navController: NavController, album: Album) {
    var decodedImage: ImageBitmap? = null

    album.image?.let { base64Image ->
        try {
            val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            if (bitmap != null) {
                decodedImage = bitmap.asImageBitmap()
            }
        } catch (e: Exception) {
            decodedImage = null
        }
    }

    Card(
        onClick = {
            navController.navigate(AppScreens.AlbumDetailScreen.route + "/" + album.id)
        }, shape = CutCornerShape(0.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ), modifier = Modifier
            .padding(20.dp, 2.dp)
            .fillMaxWidth()
    ) {
        Column {
            if (decodedImage != null) {
                Image(
                    bitmap = decodedImage!!, contentDescription = "Album Image"
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.image_not_found),
                    contentDescription = "Album Image"
                )
            }
            Row {
                Text(album.author, fontSize = 12.sp)
            }
            Row {
                Text(album.name, fontWeight = FontWeight.Bold)
            }
        }
    }
}