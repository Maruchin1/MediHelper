package com.maruchin.medihelper.domain.usecases.user

interface InitDefaultsUseCase {

    suspend fun execute(userName: String)
}