package com.maruchin.medihelper.domain.utils

class ProfileValidator {

    fun validate(params: Params): Errors {
        val errors = Errors()

        if (params.name.isNullOrEmpty()) {
            errors.emptyName = true
        }
        if (params.color.isNullOrEmpty()) {
            errors.emptyColor = true
        }
        return errors
    }

    data class Params(
        val name: String?,
        val color: String?
    )

    data class Errors(
        var emptyName: Boolean = false,
        var emptyColor: Boolean = false
    ) {
        val noErrors: Boolean
            get() = arrayOf(
                emptyName,
                emptyColor
            ).all { !it }
    }
}