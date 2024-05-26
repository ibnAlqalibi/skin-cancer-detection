package com.dicoding.asclepius.retrofit

import com.dicoding.asclepius.responses.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v2/everything")
    fun getNews(
        @Query("q") query: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}