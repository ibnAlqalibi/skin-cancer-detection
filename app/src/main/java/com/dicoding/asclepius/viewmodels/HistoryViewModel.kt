package com.dicoding.asclepius.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.ResultEntity
import com.dicoding.asclepius.repository.ResultsRepository

class HistoryViewModel(application: Application) : ViewModel() {
    private val mResultsRepository: ResultsRepository = ResultsRepository(application)

    fun insert(result: ResultEntity) {
        mResultsRepository.insert(result)
    }
    fun delete(result: ResultEntity) {
        mResultsRepository.delete(result)
    }
    fun getAllResults(): LiveData<List<ResultEntity>> = mResultsRepository.getAllResults()
    fun getResultByImageName(imageName: String?): LiveData<List<ResultEntity>> = mResultsRepository.getResultByImageName(imageName)
}