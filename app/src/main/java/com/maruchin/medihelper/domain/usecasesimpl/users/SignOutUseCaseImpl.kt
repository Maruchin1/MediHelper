package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase

class SignOutUseCaseImpl(
    private val userAuthRepo: UserAuthRepo
) : SignOutUseCase {

    override suspend fun execute() {
        userAuthRepo.signOut()
    }
}