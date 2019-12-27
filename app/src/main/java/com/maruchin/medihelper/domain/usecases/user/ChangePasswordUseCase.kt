package com.maruchin.medihelper.domain.usecases.user

import com.maruchin.medihelper.domain.repositories.UserRepo
import com.maruchin.medihelper.domain.utils.ChangePasswordValidator

class ChangePasswordUseCase(
    private val userRepo: UserRepo,
    private val validator: ChangePasswordValidator
) {
    suspend fun execute(params: Params): ChangePasswordValidator.Errors {
        val validatorParams = ChangePasswordValidator.Params(
            password = params.newPassword,
            passwordConfirm = params.newPasswordConfirm
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            userRepo.changePassword(params.newPassword!!)
        }
        return errors
    }

    data class Params(
        val newPassword: String?,
        val newPasswordConfirm: String?
    )
}