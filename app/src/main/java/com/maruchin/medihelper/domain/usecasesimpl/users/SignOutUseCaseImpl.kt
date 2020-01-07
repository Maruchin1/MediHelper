package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase

class SignOutUseCaseImpl(
    private val userRepo: UserRepo
) : SignOutUseCase {

    override suspend fun execute() {
        userRepo.signOut()
    }
}