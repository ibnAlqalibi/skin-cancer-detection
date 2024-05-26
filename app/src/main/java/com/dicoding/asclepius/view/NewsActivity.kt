package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.responses.ArticlesItem
import com.dicoding.asclepius.utils.NewsAdapter
import com.dicoding.asclepius.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val newsViewModel by viewModels<NewsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            rvNews.layoutManager = LinearLayoutManager(this@NewsActivity)
        }

        newsViewModel.listNews.observe(this) { it ->
            setUsersData(it)
        }

        newsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        newsViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUsersData(usersList: List<ArticlesItem?>?) {
        val adapter = NewsAdapter(this@NewsActivity)
        adapter.submitList(usersList)
        binding.rvNews.adapter = adapter
    }
}