package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.user.IsUserSignedInUseCase

class IsUserSignedInUseCaseImpl(
    private val userAuthRepo: UserAuthRepo
) : IsUserSignedInUseCase {

    override suspend fun execute(): Boolean {
        return userAuthRepo.getCurrUser() != null
    }
}