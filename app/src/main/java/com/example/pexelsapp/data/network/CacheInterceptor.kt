package com.example.pexelsapp.data.network

import com.example.pexelsapp.common.AppConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(1, TimeUnit.HOURS)
            .build()
        return response.newBuilder()
            .header(AppConfig.getCacheControl(), cacheControl.toString())
            .build()
    }
}