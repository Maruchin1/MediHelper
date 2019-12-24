package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.data.ProfileColor
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.repositories.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignUpUseCase(
    private val userRepo: UserRepo,
    private val profileRepo: ProfileRepo
) {
    suspend fun execute(email: String, password: String, userName: String): Boolean = withContext(Dispatchers.Default) {
        val userId = userRepo.signUp(email, password) ?: return@withContext false

        addUserEntity(userId, userName, email)
        addUserMainProfile(userName)

        return@withContext true
    }

    private suspend fun addUserEntity(userId: String, userName: String, email: String) {
        val newUser = User(
            entityId = userId,
            userName = userName,
            email = email
        )
        userRepo.addNew(newUser)
    }

    private suspend fun addUserMainProfile(userName: String) {
        val mainProfile = Profile(
            entityId = "",
            name = userName,
            color = ProfileColor.MAIN.colorString,
            mainPerson = true
        )
        profileRepo.addNew(mainProfile)
    }
}