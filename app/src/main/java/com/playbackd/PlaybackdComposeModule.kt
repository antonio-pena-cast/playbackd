package com.playbackd

import android.app.Application
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.playbackd.controller.SessionManager
import com.playbackd.converter.DateConverter
import com.playbackd.data.api.PlaybackdAPI
import com.playbackd.data.repositories.AlbumRepository
import com.playbackd.data.repositories.AuthRepository
import com.playbackd.data.repositories.ListRepository
import com.playbackd.data.repositories.ReviewRepository
import com.playbackd.data.repositories.UserRepository
import com.playbackd.utilities.UrlProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Provider
import javax.inject.Singleton

val environment = PlaybackdComposeEnvironment

@Module
@InstallIn(SingletonComponent::class)
object EventsAPIComposeModule {
    @RequiresApi(VERSION_CODES.O)
    @Provides
    fun providesEventAPI(provider: Provider<SessionManager>, urlProvider: UrlProvider): PlaybackdAPI {
        val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, DateConverter()).create()

        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            provider.get().getAuthToken()?.let {
                val newRequest =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $it").build()
                chain.proceed(newRequest)
            } ?: chain.proceed(chain.request())
        }.build()

        return Retrofit.Builder().baseUrl(urlProvider.baseUrl)
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
    fun providesAuthRepository(playbackdAPI: PlaybackdAPI, sessionManager: SessionManager): AuthRepository {
        return AuthRepository(playbackdAPI, sessionManager)
    }

    @Provides
    fun providesAlbumRepository(playbackdAPI: PlaybackdAPI): AlbumRepository {
        return AlbumRepository(playbackdAPI)
    }

    @Provides
    @Singleton
    fun providesReviewRepository(playbackdAPI: PlaybackdAPI): ReviewRepository {
        return ReviewRepository(playbackdAPI)
    }

    @Provides
    @Singleton
    fun providesListRepository(playbackdAPI: PlaybackdAPI): ListRepository {
        return ListRepository(playbackdAPI)
    }

    @Provides
    @Singleton
    fun providesUserRepository(playbackdAPI: PlaybackdAPI, sessionManager: SessionManager): UserRepository {
        return UserRepository(playbackdAPI, sessionManager)
    }
}
