package com.example.pexelsapp.di

import android.content.Context
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.data.network.CacheInterceptor
import com.example.pexelsapp.data.network.ForceCacheInterceptor
import com.example.pexelsapp.data.network.NetworkHelper
import com.example.pexelsapp.data.network.PexelsApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("BASE_URL_V1")
    fun provideBaseUrl(): String = "https://api.pexels.com/v1/"

    @Provides
    @Singleton
    fun provideRetrofit(@Named("BASE_URL_V1") baseUrl: String, client: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): PexelsApiService = retrofit.create(
        PexelsApiService::class.java
    )

    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext context: Context) = NetworkHelper(context)

    @Provides
    @Singleton
    fun provideCacheInterceptor() = CacheInterceptor()

    @Provides
    @Singleton
    fun provideForceCacheInterceptor(networkHelper: NetworkHelper) = ForceCacheInterceptor(networkHelper)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext applicationContext: Context,
        cacheInterceptor: CacheInterceptor,
        forceCacheInterceptor: ForceCacheInterceptor
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .cache(Cache(File(applicationContext.cacheDir, AppConfig.getCacheSubDir()), 10L * 1024L * 1024L))
            .addNetworkInterceptor(cacheInterceptor)
            .addInterceptor(forceCacheInterceptor)
            .build()
    }
}

