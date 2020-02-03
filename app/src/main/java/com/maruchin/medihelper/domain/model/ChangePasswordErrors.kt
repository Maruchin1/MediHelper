package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.framework.BaseErrors

data class ChangePasswordErrors(
    var emptyPassword: Boolean = false,
    var emptyPasswordConfirm: Boolean = false,
    var passwordsNotTheSame: Boolean = false,
    var weakPassword: Boolean = false,
    var undefinedError: Boolean = false
) : BaseErrors() {

    override val noErrors: Boolean
        get() = arrayOf(
            emptyPassword,
            emptyPasswordConfirm,
            passwordsNotTheSame,
            weakPassword,
            undefinedError
        ).all { !it }
}