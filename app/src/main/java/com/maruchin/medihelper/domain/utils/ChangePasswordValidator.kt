package com.maruchin.medihelper.domain.utils

class ChangePasswordValidator {

    fun validate(params: Params): Errors {
        val errors = Errors()

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

    data class Errors(
        var globalMessage: String = "",
        var emptyPassword: Boolean = false,
        var emptyPasswordConfirm: Boolean = false,
        var passwordsNotTheSame: Boolean = false
    ) {
        val noErrors: Boolean
            get() = arrayOf(
                emptyPassword,
                emptyPasswordConfirm,
                passwordsNotTheSame
            ).all { !it } and globalMessage.isEmpty()
    }
}