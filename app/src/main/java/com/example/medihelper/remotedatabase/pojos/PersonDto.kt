package com.example.medihelper.remotedatabase.pojos

import com.example.medihelper.localdatabase.entities.PersonEntity
import com.google.gson.annotations.SerializedName

data class PersonDto(
    @SerializedName(value = "personRemoteId")
    val personRemoteId: Long?,

    @SerializedName(value = "personName")
    val personName: String,

    @SerializedName(value = "personColorResId")
    val personColorResId: Int,

    @SerializedName(value = "mainPerson")
    val mainPerson: Boolean
) {
    fun toPersonEntity() = PersonEntity(
        personRemoteID = personRemoteId,
        personName = personName,
        personColorResID = personColorResId,
        mainPerson = mainPerson,
        synchronizedWithServer = true
    )

    companion object {
        fun fromPersonEntity(personEntity: PersonEntity) = PersonDto(
            personRemoteId = personEntity.personRemoteID,
            personName = personEntity.personName,
            personColorResId = personEntity.personColorResID,
            mainPerson = personEntity.mainPerson
        )
    }
}