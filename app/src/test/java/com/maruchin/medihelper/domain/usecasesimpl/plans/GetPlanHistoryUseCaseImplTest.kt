package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.plans.GetPlanHistoryUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetPlanHistoryUseCaseImplTest {

    private val plannedMedicineRepo: PlannedMedicineRepo = mock()

    private lateinit var useCase: GetPlanHistoryUseCase

    @Before
    fun before() {
        useCase = GetPlanHistoryUseCaseImpl(plannedMedicineRepo)
    }

    @Test
    fun execute() {
        val medicinePlanId = "abc"
        val plannedMedicinesMock = listOf(
            PlannedMedicine(
                entityId = "aaa",
                medicinePlanId = medicinePlanId,
                medicineId = "mmm",
                profileId = "ppp",
                plannedDate = AppDate(2019, 11, 13),
                plannedTime = AppTime(17, 0),
                plannedDoseSize = 2f,
                status = PlannedMedicine.Status.NOT_TAKEN
            ),
            PlannedMedicine(
                entityId = "bbb",
                medicinePlanId = medicinePlanId,
                medicineId = "mmm",
                profileId = "ppp",
                plannedDate = AppDate(2019, 11, 13),
                plannedTime = AppTime(9, 30),
                plannedDoseSize = 2f,
                status = PlannedMedicine.Status.NOT_TAKEN
            ),
            PlannedMedicine(
                entityId = "ccc",
                medicinePlanId = medicinePlanId,
                medicineId = "mmm",
                profileId = "ppp",
                plannedDate = AppDate(2019, 11, 7),
                plannedTime = AppTime(9, 30),
                plannedDoseSize = 2f,
                status = PlannedMedicine.Status.NOT_TAKEN
            )
        )

        val expectedResult = listOf(
            HistoryItem(
                date = AppDate(2019, 11, 7),
                checkBoxes = listOf(
                    HistoryItem.CheckBox(
                        plannedMedicineId = "ccc",
                        plannedTime = AppTime(9, 30),
                        status = PlannedMedicine.Status.NOT_TAKEN
                    )
                )
            ),
            HistoryItem(
                date = AppDate(2019, 11, 13),
                checkBoxes = listOf(
                    HistoryItem.CheckBox(
                        plannedMedicineId = "bbb",
                        plannedTime = AppTime(9, 30),
                        status = PlannedMedicine.Status.NOT_TAKEN
                    ),
                    HistoryItem.CheckBox(
                        plannedMedicineId = "aaa",
                        plannedTime = AppTime(17, 0),
                        status = PlannedMedicine.Status.NOT_TAKEN
                    )
                )
            )
        )

        runBlocking {
            Mockito.`when`(plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)).thenReturn(plannedMedicinesMock)

            val result = useCase.execute(medicinePlanId)

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }
}