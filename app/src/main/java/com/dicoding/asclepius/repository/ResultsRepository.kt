package com.dicoding.asclepius.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.database.ResultEntity
import com.dicoding.asclepius.database.ResultsDao
import com.dicoding.asclepius.database.ResultsRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ResultsRepository (application: Application) {
    private val mResultsDao: ResultsDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = ResultsRoomDatabase.getDatabase(application)
        mResultsDao = db.resultsDao()
    }
    fun getAllResults(): LiveData<List<ResultEntity>> = mResultsDao.getAllResults()
    fun getResultByImageName(imageName: String?): LiveData<List<ResultEntity>> = mResultsDao.getResultByImageName(imageName)
    fun insert(result: ResultEntity) {
        executorService.execute { mResultsDao.insert(result) }
    }
    fun delete(result: ResultEntity) {
        executorService.execute { mResultsDao.delete(result) }
    }
}