package com.maruchin.medihelper.domain.usecases.profile


interface GetMainProfileIdUseCase {

    suspend fun execute(): String?
}