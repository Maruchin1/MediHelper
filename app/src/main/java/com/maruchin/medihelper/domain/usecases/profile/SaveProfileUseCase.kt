package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileValidator
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveProfileUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(params: Params): ProfileValidator = withContext(Dispatchers.Default) {
        val validator = ProfileValidator(
            name = params.name,
            color = params.color
        )
        if (validator.noErrors) {
            saveProfileToRepo(params)
        }
        return@withContext validator
    }

    private suspend fun saveProfileToRepo(params: Params) {
        val profile = Profile(
            entityId = params.profileId ?: "",
            name = params.name!!,
            color = params.color!!,
            mainPerson = false
        )
        if (params.profileId == null) {
            profileRepo.addNew(profile)
        } else {
            profileRepo.update(profile)
        }
    }

    data class Params(
        val profileId: String?,
        val name: String?,
        val color: String?
    )
}