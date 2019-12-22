package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetProfileItemUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(profileId: String): ProfileItem? = withContext(Dispatchers.Default) {
        return@withContext profileRepo.getById(profileId)?.let { profile ->
            ProfileItem(profile)
        }
    }
}