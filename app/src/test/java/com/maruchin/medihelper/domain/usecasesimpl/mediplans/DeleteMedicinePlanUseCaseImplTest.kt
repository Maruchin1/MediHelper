package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteMedicinePlanUseCase
import com.maruchin.medihelper.testingframework.mock
import com.maruchin.medihelper.testingframework.verifyInvokes
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DeleteMedicinePlanUseCaseImplTest {

    private val medicinePlanRepo: MedicinePlanRepo = mock()
    private val plannedMedicineRepo: PlannedMedicineRepo = mock()
    private val deviceReminder: DeviceReminder = mock()

    private lateinit var useCase: DeleteMedicinePlanUseCase

    @Before
    fun before() {
        useCase = DeleteMedicinePlanUseCaseImpl(medicinePlanRepo, plannedMedicineRepo, deviceReminder)
    }

    @Test
    fun execute() {
        val medicinePlanId = "abc"
        val plannedMedicinesMock = listOf(
            PlannedMedicine(
                entityId = "aaa",
                medicinePlanId = "abc",
                profileId = "ppp",
                medicineId = "ooo",
                plannedDate = AppDate(2019, 11, 13),
                plannedTime = AppTime(9, 30),
                plannedDoseSize = 2f,
                status = PlannedMedicine.Status.NOT_TAKEN
            )
        )

        runBlocking {
            Mockito.`when`(plannedMedicineRepo.getListByMedicinePlan("abc")).thenReturn(plannedMedicinesMock)
            useCase.execute(medicinePlanId)

            verifyInvokes(medicinePlanRepo, numberOfInvocations = 1).deleteById(medicinePlanId)
            verifyInvokes(plannedMedicineRepo, numberOfInvocations = 1).deleteListById(listOf("aaa"))
            verifyInvokes(deviceReminder, numberOfInvocations = 1).cancelReminders(plannedMedicinesMock)
        }
    }
}