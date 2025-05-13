package com.playbackd

import android.app.Application
import android.content.Context
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import com.playbackd.controller.SessionManager
import com.playbackd.converter.DateConverter
import com.playbackd.data.api.PlaybackdAPI
import com.playbackd.data.repositories.AlbumRepository
import com.playbackd.data.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Provider
import javax.inject.Singleton

val environment = PlaybackdComposeEnvironment.Local
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")

@Module
@InstallIn(SingletonComponent::class)
object EventsAPIComposeModule {
    @RequiresApi(VERSION_CODES.O)
    @Provides
    @Singleton
    fun providesEventAPI(provider: Provider<SessionManager>): PlaybackdAPI {
        val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, DateConverter()).create()

        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            provider.get().getAuthToken()?.let {
                val newRequest =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $it").build()
                chain.proceed(newRequest)
            } ?: chain.proceed(chain.request())
        }.build()

        return Retrofit.Builder().baseUrl(environment.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build().create(PlaybackdAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesSessionManager(application: Application): SessionManager {
        return SessionManager(application)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(playbackdAPI: PlaybackdAPI, sessionManager: SessionManager): AuthRepository {
        return AuthRepository(playbackdAPI, sessionManager)
    }

    @Provides
    @Singleton
    fun providesAlbumRepository(playbackdAPI: PlaybackdAPI): AlbumRepository {
        return AlbumRepository(playbackdAPI)
    }
}
