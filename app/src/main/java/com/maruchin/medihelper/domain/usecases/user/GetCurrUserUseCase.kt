package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.usecases.UserNotSignedInException

interface GetCurrUserUseCase {

    @Throws(UserNotSignedInException::class)
    suspend fun execute(): User
}