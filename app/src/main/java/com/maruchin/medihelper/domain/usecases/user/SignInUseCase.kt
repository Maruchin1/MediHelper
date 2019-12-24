package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.utils.SignInValidator

class SignInUseCase(
    private val userRepo: UserRepo,
    private val validator: SignInValidator
) {
    suspend fun execute(params: Params): SignInValidator.Errors {
        val validatorParams = SignInValidator.Params(
            email = params.email,
            password = params.password
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            userRepo.signIn(
                email = params.email!!,
                password = params.password!!
            )
        }
        return errors
    }

    data class Params(
        val email: String?,
        val password: String?
    )
}