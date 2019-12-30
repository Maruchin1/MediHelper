package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.framework.BaseValidator
import com.maruchin.medihelper.domain.model.MedicineErrors

class MedicineValidator : BaseValidator<MedicineValidator.Params, MedicineErrors>() {

    override fun validate(params: Params): MedicineErrors {
        val errors = MedicineErrors()

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
}