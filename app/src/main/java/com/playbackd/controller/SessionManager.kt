package com.playbackd.controller

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")

class SessionManager(val context: Context) {
    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { settings ->
            settings[USER_TOKEN] = token
        }
    }

    fun getAuthToken(): String? {
        var token: String? = null
        val flow = context.dataStore.data.map { it[USER_TOKEN] }

        runBlocking {
            token = flow.first()
        }

        return token
    }
}