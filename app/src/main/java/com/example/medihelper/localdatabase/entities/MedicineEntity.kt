package com.example.medihelper.localdatabase.entities

import androidx.room.*
import com.example.medihelper.AppDate
import com.example.medihelper.R

@Entity(tableName = "medicines")
data class MedicineEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicine_id")
    val medicineID: Int = 0,

    @ColumnInfo(name = "medicine_remote_id")
    var medicineRemoteID: Long? = null,

    @ColumnInfo(name = "medicine_name")
    var medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    var medicineUnit: String,

    @ColumnInfo(name = "expire_date")
    var expireDate: AppDate? = null,

    @ColumnInfo(name = "package_size")
    var packageSize: Float? = null,

    @ColumnInfo(name = "curr_state")
    var currState: Float? = null,

    @ColumnInfo(name = "additional_info")
    var additionalInfo: String? = null,

    @ColumnInfo(name = "image_name")
    var imageName: String?,

    @ColumnInfo(name = "synchronized_with_server")
    var synchronizedWithServer: Boolean = false
) {
    fun reduceCurrState(doseSize: Float) {
        currState?.let { currState ->
            this.currState = currState - doseSize
            if (this.currState!! < 0f) {
                this.currState = 0f
            }
        }
    }

    fun increaseCurrState(doseSize: Float) {
        currState?.let { currState ->
            this.currState = currState + doseSize
            if (packageSize != null && this.currState!! > packageSize!!) {
                this.currState = this.packageSize
            }
        }
    }

    class StateData(packageSize: Float?, currState: Float?) {
        val stateAvailable: Boolean
        val stateWeight: Float?
        val emptyWeight: Float?
        val colorId: Int?
        val text: String?
        val numberText: String?
        private val state = packageSize?.let { currState?.div(it) }

        init {
            stateAvailable = state != nulls
            stateWeight = state
            emptyWeight = state?.let { 1 - it }
            colorId = state?.let {
                when {
                    it >= STATE_GOOD_LIMIT -> R.color.colorStateGood
                    it > STATE_MEDIUM_LIMIT -> R.color.colorStateMedium
                    else -> R.color.colorStateSmall
                }
            }
            text = state?.let {
                when {
                    it >= STATE_GOOD_LIMIT -> TEXT_STATE_GOOD
                    it > STATE_MEDIUM_LIMIT -> TEXT_STATE_MEDIUM
                    else -> TEXT_STATE_SMALL
                }
            }
            numberText = "${currState}/${packageSize}"
        }

        companion object {
            private const val STATE_GOOD_LIMIT = 0.75f
            private const val STATE_MEDIUM_LIMIT = 0.4f
            private const val TEXT_STATE_GOOD = "Duży zapas"
            private const val TEXT_STATE_MEDIUM = "Średnia ilość"
            private const val TEXT_STATE_SMALL = "Blisko końca"
        }
    }
}