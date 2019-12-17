package com.maruchin.medihelper.domain.usecases.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetLiveAllProfilesItemsUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(): LiveData<List<ProfileItem>> {
        val allEntitiesLive = profileRepo.getAllListLive()
        return Transformations.map(allEntitiesLive) { list ->
            list.map { ProfileItem(it)}
        }
    }
}