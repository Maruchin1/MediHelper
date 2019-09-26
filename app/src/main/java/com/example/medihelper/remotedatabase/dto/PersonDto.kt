package com.example.medihelper.remotedatabase.dto

import com.example.medihelper.localdatabase.entities.PersonEntity
import com.google.gson.annotations.SerializedName

data class PersonDto(
    @SerializedName(value = "personLocalId")
    val personLocalId: Int?,

    @SerializedName(value = "personRemoteId")
    val personRemoteId: Long?,

    @SerializedName(value = "personName")
    val personName: String,

    @SerializedName(value = "personColorResId")
    val personColorResId: Int,

    @SerializedName(value = "mainPerson")
    val mainPerson: Boolean
) {
    fun toEntity() = PersonEntity(
        personID = personLocalId ?: 0,
        personRemoteID = personRemoteId,
        personName = personName,
        personColorResID = personColorResId,
        mainPerson = mainPerson,
        synchronizedWithServer = true
    )

    companion object {
        fun fromEntity(personEntity: PersonEntity) = PersonDto(
            personLocalId = personEntity.personID,
            personRemoteId = personEntity.personRemoteID,
            personName = personEntity.personName,
            personColorResId = personEntity.personColorResID,
            mainPerson = personEntity.mainPerson
        )
    }
}