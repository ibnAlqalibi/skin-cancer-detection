package com.dicoding.asclepius.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ResultEntity::class], version = 1)
abstract class ResultsRoomDatabase : RoomDatabase() {
    abstract fun resultsDao(): ResultsDao

    companion object {
        @Volatile
        private var INSTANCE: ResultsRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): ResultsRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ResultsRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ResultsRoomDatabase::class.java, "result_database")
                        .build()
                }
            }
            return INSTANCE as ResultsRoomDatabase
        }
    }
}