package com.maruchin.medihelper.domain.usecases.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLiveAllProfilesItemsUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(): LiveData<List<ProfileItem>> = withContext(Dispatchers.Default) {
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