package com.maruchin.medihelper.domain.entities


data class Medicine(
    val medicineId: String,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate,
    val packageSize: Float?,
    var currState: Float?,
    val pictureName: String?
) {
    val stateData: MedicineStateData
        get() = MedicineStateData.get(packageSize, currState)

    fun reduceCurrState(doseSize: Float) {
        val currState = this.currState
        if (currState != null) {
            var newState = currState - doseSize
            if (newState < 0f) {
                newState = 0f
            }
            this.currState = newState
        }
    }

    fun increaseCurrState(doseSize: Float) {
        val currState = this.currState
        val packageSize = this.packageSize
        if (currState != null) {
            var newState = currState + doseSize
            if (packageSize != null && newState > packageSize) {
                newState = packageSize
            }
            this.currState = newState
        }
    }
}