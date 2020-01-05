package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.UserNotSignedInException
import com.maruchin.medihelper.domain.usecases.user.GetCurrUserEmailUseCase

class GetCurrUserEmailUseCaseImpl(
    private val userAuthRepo: UserAuthRepo
) : GetCurrUserEmailUseCase {

    override suspend fun execute(): String {
        val user = userAuthRepo.getCurrUser() ?: throw UserNotSignedInException()
        return user.email
    }
}