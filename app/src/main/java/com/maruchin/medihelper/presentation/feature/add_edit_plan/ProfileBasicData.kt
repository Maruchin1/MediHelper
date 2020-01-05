package com.maruchin.medihelper.presentation.feature.add_edit_plan

import com.maruchin.medihelper.domain.model.ProfileSimpleItem

data class ProfileBasicData(
    val profileId: String,
    val name: String,
    val color: String
) {
    companion object {

        fun fromDomainModel(model: ProfileSimpleItem) =
            ProfileBasicData(
                profileId = model.profileId,
                name = model.name,
                color = model.color
            )
    }
}