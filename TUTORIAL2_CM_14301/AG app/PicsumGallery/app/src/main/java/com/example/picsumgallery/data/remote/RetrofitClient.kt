package com.example.picsumgallery.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object to provide and configure the Retrofit instance.
 */
object RetrofitClient {

    private const val BASE_URL = "https://picsum.photos/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * The [PicsumApiService] instance to be used for API calls.
     */
    val apiService: PicsumApiService by lazy {
        retrofit.create(PicsumApiService::class.java)
    }
}
