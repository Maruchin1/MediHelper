package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.model.PlanDetails
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException
import com.maruchin.medihelper.domain.usecases.plans.GetPlanDetailsUseCase
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetPlanDetailsUseCaseImplTest {

    private val planRepo: PlanRepo = mock()
    private val medicineRepo: MedicineRepo = mock()
    private val profileRepo: ProfileRepo = mock()

    private lateinit var useCase: GetPlanDetailsUseCase

    @Before
    fun before() {
        useCase = GetPlanDetailsUseCaseImpl(planRepo, medicineRepo, profileRepo)
    }

    @Test
    fun execute_Correct() {
        val medicinePlanId = "aaa"
        val medicineId = "bbb"
        val profileId = "ccc"
        val medicinePlanMock = Plan(
            entityId = medicinePlanId,
            medicineId = medicineId,
            profileId = profileId,
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
        val medicineMock = Medicine(
            entityId = medicineId,
            name = "Hitaxa",
            unit = "tabletki",
            expireDate = AppExpireDate(2020, 3),
            type = "Na katar",
            state = MedicineState(
                packageSize = 0f,
                currState = 0f
            ),
            pictureName = "test.jpg"
        )
        val profileMock = Profile(
            entityId = profileId,
            name = "Wojtek",
            color = "#000000",
            mainPerson = false
        )

        val expectedResult = PlanDetails(
            medicinePlanId = medicinePlanId,
            profileColor = profileMock.color,
            medicineId = medicineId,
            medicineName = medicineMock.name,
            medicineUnit = medicineMock.unit,
            planType = medicinePlanMock.planType,
            startDate = medicinePlanMock.startDate,
            endDate = medicinePlanMock.endDate,
            intakeDays = medicinePlanMock.intakeDays,
            timeDoseList = medicinePlanMock.timeDoseList
        )

        runBlocking {
            Mockito.`when`(planRepo.getById(medicinePlanId)).thenReturn(medicinePlanMock)
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(medicineMock)
            Mockito.`when`(profileRepo.getById(profileId)).thenReturn(profileMock)

            val result = useCase.execute(medicinePlanId)

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }

    @Test(expected = MedicinePlanNotFoundException::class)
    fun execute_MedicinePlanNotFound() {
        val medicinePlanId = "aaa"
        val medicineId = "bbb"
        val profileId = "ccc"

        val medicineMock = Medicine(
            entityId = medicineId,
            name = "Hitaxa",
            unit = "tabletki",
            expireDate = AppExpireDate(2020, 3),
            type = "Na katar",
            state = MedicineState(
                packageSize = 0f,
                currState = 0f
            ),
            pictureName = "test.jpg"
        )
        val profileMock = Profile(
            entityId = profileId,
            name = "Wojtek",
            color = "#000000",
            mainPerson = false
        )

        runBlocking {
            Mockito.`when`(planRepo.getById(medicinePlanId)).thenReturn(null)
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(medicineMock)
            Mockito.`when`(profileRepo.getById(profileId)).thenReturn(profileMock)

            useCase.execute(medicinePlanId)
        }
    }

    @Test(expected = MedicineNotFoundException::class)
    fun execute_MedicineNotFound() {
        val medicinePlanId = "aaa"
        val medicineId = "bbb"
        val profileId = "ccc"
        val medicinePlanMock = Plan(
            entityId = medicinePlanId,
            medicineId = medicineId,
            profileId = profileId,
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
        val profileMock = Profile(
            entityId = profileId,
            name = "Wojtek",
            color = "#000000",
            mainPerson = false
        )

        runBlocking {
            Mockito.`when`(planRepo.getById(medicinePlanId)).thenReturn(medicinePlanMock)
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(null)
            Mockito.`when`(profileRepo.getById(profileId)).thenReturn(profileMock)

            useCase.execute(medicinePlanId)
        }
    }

    @Test(expected = ProfileNotFoundException::class)
    fun execute_ProfileNotFound() {
        val medicinePlanId = "aaa"
        val medicineId = "bbb"
        val profileId = "ccc"
        val medicinePlanMock = Plan(
            entityId = medicinePlanId,
            medicineId = medicineId,
            profileId = profileId,
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
        val medicineMock = Medicine(
            entityId = medicineId,
            name = "Hitaxa",
            unit = "tabletki",
            expireDate = AppExpireDate(2020, 3),
            type = "Na katar",
            state = MedicineState(
                packageSize = 0f,
                currState = 0f
            ),
            pictureName = "test.jpg"
        )

        runBlocking {
            Mockito.`when`(planRepo.getById(medicinePlanId)).thenReturn(medicinePlanMock)
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(medicineMock)
            Mockito.`when`(profileRepo.getById(profileId)).thenReturn(null)

            useCase.execute(medicinePlanId)
        }
    }
}