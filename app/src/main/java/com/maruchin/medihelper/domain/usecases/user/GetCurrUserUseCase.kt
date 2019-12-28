package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserAuthRepo

class GetCurrUserUseCase(
    private val userAuthRepo: UserAuthRepo
) {
    suspend fun execute(): User {
        return userAuthRepo.getCurrUser() ?: throw Exception("Curr user is null")
    }
}