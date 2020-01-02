package com.maruchin.medihelper.domain.usecasesimpl.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.GetLiveAllProfilesItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLiveAllProfilesItemsUseCaseImpl(
    private val profileRepo: ProfileRepo
) :GetLiveAllProfilesItemsUseCase {

    override suspend fun execute(): LiveData<List<ProfileItem>> = withContext(Dispatchers.Default) {
        val allEntitiesLive = profileRepo.getAllListLive()
        return@withContext Transformations.switchMap(allEntitiesLive) { list ->
            getLiveProfilesItems(list)
        }
    }

    private fun getLiveProfilesItems(profiles: List<Profile>): LiveData<List<ProfileItem>> {
        return liveData {
            val items = mapProfilesToItemsSortedByName(profiles)
            emit(items)
        }
    }

    private suspend fun mapProfilesToItemsSortedByName(profiles: List<Profile>): List<ProfileItem> = withContext(Dispatchers.Default) {
        return@withContext profiles.map { ProfileItem(it) }.sortedBy { it.name }
    }
}