package com.ibnAlqalibi.SimpleSkinCancerDetection.retrofit

import com.ibnAlqalibi.SimpleSkinCancerDetection.responses.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v2/top-headlines")
    fun getNews(
        @Query("q") query: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}