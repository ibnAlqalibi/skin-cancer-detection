package com.ibnAlqalibi.SimpleSkinCancerDetection.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ResultEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "imagename")
    var imagename: String?,

    @ColumnInfo(name = "label")
    var label: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "score")
    var score: Float
) : Parcelable
