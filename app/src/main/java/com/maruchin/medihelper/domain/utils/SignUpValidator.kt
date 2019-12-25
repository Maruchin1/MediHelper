package com.maruchin.medihelper.domain.utils

class SignUpValidator {

    fun validate(params: Params): Errors {
        val errors = Errors()

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

    data class Errors(
        var emptyEmail: Boolean = false,
        var emptyPassword: Boolean = false,
        var emptyPasswordConfirm: Boolean = false,
        var passwordsNotTheSame: Boolean = false,
        var emptyUserName: Boolean = false
    ) {
        val noErrors: Boolean
            get() = arrayOf(
                emptyEmail,
                emptyPassword,
                emptyPasswordConfirm,
                passwordsNotTheSame,
                emptyUserName
            ).all { !it }
    }
}