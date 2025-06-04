package com.playbackd.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.playbackd.model.Album
import com.playbackd.screens.home.AlbumItem
import com.playbackd.screens.home.SearchAlbum

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController: NavController,
    type: String,
    viewModel: ListViewModel = hiltViewModel<ListViewModel, ListViewModelFactory> {
        it.create(type)
    }
) {
    val state = viewModel.state
    val search = remember { mutableStateOf("") }
    val list = remember { mutableStateListOf<Album>() }
    val listFiltered = remember { mutableStateListOf<Album>() }

    LaunchedEffect(state.listenList) {
        if (state.listenList != null && state.listenList!!.isNotEmpty()) {
            list.clear()
            listFiltered.clear()
            list.addAll(state.listenList!!)
            listFiltered.addAll(state.listenList!!)
        }
    }

    LaunchedEffect(state.playedList) {
        if (state.playedList != null && state.playedList!!.isNotEmpty()) {
            list.clear()
            listFiltered.clear()
            list.addAll(state.playedList!!)
            listFiltered.addAll(state.playedList!!)
        }
    }

    LaunchedEffect(search.value) {
        listFiltered.clear()
        listFiltered.addAll(list.filter {
            it.name.contains(search.value, ignoreCase = true)
        })
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
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            SearchAlbum(search)

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
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