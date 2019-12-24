package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.repositories.UserRepo

class SignOutUseCase(
    private val userRepo: UserRepo
) {
    suspend fun execute() {
        userRepo.signOut()
    }
}