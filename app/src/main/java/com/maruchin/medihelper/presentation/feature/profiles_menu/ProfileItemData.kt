package com.maruchin.medihelper.presentation.feature.profiles_menu

import com.maruchin.medihelper.domain.model.ProfileItem

data class ProfileItemData(
    val profileId: String,
    val name: String,
    val color: String,
    val deletable: Boolean
) {

    companion object {

        fun fromDomainModel(model: ProfileItem): ProfileItemData {
            return ProfileItemData(
                profileId = model.profileId,
                name = model.name,
                color = model.color,
                deletable = !model.mainPerson
            )
        }
    }
}