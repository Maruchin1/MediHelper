package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.model.ProfileErrors
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.SaveProfileUseCase
import com.maruchin.medihelper.domain.utils.ProfileValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveProfileUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val validator: ProfileValidator
) : SaveProfileUseCase {

    override suspend fun execute(params: SaveProfileUseCase.Params): ProfileErrors = withContext(Dispatchers.Default) {
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

    private suspend fun saveProfileToRepo(params: SaveProfileUseCase.Params) {
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
}