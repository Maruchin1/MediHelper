package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.utils.DateTimeCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineDetailsUseCaseImpl(
    private val medicineRepo: MedicineRepo,
    private val profileRepo: ProfileRepo,
    private val deviceCalendar: DeviceCalendar,
    private val dateTimeCalculator: DateTimeCalculator
) : GetMedicineDetailsUseCase {

    override suspend fun execute(medicineId: String): MedicineDetails = withContext(Dispatchers.Default) {
        val medicine = getMedicine(medicineId)
        val daysRemains = medicine.expireDate?.let { getDaysRemains(it) }
        val profilesWithMedicine = profileRepo.getListByMedicine(medicineId)
        return@withContext MedicineDetails(medicine, daysRemains, profilesWithMedicine)
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }

    private fun getDaysRemains(expireDate: AppExpireDate): Int {
        val currDate = deviceCalendar.getCurrDate()
        val daysRemains = dateTimeCalculator.calcDaysDiff(currDate, expireDate)
        return daysRemains
    }
}