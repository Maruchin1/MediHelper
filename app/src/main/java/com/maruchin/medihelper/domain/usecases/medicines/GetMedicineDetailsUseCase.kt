package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.utils.DateTimeCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineDetailsUseCase(
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo,
    private val deviceCalendar: DeviceCalendar,
    private val dateTimeCalculator: DateTimeCalculator
) {
    suspend fun execute(medicineId: String): MedicineDetails? = withContext(Dispatchers.Default) {
        return@withContext medicineRepo.getById(medicineId)?.let { medicine ->
            val currDate = deviceCalendar.getCurrDate()
            val daysRemains = dateTimeCalculator.calcDaysDiff(currDate, medicine.expireDate)
            val profileList = profileRepo.getListByMedicine(medicineId)
            return@let MedicineDetails(medicine, daysRemains, profileList)
        }
    }
}