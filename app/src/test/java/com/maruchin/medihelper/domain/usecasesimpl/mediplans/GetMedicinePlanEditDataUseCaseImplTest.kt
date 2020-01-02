package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.domain.model.MedicinePlanEditData
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanEditDataUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetMedicinePlanEditDataUseCaseImplTest {

    private val medicinePlanRepo: MedicinePlanRepo = mock()

    private lateinit var useCase: GetMedicinePlanEditDataUseCase

    @Before
    fun before() {
        useCase = GetMedicinePlanEditDataUseCaseImpl(medicinePlanRepo)
    }

    @Test
    fun execute_Correct() {
        val medicinePlanId = "abc"
        val medicinePlanMock = MedicinePlan(
            entityId = medicinePlanId,
            medicineId = "aaa",
            profileId = "bbb",
            planType = MedicinePlan.Type.ONCE,
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

        val expectedResult = MedicinePlanEditData(
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
            Mockito.`when`(medicinePlanRepo.getById(medicinePlanId)).thenReturn(medicinePlanMock)

            val result = useCase.execute(medicinePlanId)

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }

    @Test(expected = MedicinePlanNotFoundException::class)
    fun execute_MedicinePlanNotFound() {
        val medicinePlanId = "abc"

        runBlocking {
            Mockito.`when`(medicinePlanRepo.getById(medicinePlanId)).thenReturn(null)

            useCase.execute(medicinePlanId)
        }
    }
}