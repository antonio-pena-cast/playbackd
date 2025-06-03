package com.playbackd.utilities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UrlProviderViewModel @Inject constructor(private val urlProvider: UrlProvider): ViewModel() {
    var currentUrl by mutableStateOf(urlProvider.baseUrl)
        private set

    fun updateUrl(newUrl: String) {
        urlProvider.baseUrl = newUrl
        currentUrl = newUrl
    }
}