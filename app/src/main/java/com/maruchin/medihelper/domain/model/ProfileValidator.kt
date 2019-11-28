package com.maruchin.medihelper.domain.model

class ProfileValidator(
    name: String?,
    color: String?
) {
    val noErrors: Boolean
        get() = arrayOf(
            emptyName,
            emptyColor
        ).all { !it }
    var emptyName: Boolean = false
        private set
    var emptyColor: Boolean = false
        private set

    init {
        if (name.isNullOrEmpty()) {
            emptyName = true
        }
        if (color.isNullOrEmpty()) {
            emptyColor = true
        }
    }
}