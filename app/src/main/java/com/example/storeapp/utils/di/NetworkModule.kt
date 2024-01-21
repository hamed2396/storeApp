package com.example.storeapp.utils.di

import com.crazylegend.kotlinextensions.root.logError
import com.example.storeapp.BuildConfig
import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.data.stored.SessionManger
import com.example.storeapp.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideConnectionTimeout() = Constants.CONNECTION_TIME_OUT

    @Provides
    @Singleton
    @Named(Constants.PING_NAMED)
    fun providePingTimeout() = Constants.PING_TIME_OUT

    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    }

    @Provides
    @Singleton
    fun provideClient(
        @Named(Constants.PING_NAMED) ping: Long,
        connectionTimeout: Long,
        interceptor: HttpLoggingInterceptor,
        session: SessionManger
    ) =
        OkHttpClient.Builder().addInterceptor { chain ->
            val token = runBlocking {
                session.getToken().firstOrNull()
            }

            chain.proceed(chain.request().newBuilder().apply {
                addHeader(Constants.AUTHORIZATION, "${Constants.BEARER} $token")
                addHeader(Constants.ACCEPT, Constants.APPLICATION_JSON)
            }.build())
        }.apply {
            addInterceptor(interceptor)
        }
            .readTimeout(connectionTimeout, TimeUnit.SECONDS)
            .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .writeTimeout(connectionTimeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .pingInterval(ping, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, baseUrl: String, gson: Gson): ApiServices =
        Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiServices::class.java)
}