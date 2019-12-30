package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.framework.BaseErrors

data class SignInErrors(
    var emptyEmail: Boolean = false,
    var emptyPassword: Boolean = false,
    var incorrectEmail: Boolean = false,
    var incorrectPassword: Boolean = false,
    var undefinedError: Boolean = false
) : BaseErrors() {

    override val noErrors: Boolean
        get() = arrayOf(
            emptyEmail,
            emptyPassword,
            incorrectEmail,
            incorrectPassword,
            undefinedError
        ).all { !it }
}