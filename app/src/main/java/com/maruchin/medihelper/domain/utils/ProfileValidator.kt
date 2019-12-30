package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.framework.BaseValidator
import com.maruchin.medihelper.domain.model.ProfileErrors

class ProfileValidator : BaseValidator<ProfileValidator.Params, ProfileErrors>() {

    override fun validate(params: Params): ProfileErrors {
        val errors = ProfileErrors()

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
}