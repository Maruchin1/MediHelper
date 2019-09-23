package com.example.medihelper.remotedatabase.pojos

import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.google.gson.annotations.SerializedName
import java.io.File
import java.util.*

data class MedicineGetDto(
    @SerializedName(value = "medicineId")
    val medicineId: Long,

    @SerializedName(value = "medicineName")
    val medicineName: String,

    @SerializedName(value = "medicineUnit")
    val medicineUnit: String,

    @SerializedName(value = "expireDate")
    val expireDate: String?,

    @SerializedName(value = "packageSize")
    val packageSize: Float?,

    @SerializedName(value = "currState")
    val currState: Float?,

    @SerializedName(value = "additionalInfo")
    val additionalInfo: String?,

    @SerializedName(value = "image")
    val image: ByteArray?
) {
    fun toMedicineEntity(appFilesDir: File) = MedicineEntity(
        medicineRemoteID = medicineId,
        medicineName = medicineName,
        medicineUnit = medicineUnit,
        expireDate = expireDate?.let { AppDate(it) },
        packageSize = packageSize,
        currState = currState,
        additionalInfo = additionalInfo,
        imageName = image?.let { byteArray ->
            File(appFilesDir, Date().toString()).apply { writeBytes(byteArray) }.absolutePath
        }
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicineGetDto

        if (medicineId != other.medicineId) return false
        if (medicineName != other.medicineName) return false
        if (medicineUnit != other.medicineUnit) return false
        if (expireDate != other.expireDate) return false
        if (packageSize != other.packageSize) return false
        if (currState != other.currState) return false
        if (additionalInfo != other.additionalInfo) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = medicineId.hashCode()
        result = 31 * result + medicineName.hashCode()
        result = 31 * result + medicineUnit.hashCode()
        result = 31 * result + (expireDate?.hashCode() ?: 0)
        result = 31 * result + (packageSize?.hashCode() ?: 0)
        result = 31 * result + (currState?.hashCode() ?: 0)
        result = 31 * result + (additionalInfo?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}