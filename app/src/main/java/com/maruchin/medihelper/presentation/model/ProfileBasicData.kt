package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.model.ProfileSimpleItem

data class ProfileBasicData(
    val profileId: String,
    val name: String,
    val color: String
) {
    companion object {

        fun fromDomainModel(model: ProfileSimpleItem) = ProfileBasicData(
            profileId = model.profileId,
            name = model.name,
            color = model.color
        )
    }
}