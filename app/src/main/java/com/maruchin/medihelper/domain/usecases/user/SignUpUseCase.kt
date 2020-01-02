package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.model.SignUpErrors

interface SignUpUseCase {

    suspend fun execute(params: Params): SignUpErrors

    data class Params(
        val email: String?,
        val password: String?,
        val passwordConfirm: String?,
        val userName: String?
    )

    class SignUpFailedException : Exception()
}