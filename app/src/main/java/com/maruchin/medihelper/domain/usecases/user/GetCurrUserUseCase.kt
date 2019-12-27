package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserRepo

class GetCurrUserUseCase(
    private val userRepo: UserRepo
) {
    suspend fun execute(): User {
        return userRepo.getCurrUserId()?.let { userId ->
            userRepo.getById(userId)
        } ?: throw Exception("Current userId is null")
    }
}