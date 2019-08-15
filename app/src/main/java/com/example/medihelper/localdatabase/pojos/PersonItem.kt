package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import com.example.medihelper.localdatabase.entities.PersonEntity

data class PersonItem(
    @ColumnInfo(name = "person_id")
    val personID: Int,

    @ColumnInfo(name = "person_name")
    val personName: String,

    @ColumnInfo(name = "person_color_res_id")
    val personColorResID: Int
)