package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.model.SignInErrors

interface SignInUseCase {

    suspend fun execute(params: Params): SignInErrors

    data class Params(
        val email: String?,
        val password: String?
    )
}