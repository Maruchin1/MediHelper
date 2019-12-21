package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.domain.framework.BaseEntity


data class Medicine(
    override val entityId: String,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate,
    val packageSize: Float,
    var currState: Float,
    val pictureName: String?
) : BaseEntity() {
    val stateData: MedicineStateData?
        get() = MedicineStateData.get(packageSize, currState)

    fun reduceCurrState(doseSize: Float) {
        val currState = this.currState
        var newState = currState - doseSize
        if (newState < 0f) {
            newState = 0f
        }
        this.currState = newState
    }

    fun increaseCurrState(doseSize: Float) {
        val currState = this.currState
        val packageSize = this.packageSize
        var newState = currState + doseSize
        if (newState > packageSize) {
            newState = packageSize
        }
        this.currState = newState
    }
}