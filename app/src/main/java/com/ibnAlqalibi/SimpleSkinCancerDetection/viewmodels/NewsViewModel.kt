package com.ibnAlqalibi.SimpleSkinCancerDetection.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibnAlqalibi.SimpleSkinCancerDetection.BuildConfig
import com.ibnAlqalibi.SimpleSkinCancerDetection.responses.ArticlesItem
import com.ibnAlqalibi.SimpleSkinCancerDetection.responses.NewsResponse
import com.ibnAlqalibi.SimpleSkinCancerDetection.retrofit.ApiConfig
import com.ibnAlqalibi.SimpleSkinCancerDetection.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel: ViewModel() {
    private val _listNews = MutableLiveData<List<ArticlesItem?>?>()
    val listNews: LiveData<List<ArticlesItem?>?> = _listNews

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun findNews(size: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNews(QUERY, CAT, LAN, size, KEY)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.totalResults!! > 0) {
                    _listNews.value = response.body()?.articles
                } else {
                    _message.value = Event("Tidak Ditemukan")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "NewsViewModel"
        private const val QUERY = "cancer"
        private const val CAT = "health"
        private const val LAN = "en"
        private const val KEY = BuildConfig.API_KEY
    }
}