package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Profile

data class ProfileItem(
    val profileId: String,
    val name: String,
    val color: String,
    val mainPerson: Boolean
) {
    constructor(profile: Profile) : this(
        profileId = profile.entityId,
        name = profile.name,
        color = profile.color,
        mainPerson = profile.mainPerson
    )
}