package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.model.SignInErrors
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.utils.SignInValidator

class SignInUseCase(
    private val userAuthRepo: UserAuthRepo,
    private val validator: SignInValidator
) {
    suspend fun execute(params: Params): SignInErrors {
        val validatorParams = SignInValidator.Params(
            email = params.email,
            password = params.password
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            val userId = userAuthRepo.signIn(
                email = params.email!!,
                password = params.password!!,
                errors = errors
            )
        }
        return errors
    }

    data class Params(
        val email: String?,
        val password: String?
    )
}