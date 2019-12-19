package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.Profile

data class ProfileDb(
    val name: String? = null,
    val color: String? = null,
    val mainPerson: Boolean? = null
) {

    constructor(profile: Profile) : this(
        name = profile.name,
        color = profile.color,
        mainPerson = profile.mainPerson
    )

    fun toEntity(id: String) = Profile(
        profileId = id,
        name = name!!,
        color = color!!,
        mainPerson = mainPerson!!
    )
}