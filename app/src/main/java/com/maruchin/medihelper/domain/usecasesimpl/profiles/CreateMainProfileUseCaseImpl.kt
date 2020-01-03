package com.maruchin.medihelper.domain.usecasesimpl.profiles

import com.maruchin.medihelper.data.utils.ProfileColor
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.profile.CreateMainProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateMainProfileUseCaseImpl(
    private val profileRepo: ProfileRepo
) : CreateMainProfileUseCase {

    override suspend fun execute(profileName: String) = withContext(Dispatchers.Default) {
        val mainProfile = getMainProfile(profileName)
        profileRepo.addNew(mainProfile)
        return@withContext
    }

    private fun getMainProfile(profileName: String): Profile {
        return Profile(
            entityId = "",
            name = profileName,
            color = ProfileColor.MAIN.colorString,
            mainPerson = true
        )
    }
}