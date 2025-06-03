package com.playbackd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.playbackd.controller.SessionManager
import com.playbackd.screens.MainScreen
import com.playbackd.ui.theme.PlaybackdTheme
import com.playbackd.utilities.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme

            PlaybackdTheme(darkTheme = isDarkTheme) {
                val context = LocalContext.current
                val sessionManager = remember { SessionManager(context) }

                Column {
                    MainScreen(rememberNavController(), sessionManager, themeViewModel)
                }
            }
        }
    }
}