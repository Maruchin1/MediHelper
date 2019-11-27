package com.maruchin.medihelper.domain.usecases.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetAllProfilesItemsLiveUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(): LiveData<List<ProfileItem>> {
        val allEntitiesLive = profileRepo.getAllListLive()
        return Transformations.map(allEntitiesLive) { list ->
            list.map { mapToProfileItem(it) }
        }
    }

    private fun mapToProfileItem(profile: Profile) = ProfileItem(
        profileId = profile.profileId,
        name = profile.name,
        color = profile.color,
        mainPerson = profile.mainPerson
    )

    data class ProfileItem(
        val profileId: String,
        val name: String,
        val color: String,
        val mainPerson: Boolean
    )
}