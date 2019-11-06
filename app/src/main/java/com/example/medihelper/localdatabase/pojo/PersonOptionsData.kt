package com.example.medihelper.localdatabase.pojo

import androidx.room.ColumnInfo

data class PersonOptionsData(
    @ColumnInfo(name = "person_id")
    val personID: Int,

    @ColumnInfo(name = "person_name")
    val personName: String,

    @ColumnInfo(name = "person_color_res_id")
    val personColorResID: Int,

    @ColumnInfo(name = "connection_key")
    var connectionKey: String? = null
)