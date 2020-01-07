package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.usecases.UserNotSignedInException
import com.maruchin.medihelper.domain.usecases.user.GetCurrUserEmailUseCase

class GetCurrUserEmailUseCaseImpl(
    private val userRepo: UserRepo
) : GetCurrUserEmailUseCase {

    override suspend fun execute(): String {
        val user = userRepo.getCurrUser() ?: throw UserNotSignedInException()
        return user.email
    }
}