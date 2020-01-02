package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.UserNotSignedInException
import com.maruchin.medihelper.domain.usecases.user.GetCurrUserUseCase

class GetCurrUserUseCaseImpl(
    private val userAuthRepo: UserAuthRepo
) : GetCurrUserUseCase {

    override suspend fun execute(): User {
        return userAuthRepo.getCurrUser() ?: throw UserNotSignedInException()
    }
}