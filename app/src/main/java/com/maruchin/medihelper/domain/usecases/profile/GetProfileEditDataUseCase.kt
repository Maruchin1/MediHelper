package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.model.ProfileEditData
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetProfileEditDataUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(profileId: String): ProfileEditData? = withContext(Dispatchers.Default) {
        val profile = profileRepo.getById(profileId)
        return@withContext profile?.let { ProfileEditData(it) }
    }
}