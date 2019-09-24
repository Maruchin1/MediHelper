package com.example.medihelper.remotedatabase.pojos.person

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
    val mainPerson: Boolean
) {
    companion object {
        fun fromPersonEntity(personEntity: PersonEntity) = PersonPostDto(
            personLocalId = personEntity.personID,
            personName = personEntity.personName,
            personColorResId = personEntity.personColorResID,
            mainPerson = personEntity.mainPerson
        )
    }
}