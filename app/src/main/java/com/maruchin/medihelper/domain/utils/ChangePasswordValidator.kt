package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.framework.BaseValidator
import com.maruchin.medihelper.domain.model.ChangePasswordErrors

class ChangePasswordValidator : BaseValidator<ChangePasswordValidator.Params, ChangePasswordErrors>() {

    override fun validate(params: Params): ChangePasswordErrors {
        val errors = ChangePasswordErrors()

        if (params.password.isNullOrEmpty()) {
            errors.emptyPassword = true
        }
        if (params.passwordConfirm.isNullOrEmpty()) {
            errors.emptyPasswordConfirm = true
        }
        if (!params.password.isNullOrEmpty() &&
            !params.passwordConfirm.isNullOrEmpty() &&
            params.password != params.passwordConfirm
        ) {
            errors.passwordsNotTheSame = true
        }
        return errors
    }

    data class Params(
        val password: String?,
        val passwordConfirm: String?
    )
}