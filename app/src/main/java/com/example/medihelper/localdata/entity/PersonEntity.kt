package com.example.medihelper.localdata.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "person_id")
    val personId: Int = 0,

    @ColumnInfo(name = "person_remote_id")
    var personRemoteId: Long? = null,

    @ColumnInfo(name = "person_name")
    var personName: String,

    @ColumnInfo(name = "person_color_res_id")
    var personColorResId: Int,

    @ColumnInfo(name = "connection_key")
    var connectionKey: String? = null,

    @ColumnInfo(name = "synchronized_with_server")
    var synchronizedWithServer: Boolean = false
)