package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo

data class PersonItem(
    @ColumnInfo(name = "person_id")
    val personId: Int,

    @ColumnInfo(name = "person_name")
    val personName: String,

    @ColumnInfo(name = "person_color_res_id")
    val personColorResId: Int,

    @ColumnInfo(name = "main_person")
    var mainPerson: Boolean = false
)