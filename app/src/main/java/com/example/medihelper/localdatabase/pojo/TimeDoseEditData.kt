package com.example.medihelper.localdatabase.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.localdatabase.AppTime

data class TimeDoseEditData(
    @ColumnInfo( name = "time")
    var time: AppTime = AppTime(8, 0),

    @ColumnInfo(name = "dose_size")
    var doseSize: Float = 1.0f
)