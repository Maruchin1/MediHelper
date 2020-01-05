package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.plans.DeleteSinglePlanUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.DeletePlannedMedicinesUseCase
import com.maruchin.medihelper.testingframework.mock
import com.maruchin.medihelper.testingframework.verifyInvocations
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class DeleteSinglePlanUseCaseImplTest {

    private val planRepo: PlanRepo = mock()
    private val plannedMedicineRepo: PlannedMedicineRepo = mock()
    private val deletePlannedMedicinesUseCase: DeletePlannedMedicinesUseCase = mock()

    private lateinit var useCaseSingle: DeleteSinglePlanUseCase

    @Before
    fun before() {
        useCaseSingle = DeleteSinglePlanUseCaseImpl(
            planRepo,
            plannedMedicineRepo,
            deletePlannedMedicinesUseCase
        )
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
            useCaseSingle.execute(medicinePlanId)

            verifyInvocations(planRepo, invocations = 1).deleteById(medicinePlanId)
            verifyInvocations(deletePlannedMedicinesUseCase, invocations = 1).execute(Mockito.anyList())
        }
    }
}