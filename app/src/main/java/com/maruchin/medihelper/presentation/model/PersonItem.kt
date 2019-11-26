package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.Profile

data class PersonItem(
    val personId: Int,
    val name: String,
    val colorId: Int,
    val mainPerson: Boolean
) {
//    constructor(profile: Profile) : this(
//        personId = profile.profileId,
//        name = profile.name,
//        color = profile.color,
//        mainPerson = profile.mainPerson
//    )
}