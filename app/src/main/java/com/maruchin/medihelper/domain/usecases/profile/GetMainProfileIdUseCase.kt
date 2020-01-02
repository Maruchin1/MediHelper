package com.maruchin.medihelper.domain.usecases.profile

import com.maruchin.medihelper.domain.usecases.MainProfileNotFoundException


interface GetMainProfileIdUseCase {

    @Throws(MainProfileNotFoundException::class)
    suspend fun execute(): String?
}