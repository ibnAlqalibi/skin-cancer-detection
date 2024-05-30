package com.ibnAlqalibi.SimpleSkinCancerDetection.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnAlqalibi.SimpleSkinCancerDetection.databinding.ActivityHomeBinding
import com.ibnAlqalibi.SimpleSkinCancerDetection.responses.ArticlesItem
import com.ibnAlqalibi.SimpleSkinCancerDetection.utils.NewsAdapter
import com.ibnAlqalibi.SimpleSkinCancerDetection.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val newsViewModel by viewModels<NewsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newsViewModel.findNews(3)

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

        with(binding){
            rvLatestNews.layoutManager = LinearLayoutManager(this@HomeActivity)
            btnScan.setOnClickListener {
                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(intent)
            }
            btnHistory.setOnClickListener {
                val intent = Intent(this@HomeActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
            btnNews.setOnClickListener{
                val intent = Intent(this@HomeActivity, NewsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun setNewsData(usersList: List<ArticlesItem?>?) {
        val adapter = NewsAdapter(this@HomeActivity)
        adapter.submitList(usersList)
        binding.rvLatestNews.adapter = adapter
    }
}