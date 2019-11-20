package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.domain.entities.AppTime

data class TimeDoseEditData(
    @ColumnInfo( name = "time")
    var time: AppTime = AppTime(8, 0),

    @ColumnInfo(name = "dose_size")
    var doseSize: Float = 1.0f
)