package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.framework.BaseErrors

data class SignUpErrors(
    var emptyEmail: Boolean = false,
    var emptyPassword: Boolean = false,
    var emptyPasswordConfirm: Boolean = false,
    var passwordsNotTheSame: Boolean = false,
    var emptyUserName: Boolean = false,
    var incorrectEmail: Boolean = false,
    var userAlreadyExists: Boolean = false,
    var weakPassword: Boolean = false,
    var undefinedError: Boolean = false
) :BaseErrors() {

    override val noErrors: Boolean
        get() = arrayOf(
            emptyEmail,
            emptyPassword,
            emptyPasswordConfirm,
            passwordsNotTheSame,
            emptyUserName,
            incorrectEmail,
            userAlreadyExists,
            weakPassword,
            undefinedError
        ).all { !it }
}