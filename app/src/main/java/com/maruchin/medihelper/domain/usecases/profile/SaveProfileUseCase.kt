package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileErrors
import com.maruchin.medihelper.domain.utils.ProfileValidator
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveProfileUseCase(
    private val profileRepo: ProfileRepo,
    private val validator: ProfileValidator
) {
    suspend fun execute(params: Params): ProfileErrors = withContext(Dispatchers.Default) {
        val validatorParams = ProfileValidator.Params(
            name = params.name,
            color = params.color
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            saveProfileToRepo(params)
        }
        return@withContext errors
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