package com.dicoding.asclepius.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.responses.ArticlesItem
import com.dicoding.asclepius.responses.NewsResponse
import com.dicoding.asclepius.retrofit.ApiConfig
import com.dicoding.asclepius.utils.Event
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

    private val _owner = MutableLiveData<String>()
    val owner: LiveData<String> = _owner

    init{
        findNews()
    }

    fun setOwner(newOwner: String) {
        _owner.value = newOwner
    }

    private fun findNews() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNews(QUERY, CAT, LAN, KEY)
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