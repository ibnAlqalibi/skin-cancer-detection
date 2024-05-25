package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.database.ResultEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.utils.HistoryAdapter
import com.dicoding.asclepius.viewmodels.HistoryViewModel
import com.dicoding.asclepius.viewmodels.HistoryViewModelFactory
import com.google.android.material.snackbar.Snackbar

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            rvResults.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvResults.setHasFixedSize(true)
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