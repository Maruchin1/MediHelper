package com.maruchin.medihelper.domain.usecasesimpl.users

import com.maruchin.medihelper.domain.model.ChangePasswordErrors
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.usecases.user.ChangePasswordUseCase
import com.maruchin.medihelper.domain.utils.ChangePasswordValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordUseCaseImpl(
    private val userAuthRepo: UserAuthRepo,
    private val validator: ChangePasswordValidator
) : ChangePasswordUseCase {

    override suspend fun execute(params: ChangePasswordUseCase.Params): ChangePasswordErrors =
        withContext(Dispatchers.Default) {
            val validatorParams = getValidatorParams(params)
            val errors = validator.validate(validatorParams)

            if (errors.noErrors) {
                userAuthRepo.changePassword(params.newPassword!!)
            }
            return@withContext errors
        }

    private fun getValidatorParams(params: ChangePasswordUseCase.Params): ChangePasswordValidator.Params {
        return ChangePasswordValidator.Params(
            password = params.newPassword,
            passwordConfirm = params.newPasswordConfirm
        )
    }
}