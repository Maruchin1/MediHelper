package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.entities.AppExpireDate

class MedicineValidator {

    fun validate(params: Params): Errors {
        val errors = Errors()

        if (params.name.isNullOrEmpty()) {
            errors.emptyName = true
        }
        if (params.unit.isNullOrEmpty()) {
            errors.emptyUnit = true
        }
        if (params.expireDate == null) {
            errors.emptyExpireDate = true
        }
        if (params.packageSize != null && params.currState != null && params.currState > params.packageSize) {
            errors.currStateBiggerThanPackageSize = true
        }
        return errors
    }

    data class Params(
        val name: String?,
        val unit: String?,
        val expireDate: AppExpireDate?,
        val packageSize: Float?,
        val currState: Float?
    )

    data class Errors(
        var emptyName: Boolean = false,
        var emptyUnit: Boolean = false,
        var emptyExpireDate: Boolean = false,
        var currStateBiggerThanPackageSize: Boolean = false
    ) {
        val noErrors: Boolean
            get() = arrayOf(
                emptyName,
                emptyUnit,
                emptyExpireDate,
                currStateBiggerThanPackageSize
            ).all { !it }
    }
}