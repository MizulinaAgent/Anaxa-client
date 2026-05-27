package com.example.anaxa.di

import com.example.anaxa.data.remote.AuthInterceptor
import com.example.anaxa.data.remote.api.AuthApi
import com.example.anaxa.data.remote.api.GamesApi
import com.example.anaxa.data.remote.api.LotsApi
import com.example.anaxa.data.remote.api.MessagesApi
import com.example.anaxa.data.remote.api.OrdersApi
import com.example.anaxa.data.remote.api.ReviewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create()

    @Provides
    @Singleton
    fun provideGamesApi(retrofit: Retrofit): GamesApi = retrofit.create()

    @Provides
    @Singleton
    fun provideLotsApi(retrofit: Retrofit): LotsApi = retrofit.create()

    @Provides
    @Singleton
    fun provideOrdersApi(retrofit: Retrofit): OrdersApi = retrofit.create()

    @Provides
    @Singleton
    fun provideMessagesApi(retrofit: Retrofit): MessagesApi = retrofit.create()

    @Provides
    @Singleton
    fun provideReviewsApi(retrofit: Retrofit): ReviewsApi = retrofit.create()
}
