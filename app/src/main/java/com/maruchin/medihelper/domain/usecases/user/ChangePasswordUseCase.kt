package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import com.maruchin.medihelper.domain.utils.ChangePasswordValidator

class ChangePasswordUseCase(
    private val userAuthRepo: UserAuthRepo,
    private val validator: ChangePasswordValidator
) {
    suspend fun execute(params: Params): ChangePasswordValidator.Errors {
        val validatorParams = ChangePasswordValidator.Params(
            password = params.newPassword,
            passwordConfirm = params.newPasswordConfirm
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            userAuthRepo.changePassword(params.newPassword!!)
        }
        return errors
    }

    data class Params(
        val newPassword: String?,
        val newPasswordConfirm: String?
    )
}