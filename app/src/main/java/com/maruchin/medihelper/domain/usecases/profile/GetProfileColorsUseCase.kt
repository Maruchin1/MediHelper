package com.maruchin.medihelper.domain.usecases.profile


interface GetProfileColorsUseCase {

    suspend fun execute(): List<String>
}