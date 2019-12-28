package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.repositories.UserAuthRepo

class SignOutUseCase(
    private val userAuthRepo: UserAuthRepo
) {
    suspend fun execute() {
        userAuthRepo.signOut()
    }
}