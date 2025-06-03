package com.playbackd.utilities

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UrlProviderModule {
    @Provides
    @Singleton
    fun provideUrlProvider(): UrlProvider {
        return UrlProvider()
    }
}