package com.hazesoft.giphyhaze.api

import com.hazesoft.giphyhaze.model.GiphyResponseModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Saurav
 * on 3/15/2022
 */
interface ApiInterface {

    @GET("trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int
    ): Response<GiphyResponseModel>

    @GET("search")
    suspend fun getSearchedGifs(
        @Query("api_key") apiKey: String,
        @Query("q") searchKeyword: String,
        @Query("limit") limit: Int
    ): Response<GiphyResponseModel>

    companion object {
        private const val BASE_URL = "https://api.giphy.com/v1/gifs/"

        fun create(): ApiInterface {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }

}