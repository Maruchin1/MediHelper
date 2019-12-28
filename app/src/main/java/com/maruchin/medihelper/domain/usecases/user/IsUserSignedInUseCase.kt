package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.repositories.UserAuthRepo

class IsUserSignedInUseCase(
    private val userAuthRepo: UserAuthRepo
) {
    suspend fun execute(): Boolean {
        return userAuthRepo.getCurrUser() != null
    }
}