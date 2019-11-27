package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppExpireDate

class MedicineValidator(
    name: String?,
    unit: String?,
    expireDate: AppExpireDate?,
    packageSize: Float?,
    currState: Float?
) {
    val noErrors: Boolean
        get() = arrayOf(
            emptyName,
            emptyUnit,
            emptyExpireDate,
            currStateBiggerThanPackageSize
        ).all { !it }
    var emptyName: Boolean = false
        private set
    var emptyUnit: Boolean = false
        private set
    var emptyExpireDate: Boolean = false
        private set
    var currStateBiggerThanPackageSize: Boolean = false
        private set

    init {
        if (name.isNullOrEmpty()) {
            emptyName = true
        }
        if (unit.isNullOrEmpty()) {
            emptyUnit = true
        }
        if (expireDate == null) {
            emptyExpireDate = true
        }
        if (packageSize != null && currState != null && currState > packageSize) {
            currStateBiggerThanPackageSize = true
        }
    }
}