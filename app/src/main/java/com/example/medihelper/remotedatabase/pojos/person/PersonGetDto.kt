package com.example.medihelper.remotedatabase.pojos.person

import com.example.medihelper.localdatabase.entities.PersonEntity
import com.google.gson.annotations.SerializedName

data class PersonGetDto(
    @SerializedName(value = "personRemoteId")
    val personRemoteId: Long,

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
        mainPerson = mainPerson
    )
}