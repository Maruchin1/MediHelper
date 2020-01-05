package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.domain.model.PlanEditData
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.plans.GetPlanEditDataUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetPlanEditDataUseCaseImplTest {

    private val planRepo: PlanRepo = mock()

    private lateinit var useCase: GetPlanEditDataUseCase

    @Before
    fun before() {
        useCase = GetPlanEditDataUseCaseImpl(planRepo)
    }

    @Test
    fun execute_Correct() {
        val medicinePlanId = "abc"
        val medicinePlanMock = Plan(
            entityId = medicinePlanId,
            medicineId = "aaa",
            profileId = "bbb",
            planType = Plan.Type.ONE_DAY,
            startDate = AppDate(2019, 11, 13),
            endDate = null,
            intakeDays = null,
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val expectedResult = PlanEditData(
            medicinePlanId = medicinePlanId,
            medicineId = medicinePlanMock.medicineId,
            profileId = medicinePlanMock.profileId,
            planType = medicinePlanMock.planType,
            startDate = medicinePlanMock.startDate,
            endDate = medicinePlanMock.endDate,
            intakeDays = medicinePlanMock.intakeDays,
            timeDoseList = medicinePlanMock.timeDoseList
        )

        runBlocking {
            Mockito.`when`(planRepo.getById(medicinePlanId)).thenReturn(medicinePlanMock)

            val result = useCase.execute(medicinePlanId)

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }

    @Test(expected = MedicinePlanNotFoundException::class)
    fun execute_MedicinePlanNotFound() {
        val medicinePlanId = "abc"

        runBlocking {
            Mockito.`when`(planRepo.getById(medicinePlanId)).thenReturn(null)

            useCase.execute(medicinePlanId)
        }
    }
}