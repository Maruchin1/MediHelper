package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.data.ProfileColor
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.repositories.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateUserUseCase (
    private val userRepo: UserRepo,
    private val profileRepo: ProfileRepo
) {

    suspend fun execute(params: Params) = withContext(Dispatchers.Default) {
        val newUser = User(
            entityId = params.userId,
            userName = params.userName ?: "--",
            email = params.email ?: "--"
        )
        userRepo.addNew(newUser)

        val mainProfile = Profile(
            entityId = "",
            name = "User",
            color = ProfileColor.MAIN.colorString,
            mainPerson = true
        )
        profileRepo.addNew(mainProfile)
        return@withContext
    }

    data class Params(
        val userId: String,
        val userName: String?,
        val email: String?
    )
}