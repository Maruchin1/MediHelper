package com.example.medihelper.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medihelper.domain.entities.Person

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

    @ColumnInfo(name = "main_person")
    var mainPerson: Boolean = false,

    @ColumnInfo(name = "connection_key")
    var connectionKey: String? = null,

    @ColumnInfo(name = "synchronized_with_server")
    var synchronizedWithServer: Boolean = false
) {
    constructor(person: Person, personRemoteId: Long?) : this(
        personId = person.personId,
        personRemoteId = personRemoteId,
        personName = person.name,
        personColorResId = person.colorId,
        mainPerson = person.mainPerson,
        connectionKey = person.connectionKey,
        synchronizedWithServer = false
    )

    fun toPerson() = Person(
        personId = personId,
        name = personName,
        colorId = personColorResId,
        mainPerson = mainPerson,
        connectionKey = connectionKey
    )
}