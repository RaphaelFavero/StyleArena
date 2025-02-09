package com.raphaelfavero.stylearena.core.di

import com.raphaelfavero.stylearena.BuildConfig
import com.raphaelfavero.stylearena.core.network.InMemorySessionManager
import com.raphaelfavero.stylearena.core.network.SessionManager
import com.raphaelfavero.stylearena.core.network.StyleArenaService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): StyleArenaService {
        return retrofit.create(StyleArenaService::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionManager(): SessionManager {
        return InMemorySessionManager()
    }
}
