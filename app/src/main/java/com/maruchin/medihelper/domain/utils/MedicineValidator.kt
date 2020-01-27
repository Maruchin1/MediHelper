package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.framework.BaseValidator
import com.maruchin.medihelper.domain.model.MedicineErrors

class MedicineValidator : BaseValidator<MedicineValidator.Params, MedicineErrors>() {

    override fun validate(params: Params): MedicineErrors {
        val errors = MedicineErrors()

        if (params.name.isNullOrEmpty()) {
            errors.emptyName = true
        }
        if (params.type.isNullOrEmpty()) {
            errors.emptyType = true
        }
        if (params.unit.isNullOrEmpty()) {
            errors.emptyUnit = true
        }
        if (params.packageSize != null && params.currState != null && params.currState > params.packageSize) {
            errors.currStateBiggerThanPackageSize = true
        }
        return errors
    }

    data class Params(
        val name: String?,
        val type: String?,
        val unit: String?,
        val packageSize: Float?,
        val currState: Float?
    )
}