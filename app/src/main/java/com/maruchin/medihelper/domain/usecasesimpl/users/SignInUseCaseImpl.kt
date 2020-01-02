package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.model.SignInErrors
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.user.SignInUseCase
import com.maruchin.medihelper.domain.utils.SignInValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInUseCaseImpl(
    private val userAuthRepo: UserAuthRepo,
    private val validator: SignInValidator
) : SignInUseCase {

    override suspend fun execute(params: SignInUseCase.Params): SignInErrors = withContext(Dispatchers.Default) {
        val validatorParams = getValidatorParams(params)
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            signInUser(params, errors)
        }
        return@withContext errors
    }

    private fun getValidatorParams(params: SignInUseCase.Params): SignInValidator.Params {
        return SignInValidator.Params(
            email = params.email,
            password = params.password
        )
    }

    private suspend fun signInUser(params: SignInUseCase.Params, errors: SignInErrors) {
        userAuthRepo.signIn(
            email = params.email!!,
            password = params.password!!,
            errors = errors
        )
    }
}