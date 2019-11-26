package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo

class GetMedicineDetailsUseCase(
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo
) {

    suspend fun execute(medicineId: String): MedicineDetails? {
        return medicineRepo.getById(medicineId)?.let { medicine ->
            val profileList = profileRepo.getListByMedicine(medicineId)
            MedicineDetails(
                medicineId = medicine.medicineId,
                name = medicine.name,
                unit = medicine.unit,
                expireDate = medicine.expireDate,
                stateData = medicine.getStateData(),
                additionalInfo = medicine.additionalInfo,
                profileSimpleItemList = profileList.map {
                    ProfileSimpleItem(
                        profileId = it.profileId,
                        name = it.name,
                        color = it.color
                    )
                }
            )
        }
    }

    data class MedicineDetails(
        val medicineId: String,
        val name: String,
        val unit: String,
        val expireDate: AppExpireDate,
        val stateData: MedicineStateData,
        val additionalInfo: String?,
        val profileSimpleItemList: List<ProfileSimpleItem>
    )

    data class ProfileSimpleItem(
        val profileId: String,
        val name: String,
        val color: String
    )
}