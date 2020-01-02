package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.entities.User

interface GetCurrUserUseCase {

    suspend fun execute(): User

    class UserNotSignedInException : Exception()
}