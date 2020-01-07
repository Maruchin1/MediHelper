package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.usecases.user.IsUserSignedInUseCase

class IsUserSignedInUseCaseImpl(
    private val userRepo: UserRepo
) : IsUserSignedInUseCase {

    override suspend fun execute(): Boolean {
        return userRepo.getCurrUser() != null
    }
}