package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.framework.BaseValidator
import com.maruchin.medihelper.domain.model.SignUpErrors

class SignUpValidator : BaseValidator<SignUpValidator.Params, SignUpErrors>() {

    override fun validate(params: Params): SignUpErrors {
        val errors = SignUpErrors()

        if (params.email.isNullOrEmpty()) {
            errors.emptyEmail = true
        }
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
        if (params.userName.isNullOrEmpty()) {
            errors.emptyUserName = true
        }
        return errors
    }

    data class Params(
        val email: String?,
        val password: String?,
        val passwordConfirm: String?,
        val userName: String?
    )
}