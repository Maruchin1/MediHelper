package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.framework.BaseErrors

data class MedicineErrors(
    var emptyName: Boolean = false,
    var emptyType: Boolean = false,
    var emptyUnit: Boolean = false,
    var currStateBiggerThanPackageSize: Boolean = false
) : BaseErrors() {

    override val noErrors: Boolean
        get() = arrayOf(
            emptyName,
            emptyType,
            emptyUnit,
            currStateBiggerThanPackageSize
        ).all { !it }
}