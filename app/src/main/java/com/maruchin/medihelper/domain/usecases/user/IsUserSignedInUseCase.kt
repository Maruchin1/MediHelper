package com.maruchin.medihelper.domain.usecases.user


interface IsUserSignedInUseCase {

    suspend fun execute(): Boolean
}