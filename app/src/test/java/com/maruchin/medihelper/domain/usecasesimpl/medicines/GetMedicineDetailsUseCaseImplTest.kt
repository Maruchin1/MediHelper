package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.model.ProfileItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.ProfileRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.domain.utils.DateTimeCalculator
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetMedicineDetailsUseCaseImplTest {

    private val medicineRepo: MedicineRepo = mock()
    private val profileRepo: ProfileRepo = mock()
    private val deviceCalendar: DeviceCalendar = mock()
    private val dateTimeCalculator: DateTimeCalculator = mock()

    private lateinit var useCase: GetMedicineDetailsUseCase

    @Before
    fun before() {
        useCase =
            GetMedicineDetailsUseCaseImpl(
                medicineRepo,
                profileRepo,
                deviceCalendar,
                dateTimeCalculator
            )
    }

    @Test
    fun execute() {
        val medicineId = "abc"
        val medicine = Medicine(
            entityId = medicineId,
            name = "Hitaxa",
            unit = "tabletki",
            expireDate = AppExpireDate(2020, 3),
            type = "Na katar",
            state = MedicineState(
                packageSize = 100f,
                currState = 70f
            ),
            pictureName = "test.jpg"
        )
        val profileList = listOf(
            Profile(
                entityId = "cba",
                name = "Marcin",
                color = "#000000",
                mainPerson = true
            )
        )

        val expectedResult = MedicineDetails(
            medicineId = medicineId,
            name = "Hitaxa",
            unit = "tabletki",
            expireDate = AppExpireDate(2020, 3),
            type = "Na katar",
            daysRemains = 61,
            state = MedicineState(
                packageSize = 100f,
                currState = 70f
            ),
            pictureName = "test.jpg",
            profileItems = listOf(
                ProfileItem(
                    profileId = "cba",
                    name = "Marcin",
                    color = "#000000",
                    mainPerson = true
                )
            )
        )

        runBlocking {
            Mockito.`when`(deviceCalendar.getCurrDate()).thenReturn(AppDate(2019, 12, 31))
            Mockito.`when`(dateTimeCalculator.calcDaysDiff(AppDate(2019, 12, 31), AppExpireDate(2020, 3)))
                .thenReturn(61)
            Mockito.`when`(medicineRepo.getById(medicineId)).thenReturn(medicine)
            Mockito.`when`(profileRepo.getListByMedicine(medicineId)).thenReturn(profileList)

            val result = useCase.execute(medicineId)

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }
}