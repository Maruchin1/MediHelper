package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Profile

data class ProfileSimpleItem(
    val profileId: String,
    val name: String,
    val color: String
) {
    constructor(profile: Profile) : this(
        profileId = profile.entityId,
        name = profile.name,
        color = profile.color
    )
}