package com.maruchin.medihelper.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maruchin.medihelper.domain.entities.Person

@Entity(tableName = "persons")
data class PersonEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "person_id")
    val personId: Int,

    @ColumnInfo(name = "person_remote_id")
    var personRemoteId: Long?,

    @ColumnInfo(name = "person_name")
    var personName: String,

    @ColumnInfo(name = "person_color_res_id")
    var personColorResId: Int,

    @ColumnInfo(name = "main_person")
    var mainPerson: Boolean,

    @ColumnInfo(name = "connection_key")
    var connectionKey: String?,

    @ColumnInfo(name = "person_synchronized")
    var personSynchronized: Boolean
) {
    constructor(person: Person) : this(
        personId = 0,
        personRemoteId = null,
        personName = person.name,
        personColorResId = person.colorId,
        mainPerson = person.mainPerson,
        connectionKey = person.connectionKey,
        personSynchronized = false
    )

    fun update(person: Person) {
        personName = person.name
        personColorResId = person.colorId
        mainPerson = person.mainPerson
        connectionKey = person.connectionKey
        personSynchronized = false
    }

    fun toPerson() = Person(
        personId = personId,
        name = personName,
        colorId = personColorResId,
        mainPerson = mainPerson,
        connectionKey = connectionKey
    )
}