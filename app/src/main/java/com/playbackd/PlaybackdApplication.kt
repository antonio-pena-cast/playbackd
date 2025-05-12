package com.playbackd

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlaybackdApplication: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: PlaybackdApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

    }

    override fun onCreate() {
        super.onCreate()

        val context: Context = PlaybackdApplication.applicationContext()
    }
}