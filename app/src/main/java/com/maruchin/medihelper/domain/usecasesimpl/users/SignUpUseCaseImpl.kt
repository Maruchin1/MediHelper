package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.model.SignUpErrors
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.user.SignUpUseCase
import com.maruchin.medihelper.domain.utils.SignUpValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignUpUseCaseImpl(
    private val userAuthRepo: UserAuthRepo,
    private val validator: SignUpValidator
) : SignUpUseCase {

    override suspend fun execute(params: SignUpUseCase.Params): SignUpErrors = withContext(Dispatchers.Default) {
        val validatorParams = getValidatorParams(params)
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            signUpUser(params, errors)
        }

        return@withContext errors
    }

    private fun getValidatorParams(params: SignUpUseCase.Params): SignUpValidator.Params {
        return SignUpValidator.Params(
            email = params.email,
            password = params.password,
            passwordConfirm = params.passwordConfirm,
            userName = params.userName
        )
    }

    private suspend fun signUpUser(params: SignUpUseCase.Params, errors: SignUpErrors) {
        try {
            userAuthRepo.signUp(params.email!!, params.password!!)
        } catch (ex: UserAuthRepo.IncorrectEmailException) {
            errors.incorrectEmail = true
        } catch (ex: UserAuthRepo.UserAlreadyExistException) {
            errors.userAlreadyExists = true
        } catch (ex: UserAuthRepo.WeakPasswordException) {
            errors.weakPassword = true
        } catch (ex: UserAuthRepo.UndefinedAuthException) {
            errors.undefinedError = true
        }
    }
}