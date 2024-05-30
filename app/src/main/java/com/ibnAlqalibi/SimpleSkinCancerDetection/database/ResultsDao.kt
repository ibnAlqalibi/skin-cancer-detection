package com.ibnAlqalibi.SimpleSkinCancerDetection.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ResultsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(result: ResultEntity)
    @Delete
    fun delete(result: ResultEntity)
    @Query("SELECT * from ResultEntity ORDER BY id ASC")
    fun getAllResults(): LiveData<List<ResultEntity>>
    @Query("SELECT * from ResultEntity where imagename = :imageName")
    fun getResultByImageName(imageName: String?): LiveData<List<ResultEntity>>
}