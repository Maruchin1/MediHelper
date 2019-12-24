package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.repositories.UserRepo

class IsUserSignedInUseCase(
    private val userRepo: UserRepo
) {
    suspend fun execute(): Boolean {
        return userRepo.getCurrUserId() != null
    }
}