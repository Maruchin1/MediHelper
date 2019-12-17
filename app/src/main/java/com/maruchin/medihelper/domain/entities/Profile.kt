package com.maruchin.medihelper.domain.entities

data class Profile(
    val profileId: String,
    val name: String,
    val color: String,
    val mainPerson: Boolean
)