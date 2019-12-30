package com.maruchin.medihelper.domain.usecasesimpl.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
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
            liveData {
                withContext(Dispatchers.Default) {
                    val value = list.map { ProfileItem(it) }.sortedBy { it.name }
                    emit(value)
                }
            }
        }
    }
}