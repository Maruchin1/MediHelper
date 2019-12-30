package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.framework.BaseValidator
import com.maruchin.medihelper.domain.model.SignInErrors

class SignInValidator : BaseValidator<SignInValidator.Params, SignInErrors>() {

    override fun validate(params: Params): SignInErrors {
        val errors = SignInErrors()

        if (params.email.isNullOrEmpty()) {
            errors.emptyEmail = true
        }
        if (params.password.isNullOrEmpty()) {
            errors.emptyPassword = true
        }
        return errors
    }

    data class Params(
        val email: String?,
        val password: String?
    )
}