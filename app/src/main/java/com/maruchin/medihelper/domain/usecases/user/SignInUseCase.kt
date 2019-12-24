package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.repositories.UserRepo

class SignInUseCase(
    private val userRepo: UserRepo
) {
    suspend fun execute(email: String, password: String): Boolean {
        val userId = userRepo.signIn(email, password)
        return userId != null
    }
}