package com.ibnAlqalibi.SimpleSkinCancerDetection.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibnAlqalibi.SimpleSkinCancerDetection.database.ResultEntity
import com.ibnAlqalibi.SimpleSkinCancerDetection.databinding.ActivityHistoryBinding
import com.ibnAlqalibi.SimpleSkinCancerDetection.utils.HistoryAdapter
import com.ibnAlqalibi.SimpleSkinCancerDetection.viewmodels.HistoryViewModel
import com.ibnAlqalibi.SimpleSkinCancerDetection.viewmodels.HistoryViewModelFactory
import com.google.android.material.snackbar.Snackbar

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding){
            rvResults.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvResults.setHasFixedSize(true)
            topAppBar.setNavigationOnClickListener {
                finish()
            }
        }

        val resultsViewModel = obtainViewModel(this)
        resultsViewModel.getAllResults().observe(this){
            setListData(it)
            if (it.isEmpty()){
                Snackbar.make(
                    window.decorView.rootView,
                    "Tidak Ada Data",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setListData(favs: List<ResultEntity>?) {
        val adapter = HistoryAdapter(this@HistoryActivity)
        adapter.submitList(favs)
        binding.rvResults.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = HistoryViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }
}