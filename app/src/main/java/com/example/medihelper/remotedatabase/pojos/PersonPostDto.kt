package com.example.medihelper.remotedatabase.pojos

import com.example.medihelper.localdatabase.entities.PersonEntity
import com.google.gson.annotations.SerializedName

data class PersonPostDto(
    @SerializedName(value = "personLocalId")
    val personLocalId: Int,

    @SerializedName(value = "personName")
    val personName: String,

    @SerializedName(value = "personColorResId")
    val personColorResId: Int,

    @SerializedName(value = "mainPerson")
    val mainPerson: Boolean,

    @SerializedName(value = "operationTime")
    val operationTime: String
) {
    constructor(personEntity: PersonEntity, operationTime: String) : this(
        personLocalId = personEntity.personID,
        personName = personEntity.personName,
        personColorResId = personEntity.personColorResID,
        mainPerson = personEntity.mainPerson,
        operationTime = operationTime
    )
}