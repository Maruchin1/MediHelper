package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.framework.BaseErrors

data class ProfileErrors(
    var emptyName: Boolean = false,
    var emptyColor: Boolean = false
) : BaseErrors() {

    override val noErrors: Boolean
        get() = arrayOf(
            emptyName,
            emptyColor
        ).all { !it }
}