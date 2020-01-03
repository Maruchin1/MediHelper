package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileEditData
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import com.maruchin.medihelper.domain.usecases.profile.GetProfileEditDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetProfileEditDataUseCaseImpl(
    private val profileRepo: ProfileRepo
) : GetProfileEditDataUseCase {

    override suspend fun execute(profileId: String): ProfileEditData = withContext(Dispatchers.Default) {
        val profile = getProfile(profileId)
        return@withContext ProfileEditData(profile)
    }

    private suspend fun getProfile(profileId: String): Profile {
        return profileRepo.getById(profileId) ?: throw ProfileNotFoundException()
    }
}