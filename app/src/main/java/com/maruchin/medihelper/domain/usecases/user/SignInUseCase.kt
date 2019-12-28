package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.entities.AuthResult
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.utils.SignInValidator

class SignInUseCase(
    private val userAuthRepo: UserAuthRepo,
    private val validator: SignInValidator
) {
    suspend fun execute(params: Params): SignInValidator.Errors {
        val validatorParams = SignInValidator.Params(
            email = params.email,
            password = params.password
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            val authResult = userAuthRepo.signIn(
                email = params.email!!,
                password = params.password!!
            )
            if (authResult is AuthResult.Error) {
                errors.globalMessage = authResult.message
            }
        }
        return errors
    }

    data class Params(
        val email: String?,
        val password: String?
    )
}