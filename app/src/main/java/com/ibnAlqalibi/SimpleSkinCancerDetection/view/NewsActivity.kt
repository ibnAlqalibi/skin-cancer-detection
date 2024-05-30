package com.ibnAlqalibi.SimpleSkinCancerDetection.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnAlqalibi.SimpleSkinCancerDetection.databinding.ActivityNewsBinding
import com.ibnAlqalibi.SimpleSkinCancerDetection.responses.ArticlesItem
import com.ibnAlqalibi.SimpleSkinCancerDetection.utils.NewsAdapter
import com.ibnAlqalibi.SimpleSkinCancerDetection.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val newsViewModel by viewModels<NewsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding){
            rvNews.layoutManager = LinearLayoutManager(this@NewsActivity)
            topAppBar.setNavigationOnClickListener {
                finish()
            }
        }

        newsViewModel.findNews(100)

        newsViewModel.listNews.observe(this) { it ->
            setNewsData(it)
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

    private fun setNewsData(usersList: List<ArticlesItem?>?) {
        val adapter = NewsAdapter(this@NewsActivity)
        adapter.submitList(usersList)
        binding.rvNews.adapter = adapter
    }
}