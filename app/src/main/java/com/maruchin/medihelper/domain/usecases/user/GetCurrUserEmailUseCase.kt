package com.maruchin.medihelper.domain.usecases.user

interface GetCurrUserEmailUseCase {

    suspend fun execute(): String
}